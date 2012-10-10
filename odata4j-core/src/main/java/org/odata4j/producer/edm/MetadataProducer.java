package org.odata4j.producer.edm;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.core4j.Enumerable;
import org.odata4j.core.NamespacedAnnotation;
import org.odata4j.core.OCollection;
import org.odata4j.core.OCollection.Builder;
import org.odata4j.core.OCollections;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OComplexObjects;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmAnnotationAttribute;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmDecorator;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmItem;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmStructuralType;
import org.odata4j.edm.EdmType;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.format.xml.EdmxFormatWriter;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.CountResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityIdResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ExpressionEvaluator;
import org.odata4j.producer.ExpressionEvaluator.VariableResolver;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.PropertyPath;
import org.odata4j.producer.PropertyPathHelper;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;

/**
 * A producer for $metadata.
 *
 * <p>This is somewhat brute-forceish.  There is maybe a world where an enhanced
 * InMemoryProducer and the org.odata4j.edm pojos together are sufficient to
 * implement much of this...I'm not sure.
 */
public class MetadataProducer implements ODataProducer {

  /**
   * Return this from your decorators annotation override method and the
   * annotation will be removed.
   */
  public static final Object REMOVE_ANNOTATION_OVERRIDE = new Object();

  public static class CustomOptions {

    /**
     * Locale will be parsed as a locale string ala java.util.Locale.
     */
    public static final String Locale = "locale";

    /**
     * If true, a query for a structural type will return a flattened
     * representation of the type..i.e. it will contain inherited properties
     * as well.
     */
    public static final String Flatten = "flatten";
  }

  private final ODataProducer dataProducer;
  private final EdmDataServices edm;
  private final EdmDecorator decorator;

  /**
   * Creates a new MetadataProducer.
   *
   * @param dataProducer  the data producer who defines the $metadata we will expose
   * @param edmDecorator  an optional decorator.  the decorator provides
   *                       context for evaluating $filter expressions, custom
   *                       runtime overrides for annotation values and overrides
   *                       for other metadata properties
   */
  public MetadataProducer(ODataProducer dataProducer, EdmDecorator edmDecorator) {
    this.dataProducer = dataProducer;
    this.decorator = edmDecorator;
    edm = new MetadataEdmGenerator().generateEdm(edmDecorator).build();
  }

  /** Get the EDM model that this producer exposes. */
  public EdmDataServices getModel() {
    return this.dataProducer.getMetadata();
  }

  /** Get the EDM that defines the queryable metadata, the meta-EDM */
  @Override
  public EdmDataServices getMetadata() {
    return edm;
  }

  // request context
  protected class Context implements VariableResolver {

    public Context(String entitySetName, QueryInfo queryInfo) {
      this(entitySetName, queryInfo, null);
    }

    public Context(String entitySetName, QueryInfo queryInfo, OEntityKey key) {
      this.entitySet = edm.findEdmEntitySet(entitySetName);
      this.queryInfo = queryInfo;
      this.entityKey = key;
      setLocale();
      pathHelper = new PropertyPathHelper(queryInfo.select, queryInfo.expand, getCustomOption(PropertyPathHelper.OptionSelectR), getCustomOption(PropertyPathHelper.OptionExpandR));
      flatten = getCustomBoolean(CustomOptions.Flatten, false);
    }

    protected final String getCustomOption(String key) {
      if (queryInfo != null && queryInfo.customOptions != null)
        return queryInfo.customOptions.get(key);
      return null;
    }

    protected final boolean getCustomBoolean(String key, boolean fallback) {
      String s = getCustomOption(key);
      return s == null ? fallback : Boolean.parseBoolean(s);
    }

    protected final void setLocale() {
      String lc = getCustomOption(CustomOptions.Locale);
      if (lc != null) {
        Locale l = parseLocale(lc);
        if (l != null) {
          locale = l;
        }
      }
    }

    public Locale parseLocale(String lstring) {
      String[] s = lstring.split("_", 3);
      if (1 == s.length) {
        return new Locale(s[0]);
      } else if (2 == s.length) {
        return new Locale(s[0], s[1]);
      } else if (3 == s.length) {
        return new Locale(s[0], s[1], s[2]);
      } else {
        return null;
      }
    }

    public void addEntity(OEntity e) {
      entities.add(e);
    }

    EdmEntitySet entitySet;
    QueryInfo queryInfo;
    OEntityKey entityKey;
    Locale locale = Locale.ENGLISH;
    PropertyPathHelper pathHelper;
    List<OEntity> entities = new LinkedList<OEntity>();
    boolean flatten = false; // flatten properties for structural types

    @Override
    public Object resolveVariable(String path) {
      PropertyPath p = new PropertyPath(path);
      EdmItem i = resolverContext.isEmpty() ? null : this.peekResolver();

      if (i != null) {
        if (i instanceof EdmStructuralType) {
          return resolveStructuralTypeVariable((EdmStructuralType) i, p);
        } else if (i instanceof EdmProperty) {
          return resolvePropertyVariable((EdmProperty) i, p);
        }
      }

      throw new NotImplementedException("unhandled EdmItem type in resolveVariable: " + (i == null ? "null" : i.getClass().getName()));
    }

    private Object resolveStructuralTypeVariable(EdmStructuralType et, PropertyPath path) {
      if (path.getNComponents() == 1) {
        String name = path.getLastComponent();
        if (Edm.EntityType.Abstract.equals(name)) {
          return et.getIsAbstract() == null ? false : et.getIsAbstract();
        } else if (Edm.EntityType.BaseType.equals(name)) {
          return et.getBaseType() == null ? null : et.getBaseType().getFullyQualifiedTypeName();
        } else if (Edm.EntityType.Name.equals(name)) {
          return et.getName();
        } else if (Edm.EntityType.Namespace.equals(name)) {
          return et.getNamespace();
        } else {
          // see if our decorator has an annotation that works
          try {
            return decorator.resolveStructuralTypeProperty(et, path);
          } catch (Exception ex) {
            throw new RuntimeException("EdmEntityType property " + name + " not found");
          }
        }
      } else {
        String navProp = path.getFirstComponent();
        // --to 1 props only
        // TODO: superclass maybe
        throw new RuntimeException("EdmEntityType navigation property " + navProp + " not found or not supported");
      }
    }

    private Object resolvePropertyVariable(EdmProperty prop, PropertyPath path) {
      if (path.getNComponents() == 1) {
        String name = path.getLastComponent();
        if (Edm.Property.DefaultValue.equals(name)) {
          return prop.getDefaultValue();
        } else if (Edm.Property.CollectionKind.equals(name)) {
          return prop.getCollectionKind().toString();
        } else if (Edm.Property.EntityTypeName.equals(name)) {
          return prop.getDeclaringType().getName();
        } else if (Edm.Property.FixedLength.equals(name)) {
          return prop.getFixedLength() != null ? prop.getFixedLength().toString() : null;
        } else if (Edm.Property.MaxLength.equals(name)) {
          return prop.getMaxLength() != null ? prop.getMaxLength().toString() : null;
        } else if (Edm.Property.Name.equals(name)) {
          return prop.getName();
        } else if (Edm.Property.Namespace.equals(name)) {
          return prop.getDeclaringType().getNamespace();
        } else if (Edm.Property.Nullable.equals(name)) {
          return prop.isNullable() ? "true" : "false";
        } else if (Edm.Property.Type.equals(name)) {
          return prop.getType().getFullyQualifiedTypeName();
        } else if (Edm.Property.Precision.equals(name)) {
          return prop.getPrecision() == null ? null : prop.getPrecision().toString();
        } else if (Edm.Property.Scale.equals(name)) {
          return prop.getScale() == null ? null : prop.getScale().toString();
        } else if (decorator != null) {
          try {
            return decorator.resolvePropertyProperty(prop, path);
          } catch (IllegalArgumentException e) {
            throw new RuntimeException("EdmProperty property path " + path + " not found");
          }
        } else {
          throw new RuntimeException("EdmProperty property " + name + " not found");
        }
      } else {
        String navProp = path.getFirstComponent();
        // --to 1 props only
        // TODO: class maybe
        throw new RuntimeException("EdmProperty navigation property " + navProp + " not found or not supported");
      }
    }

    private Stack<EdmItem> resolverContext = new Stack<EdmItem>();

    private void pushResolver(EdmItem item) {
      resolverContext.push(item);
    }

    private EdmItem peekResolver() {
      return resolverContext.peek();
    }

    private void popResolver() {
      resolverContext.pop();
    }

  }

  @Override
  public EntitiesResponse getEntities(ODataContext context, String entitySetName, QueryInfo queryInfo) {

    Context c = new Context(entitySetName, queryInfo);
    if (entitySetName.equals(Edm.EntitySets.Schemas)) {
      getSchemas(c);
    } else if (entitySetName.equals(Edm.EntitySets.EntityTypes)) {
      getEntityTypes(c, false);
    } else if (entitySetName.equals(Edm.EntitySets.RootEntityTypes)) {
      getEntityTypes(c, true);
    } else if (entitySetName.equals(Edm.EntitySets.ComplexTypes)) {
      getComplexTypes(c, false);
    } else if (entitySetName.equals(Edm.EntitySets.RootComplexTypes)) {
      getComplexTypes(c, true);
    } else if (entitySetName.equals(Edm.EntitySets.Properties)) {
      getProperties(c);
    } else {
      throw new NotFoundException("EntitySet " + entitySetName + " not found");
    }

    return Responses.entities(c.entities, c.entitySet,
        null, // inline count
        null); // skip token.
  }

  protected void getSchemas(Context c) {
    EdmDataServices ds = dataProducer.getMetadata();
    ExpressionEvaluator f = null;
    if (c.queryInfo != null && c.queryInfo.filter != null) {
      f = new ExpressionEvaluator(c); // , c.queryInfo.filter); // TODO add resolver
    }

    for (EdmSchema schema : ds.getSchemas()) {
      boolean add = true;
      if (f != null) {
        c.pushResolver(schema);
        add = f.evaluate(c.queryInfo.filter);
      }
      if (add) {
        c.addEntity(getSchema(c, schema));
      }
      if (f != null) {
        c.popResolver();
      }
    }
  }

  protected OEntity getSchema(Context c, EdmSchema schema) {
    List<OProperty<?>> props = new ArrayList<OProperty<?>>();
    if (c.pathHelper.isSelected(Edm.Schema.Namespace)) {
      props.add(OProperties.string(Edm.Schema.Namespace, schema.getNamespace()));
    }
    if (schema.getAlias() != null && c.pathHelper.isSelected(Edm.Schema.Alias)) {
      props.add(OProperties.string(Edm.Schema.Alias, schema.getAlias()));
    }

    // links
    List<OLink> links = new LinkedList<OLink>();
    // --------------- ComplexTypes -------------------------------------
    if (c.pathHelper.isSelected(Edm.Schema.NavProps.ComplexTypes)) {
      if (c.pathHelper.isExpanded(Edm.Schema.NavProps.ComplexTypes)) {
        c.pathHelper.navigate(Edm.Schema.NavProps.ComplexTypes);
        List<OEntity> complexTypes = new ArrayList<OEntity>(schema.getComplexTypes().size());
        for (EdmComplexType ct : schema.getComplexTypes()) {
          complexTypes.add(this.getStructuralType(c, ct));
        }
        c.pathHelper.popPath();
        links.add(OLinks.relatedEntitiesInline(null, Edm.Schema.NavProps.ComplexTypes, null, complexTypes));
      } else {
        // deferred
        links.add(OLinks.relatedEntities(null, Edm.Schema.NavProps.ComplexTypes, null));
      }
    } // else not selected

    // --------------- EntityTypes -------------------------------------
    if (c.pathHelper.isSelected(Edm.Schema.NavProps.EntityTypes)) {
      if (c.pathHelper.isExpanded(Edm.Schema.NavProps.EntityTypes)) {
        c.pathHelper.navigate(Edm.Schema.NavProps.EntityTypes);
        List<OEntity> etypes = new ArrayList<OEntity>(schema.getEntityTypes().size());
        for (EdmEntityType et : schema.getEntityTypes()) {
          etypes.add(this.getStructuralType(c, et));
        }
        c.pathHelper.popPath();
        links.add(OLinks.relatedEntitiesInline(null, Edm.Schema.NavProps.EntityTypes, null, etypes));
      } else {
        // deferred
        links.add(OLinks.relatedEntities(null, Edm.Schema.NavProps.EntityTypes, null));
      }
    } // else not selected

    // not sure why CSDL doesn't have documentation on a schema element
    //addDocumenation(c, schema, props);

    addAnnotationProperties(c, schema, props);

    return OEntities.create(c.entitySet,
        OEntityKey.create(Edm.Schema.Namespace, schema.getNamespace()), // OEntityKey entityKey,
        props,
        links);
  }

  protected void getEntityTypes(Context c, boolean isRoot) {
    EdmDataServices ds = dataProducer.getMetadata();
    ExpressionEvaluator f = null;
    if (c.queryInfo != null && c.queryInfo.filter != null) {
      f = new ExpressionEvaluator(c); // , c.queryInfo.filter); // TODO add resolver
    }

    for (EdmEntityType et : ds.getEntityTypes()) {
      if ((isRoot && et.isRootType()) || (!isRoot)) {
        boolean add = true;
        if (f != null) {
          c.pushResolver(et);
          add = f.evaluate(c.queryInfo.filter);
        }
        if (add) {
          c.addEntity(getStructuralType(c, et));
        }
        if (f != null) {
          c.popResolver();
        }
      }
    }
  }

  private OEntity getStructuralType(Context c, EdmStructuralType st) {
    List<OProperty<?>> props = new ArrayList<OProperty<?>>();
    if (c.pathHelper.isSelected(Edm.StructuralType.Name)) {
      props.add(OProperties.string(Edm.StructuralType.Name, st.getName()));
    }
    if (c.pathHelper.isSelected(Edm.StructuralType.Namespace)) {
      props.add(OProperties.string(Edm.StructuralType.Namespace, st.getNamespace()));
    }
    if (st.getIsAbstract() != null && c.pathHelper.isSelected(Edm.StructuralType.Abstract)) {
      props.add(OProperties.boolean_(Edm.StructuralType.Abstract, st.getIsAbstract()));
    }
    if (st.getBaseType() != null) {
      if (c.pathHelper.isSelected(Edm.StructuralType.BaseType)) {
        props.add(OProperties.string(Edm.StructuralType.BaseType, st.getBaseType().getFullyQualifiedTypeName()));
      }
    } else if (st instanceof EdmEntityType && c.pathHelper.isSelected(Edm.EntityType.Key)) {
      // all root types must specify a key
      /*
      * Entity.Key isA EntityKey
      * EntityKey.Keys isA Collection(PropertyRef)
      * PropertyRef.Name isA String
      */
      EdmComplexType propRefType = edm.findEdmComplexType(Edm.PropertyRef.fqName());
      EdmComplexType entityKeyType = edm.findEdmComplexType(Edm.EntityKey.fqName());
      Builder<OComplexObject> builder = OCollections.newBuilder(propRefType);
      for (String key : ((EdmEntityType) st).getKeys()) {
        List<OProperty<?>> refProps = new ArrayList<OProperty<?>>();
        refProps.add(OProperties.string(Edm.PropertyRef.Name, key));
        builder.add(OComplexObjects.create(propRefType, refProps));
      }

      List<OProperty<?>> keyProps = new ArrayList<OProperty<?>>();
      EdmProperty keysProp = entityKeyType.findProperty(Edm.EntityKey.Keys);
      EdmType collectionItemType = entityKeyType.findProperty(Edm.EntityKey.Keys).getType();
      keyProps.add(OProperties.collection(Edm.EntityKey.Keys, new EdmCollectionType(keysProp.getCollectionKind(), collectionItemType), builder.build()));

      OComplexObject key = OComplexObjects.create(entityKeyType, keyProps);

      props.add(OProperties.complex(Edm.EntityType.Key, entityKeyType, key.getProperties()));
    }

    // links
    List<OLink> links = new LinkedList<OLink>();
    // --------------- Properties -------------------------------------
    if (c.pathHelper.isSelected(Edm.StructuralType.NavProps.Properties)) {
      if (c.pathHelper.isExpanded(Edm.StructuralType.NavProps.Properties)) {
        c.pathHelper.navigate(Edm.StructuralType.NavProps.Properties);
        List<OEntity> properties = new ArrayList<OEntity>(st.getDeclaredProperties().count());
        addProperties(st, st, properties, c);
        c.pathHelper.popPath();
        links.add(OLinks.relatedEntitiesInline(null, Edm.StructuralType.NavProps.Properties, null, properties));
      } else {
        // deferred
        links.add(OLinks.relatedEntities(null, Edm.StructuralType.NavProps.Properties, null));
      }
    } // else not selected

    // --------------- SuperType-------------------------------------
    if (c.pathHelper.isSelected(Edm.StructuralType.NavProps.SuperType)) {
      if (c.pathHelper.isExpanded(Edm.StructuralType.NavProps.SuperType)) {

        OEntity superType = null;
        if (st.getBaseType() != null) {
          c.pathHelper.navigate(Edm.StructuralType.NavProps.SuperType);
          superType = this.getStructuralType(c, st.getBaseType());
          c.pathHelper.popPath();
        }

        links.add(OLinks.relatedEntityInline(null, Edm.StructuralType.NavProps.SuperType, null, superType));
      } else {
        // deferred
        links.add(OLinks.relatedEntities(null, Edm.StructuralType.NavProps.SuperType, null));
      }
    } // else not selected

    // --------------- SubTypes-------------------------------------
    if (c.pathHelper.isSelected(Edm.StructuralType.NavProps.SubTypes)) {
      if (c.pathHelper.isExpanded(Edm.StructuralType.NavProps.SubTypes)) {
        List<EdmStructuralType> stypes = Enumerable.create(dataProducer.getMetadata().getSubTypes(st)).toList();
        List<OEntity> subtypes = new ArrayList<OEntity>(stypes.size());
        // these are not root types...
        EdmEntitySet baseSet = c.entitySet;
        if (baseSet.getName().equals(Edm.EntitySets.RootEntityTypes)) {
          c.entitySet = edm.findEdmEntitySet(Edm.EntitySets.EntityTypes);
        } else if (baseSet.getName().equals(Edm.EntitySets.RootComplexTypes)) {
          c.entitySet = edm.findEdmEntitySet(Edm.EntitySets.ComplexTypes);
        }
        c.pathHelper.navigate(Edm.StructuralType.NavProps.SubTypes);
        for (EdmStructuralType subtype : stypes) {
          subtypes.add(this.getStructuralType(c, subtype));
        }
        c.pathHelper.popPath();
        links.add(OLinks.relatedEntitiesInline(null, Edm.StructuralType.NavProps.SubTypes, null, subtypes));
        c.entitySet = baseSet;
      } else {
        // deferred
        links.add(OLinks.relatedEntities(null, Edm.StructuralType.NavProps.SubTypes, null));
      }
    } // else not selected

    addDocumenation(c, st, props);

    addAnnotationProperties(c, st, props);

    return OEntities.create(c.entitySet,
        OEntityKey.create(Edm.StructuralType.Namespace, st.getNamespace(), Edm.StructuralType.Name, st.getName()), // OEntityKey entityKey,
        props,
        links);
  }

  private void addProperties(EdmStructuralType queryType, EdmStructuralType st, List<OEntity> props, Context c) {
    for (EdmProperty p : st.getDeclaredProperties()) {
      props.add(getProperty(queryType, st, p, c));
    }

    if (c.flatten && st.getBaseType() != null) {
      addProperties(queryType, st.getBaseType(), props, c);
    }
  }

  private void addDocumenation(Context c, EdmItem item, List<OProperty<?>> props) {
    if (item.getDocumentation() != null && (item.getDocumentation().getSummary() != null || item.getDocumentation().getLongDescription() != null)
        && c.pathHelper.isSelected(Edm.Documentation.name())) {
      List<OProperty<?>> docProps = new ArrayList<OProperty<?>>();
      EdmComplexType docType = edm.findEdmComplexType(Edm.Documentation.fqName());
      if (item.getDocumentation().getSummary() != null) {
        docProps.add(OProperties.string(Edm.Documentation.Summary, item.getDocumentation().getSummary()));
      }
      if (item.getDocumentation().getLongDescription() != null) {
        docProps.add(OProperties.string(Edm.Documentation.LongDescription, item.getDocumentation().getLongDescription()));
      }
      // OComplexObject doc = OComplexObjects.create(docType, docProps);
      props.add(OProperties.complex(Edm.Documentation.class.getSimpleName(), docType, docProps));
    }
  }

  private void addAnnotationProperties(Context c, EdmItem item, List<OProperty<?>> props) {
    if (item.getAnnotations() != null) {
      for (NamespacedAnnotation<?> a : item.getAnnotations()) {
        if (a.getValue() != null) {
          /*
           * property naming: so...annotations live in a namespace.  JSON doesn't have the concept of namespaces,
           * I think <prefix>_<propname> makes the most sense.  We *could* use <prefix>:<propname> if we quoted the
           * json key..that isn't a universally supported thing though.
           * The issue gets weird with Atom.  The OData spec says that each sub-element of <m:properties> must live
           * in the data service namespace....I guess I'll just use the same JSON name....this of course makes
           * the queryable metadata property names different than the names one would see from $metadata...not
           * sure we can do anything about that.
           */
          String propName = a.getNamespace().getPrefix() + "_" + a.getName();
          if (c.pathHelper.isSelected(propName)) {
            Object override = this.decorator != null ? this.decorator.getAnnotationValueOverride(item, a, c.flatten, c.locale,
                c.queryInfo == null ? null : c.queryInfo.customOptions) : null;

            if (override != MetadataProducer.REMOVE_ANNOTATION_OVERRIDE) {
              Object ov = override == null ? a.getValue() : override;
              if (a instanceof EdmAnnotationAttribute) {
                props.add(OProperties.string(propName, ov.toString()));
              } else if (ov instanceof OComplexObject) {
                OComplexObject co = (OComplexObject) ov;
                props.add(OProperties.complex(propName, (EdmComplexType) co.getType(), co.getProperties()));
              } else if (ov instanceof OCollection) {
                OCollection<?> co = (OCollection<?>) ov;
                props.add(OProperties.collection(propName, new EdmCollectionType(CollectionKind.Bag, co.getType()), co));
              }
            }
          }
        }
      }
    }
  }

  private OEntity getProperty(EdmStructuralType queryType, EdmStructuralType et, EdmProperty p, Context c) {
    List<OProperty<?>> props = new ArrayList<OProperty<?>>();
    if (c.pathHelper.isSelected(Edm.Property.Namespace)) {
      props.add(OProperties.string(Edm.Property.Namespace, et.getNamespace()));
    }
    if (c.pathHelper.isSelected(Edm.Property.EntityTypeName)) {
      props.add(OProperties.string(Edm.Property.EntityTypeName, et.getName()));
    }
    if (c.pathHelper.isSelected(Edm.Property.Name)) {
      props.add(OProperties.string(Edm.Property.Name, p.getName()));
    }
    if (c.pathHelper.isSelected(Edm.Property.Type)) {
      props.add(OProperties.string(Edm.Property.Type, p.getType().getFullyQualifiedTypeName()));
    }
    if (c.pathHelper.isSelected(Edm.Property.Nullable)) {
      props.add(OProperties.boolean_(Edm.Property.Nullable, p.isNullable()));
    }
    if (p.getDefaultValue() != null && c.pathHelper.isSelected(Edm.Property.DefaultValue)) {
      props.add(OProperties.string(Edm.Property.DefaultValue, p.getDefaultValue()));
    }
    if (p.getMaxLength() != null && c.pathHelper.isSelected(Edm.Property.MaxLength)) {
      props.add(OProperties.int32(Edm.Property.MaxLength, p.getMaxLength()));
    }
    if (p.getFixedLength() != null && c.pathHelper.isSelected(Edm.Property.FixedLength)) {
      props.add(OProperties.boolean_(Edm.Property.FixedLength, p.getFixedLength()));
    }
    if (p.getPrecision() != null && c.pathHelper.isSelected(Edm.Property.Precision)) {
      props.add(OProperties.int32(Edm.Property.Precision, p.getPrecision()));
    }
    if (p.getScale() != null && c.pathHelper.isSelected(Edm.Property.Scale)) {
      props.add(OProperties.int32(Edm.Property.Scale, p.getScale()));
    }
    if (p.getUnicode() != null && c.pathHelper.isSelected(Edm.Property.Unicode)) {
      props.add(OProperties.boolean_(Edm.Property.Unicode, p.getUnicode()));
    }
    // TODO: collation
    // TODO: ConcurrencyMode
    if (p.getCollectionKind() != CollectionKind.NONE) {
      props.add(OProperties.string(Edm.Property.CollectionKind, p.getCollectionKind().toString()));
    }

    this.addDocumenation(c, p, props);
    addAnnotationProperties(c, p, props);

    EdmEntitySet entitySet = edm.findEdmEntitySet(Edm.EntitySets.Properties);

    if (this.decorator != null) {
      this.decorator.decorateEntity(entitySet, p, queryType, props, c.flatten, c.locale, c.queryInfo != null ? c.queryInfo.customOptions : null);
    }

    return OEntities.create(entitySet,
        OEntityKey.create(Edm.Property.Namespace, et.getNamespace(), Edm.Property.EntityTypeName, et.getName(), Edm.Property.Name, p.getName()),
        props,
        Collections.<OLink> emptyList());
  }

  protected void getComplexTypes(Context c, boolean isRoot) {
    EdmDataServices ds = dataProducer.getMetadata();

    ExpressionEvaluator f = null;
    if (c.queryInfo != null && c.queryInfo.filter != null) {
      f = new ExpressionEvaluator(c); // , c.queryInfo.filter); // TODO add resolver
    }

    for (EdmComplexType ct : ds.getComplexTypes()) {
      if ((isRoot && ct.isRootType()) || (!isRoot)) {
        boolean add = true;
        if (f != null) {
          c.pushResolver(ct);
          add = f.evaluate(c.queryInfo.filter);
        }
        if (add) {
          c.addEntity(getStructuralType(c, ct));
        }
        if (f != null) {
          c.popResolver();
        }
      }
    }
  }

  protected void getProperties(Context c) {
    EdmDataServices ds = dataProducer.getMetadata();

    ExpressionEvaluator f = null;
    if (c.queryInfo != null && c.queryInfo.filter != null) {
      f = new ExpressionEvaluator(c);
    }

    for (EdmComplexType ct : ds.getComplexTypes()) {
      if (ct.isRootType()) {
        addStructuralTypeProperties(c, ct, f);
      }
    }

    for (EdmEntityType ct : ds.getEntityTypes()) {
      if (ct.isRootType()) {
        addStructuralTypeProperties(c, ct, f);
      }
    }
  }

  protected void addStructuralTypeProperties(Context c, EdmStructuralType st, ExpressionEvaluator ev) {
    for (EdmProperty prop : st.getProperties()) {
      boolean add = true;
      if (ev != null) {
        c.pushResolver(prop);
        add = ev.evaluate(c.queryInfo.filter);
      }
      if (add) {
        c.addEntity(this.getProperty(st, st, prop, c));
      }
      if (ev != null) {
        c.popResolver();
      }
    }

    EdmDataServices ds = dataProducer.getMetadata();
    Iterator<?> candidates = (st instanceof EdmComplexType) ? ds.getComplexTypes().iterator() : ds.getEntityTypes().iterator();
    // down the subtypes hole...
    while (candidates.hasNext()) {
      EdmStructuralType item = (EdmStructuralType) candidates.next();
      if (item.getBaseType() != null && item.getBaseType().equals(st)) {
        addStructuralTypeProperties(c, item, ev);
      }
    }
  }

  @Override
  public EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
    Context c = new Context(entitySetName, queryInfo, entityKey);

    if (entitySetName.equals(Edm.EntitySets.Schemas)) {
      findSchema(c);
    } else if (entitySetName.equals(Edm.EntitySets.EntityTypes)
        || entitySetName.equals(Edm.EntitySets.RootEntityTypes)) {
      findStructuralType(c, true, entitySetName.equals(Edm.EntitySets.RootEntityTypes));
    } else if (entitySetName.equals(Edm.EntitySets.ComplexTypes)
        || entitySetName.equals(Edm.EntitySets.RootComplexTypes)) {
      findStructuralType(c, false, entitySetName.equals(Edm.EntitySets.RootComplexTypes));
    } else {
      throw new NotFoundException("EntitySet " + entitySetName + " not found");
    }

    if (c.entities.isEmpty()) {
      throw new NotFoundException(entitySetName + entityKey.toKeyString() + " not found");
    }

    return Responses.entity(c.entities.get(0));
  }

  protected void findSchema(Context c) {
    EdmDataServices ds = dataProducer.getMetadata();
    String nm = (String) c.entityKey.asSingleValue();
    for (EdmSchema s : ds.getSchemas()) {
      if (nm.equals(s.getNamespace())) {
        c.entities.add(this.getSchema(c, s));
      }
    }
  }

  protected void findStructuralType(Context c, boolean isEntity, boolean root) {
    EdmDataServices ds = dataProducer.getMetadata();
    Iterable<?> candidates = isEntity ? ds.getEntityTypes() : ds.getComplexTypes();
    for (Object eto : candidates) {
      EdmStructuralType st = (EdmStructuralType) eto;

      if (root && st.getBaseType() != null) {
        continue;
      }
      boolean matchedAll = true;
      for (OProperty<?> keyprop : c.entityKey.asComplexProperties()) {
        String val = null;
        if (keyprop.getName().equals(Edm.EntityType.Namespace)) {
          val = st.getNamespace();
        } else if (keyprop.getName().equals(Edm.EntityType.Name)) {
          val = st.getName();
        } else {
          throw new RuntimeException(keyprop.getName() + " is not a key property of " + c.entitySet.getName());
        }
        if (!keyprop.getValue().toString().equals(val)) {
          matchedAll = false;
          break;
        }
      }
      if (matchedAll) {
        c.entities.add(this.getStructuralType(c, st));
      }
    }
    // didn't find it...
  }

  public void dump() {
    StringWriter sw = new StringWriter();
    EdmxFormatWriter.write(edm, sw);
    System.out.println(sw.toString());
  }

  @Override
  public BaseResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public CountResponse getEntitiesCount(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void mergeEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void updateEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public BaseResponse callFunction(ODataContext context, EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public MetadataProducer getMetadataProducer() {
    return null; // stop the brutal recursion :)
  }

  @Override
  public <TExtension extends OExtension<ODataProducer>> TExtension findExtension(Class<TExtension> clazz) {
    return null;
  }
}

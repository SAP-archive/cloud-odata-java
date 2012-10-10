package org.odata4j.producer.edm;

/**
 * A bunch of constants for defining the meta EDM.  This form is nice for ide code completion...
 * including the short class name 'Edm' :)...
 */
public class Edm {

  public static final String namespace = "com.microsoft.schemas.ado._2008._09.edm";
  public static final String ContainerName = "EdmContainer";
  public static final String Collection = "Collection";

  // ------------------------------------------------------------------------
  // some complex types that are used as properties in other edm types:
  public static class PropertyRef {

    public static final String Name = "Name";

    public static String name() {
      return PropertyRef.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + PropertyRef.class.getSimpleName();
    }

  }

  public static class EntityKey {

    public static final String Keys = "Keys";

    public static String name() {
      return EntityKey.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + EntityKey.class.getSimpleName();
    }

  }

  public static class Documentation {

    public static final String Summary = "Summary";
    public static final String LongDescription = "LongDescription";

    public static String name() {
      return Documentation.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + Documentation.class.getSimpleName();
    }

  }

  // ------------------------------------------------------------------------
  /**
   * Note: Schema is *almost* an Item...It doesn't have a Name though..and
   * you can't attach a Documentation to it...weird.
   */
  public static class Schema {

    public static final String Namespace = "Namespace";
    public static final String Alias = "Alias";

    public static String name() {
      return Schema.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + Schema.class.getSimpleName();
    }

    public static class NavProps {
      public static final String EntityTypes = "EntityTypes";
      public static final String ComplexTypes = "ComplexTypes";
    }

  }

  public static class Item {

    /** The Name is one of an Item's key properties */
    public static final String Name = "Name";

  }

  public static class StructuralType extends Item {

    public static final String Namespace = "Namespace"; // key
    public static final String BaseType = "BaseType";
    public static final String Abstract = "Abstract";

    public static String name() {
      return StructuralType.class.getSimpleName();
    }

    public static class NavProps {
      public static final String Properties = "Properties";
      public static final String SubTypes = "SubTypes";
      public static final String SuperType = "SuperType";
    }

  }

  public static class EntityType extends StructuralType {

    public static final String Key = "Key";
    public static final String Documentation = "Documentation";
    public static final String OpenType = "OpenType";

    public static String name() {
      return EntityType.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + EntityType.class.getSimpleName();
    }

  }

  public static class ComplexType extends StructuralType {

    public static String name() {
      return ComplexType.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + ComplexType.class.getSimpleName();
    }

  }

  public static class Property extends Item {

    public static final String Namespace = "Namespace"; // key
    public static final String EntityTypeName = "EntityTypeName"; // key
    public static final String Type = "Type";
    public static final String Nullable = "Nullable";
    public static final String DefaultValue = "DefaultValue";
    public static final String MaxLength = "MaxLength";
    public static final String FixedLength = "FixedLength";
    public static final String Precision = "Precision";
    public static final String Scale = "Scale";
    public static final String Unicode = "Unicode";
    public static final String Collation = "Collation";
    public static final String ConcurrencyMode = "ConcurrencyMode";
    public static final String CollectionKind = "CollectionKind";

    public static String name() {
      return Property.class.getSimpleName();
    }

    public static String fqName() {
      return Edm.namespace + "." + Property.class.getSimpleName();
    }

  }

  public static class EntitySets {

    /** All of the Schemas */
    public static final String Schemas = "Schemas";
    /** All EntityTypes */
    public static final String EntityTypes = "EntityTypes";
    /** All EntityTypes that do *not* have a BaseType */
    public static final String RootEntityTypes = "RootEntityTypes";
    /** All Properties of all structural types */
    public static final String Properties = "Properties";
    /** All ComplexTypes */
    public static final String ComplexTypes = "ComplexTypes";
    /** All ComplexTypes that do *not* have a BaseType */
    public static final String RootComplexTypes = "RootComplexTypes";

  }

}

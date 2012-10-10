package org.odata4j.edm;

import org.core4j.Func1;
import org.odata4j.core.ImmutableList;
import org.odata4j.core.Named;

/**
 * A CSDL Association element.
 *
 * <p>An Association element defines a relationship between two entity types. An association must specify the entity types
 * that are involved in the relationship and the possible number of entity types at each end of the relationship, which is
 * known as the multiplicity. The multiplicity of an association end can have a value of one (1), zero or one (0..1), or
 * many (*). This information is specified in two child End elements.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399734.aspx">[msdn] Association Element (CSDL)</a>
 */
public class EdmAssociation extends EdmItem {

  private final String namespace;
  private final String alias;
  private final String name;
  private final EdmAssociationEnd end1;
  private final EdmAssociationEnd end2;
  private final EdmReferentialConstraint refConstraint;

  private EdmAssociation(String namespace, String alias, String name, EdmAssociationEnd end1, EdmAssociationEnd end2,
      EdmReferentialConstraint refConstraint, EdmDocumentation doc, ImmutableList<EdmAnnotation<?>> annots,
      ImmutableList<EdmAnnotation<?>> anElements) {
    super(doc, annots, anElements);
    this.namespace = namespace;
    this.alias = alias;
    this.name = name;
    this.end1 = end1;
    this.end2 = end2;
    this.refConstraint = refConstraint;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getAlias() {
    return alias;
  }

  public String getName() {
    return name;
  }

  public EdmAssociationEnd getEnd1() {
    return end1;
  }

  public EdmAssociationEnd getEnd2() {
    return end2;
  }

  public EdmReferentialConstraint getRefConstraint() {
    return refConstraint;
  }

  public String getFQNamespaceName() {
    return namespace + "." + name;
  }

  public String getFQAliasName() {
    return alias == null ? null : (alias + "." + name);
  }

  @Override
  public String toString() {
    StringBuilder rt = new StringBuilder();
    rt.append("EdmAssociation[");
    if (namespace != null)
      rt.append(namespace + ".");
    rt.append(name);
    if (alias != null)
      rt.append(",alias=" + alias);
    rt.append(",end1=" + end1);
    rt.append(",end2=" + end2);
    rt.append(']');
    return rt.toString();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmAssociation association, BuilderContext context) {
    return context.newBuilder(association, new Builder());
  }

  /** Mutable builder for {@link EdmAssociation} objects. */
  public static class Builder extends EdmItem.Builder<EdmAssociation, Builder> implements Named {

    private String namespace;
    private String alias;
    private String name;
    private EdmAssociationEnd.Builder end1;
    private EdmAssociationEnd.Builder end2;
    private EdmReferentialConstraint.Builder refConstraint;

    @Override
    Builder newBuilder(EdmAssociation association, BuilderContext context) {
      this.name = association.name;
      this.alias = association.alias;
      this.namespace = association.namespace;
      this.end1 = EdmAssociationEnd.newBuilder(association.end1, context);
      this.end2 = EdmAssociationEnd.newBuilder(association.end2, context);
      this.refConstraint = EdmReferentialConstraint.newBuilder();
      return this;
    }

    public EdmAssociation build() {
      return new EdmAssociation(namespace, alias, name, end1.build(), end2.build(),
          refConstraint == null ? null : refConstraint.build(), getDocumentation(),
          ImmutableList.copyOf(getAnnotations()), ImmutableList.copyOf(getAnnotationElements()));
    }

    public EdmAssociationEnd.Builder getEnd1() {
      return end1;
    }

    public EdmAssociationEnd.Builder getEnd2() {
      return end2;
    }

    public EdmReferentialConstraint.Builder getRefConstraint() {
      return refConstraint;
    }

    public String getNamespace() {
      return namespace;
    }

    public String getAlias() {
      return alias;
    }

    public Builder setNamespace(String namespace) {
      this.namespace = namespace;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setAlias(String alias) {
      this.alias = alias;
      return this;
    }

    public String getFQNamespaceName() {
      // TODO share or remove
      return namespace + "." + name;
    }

    public String getFQAliasName() {
      // TODO share or remove
      return alias == null ? null : (alias + "." + name);
    }

    public Builder setEnds(EdmAssociationEnd.Builder end1, EdmAssociationEnd.Builder end2) {
      this.end1 = end1;
      this.end2 = end2;
      return this;
    }

    public Builder setRefConstraint(EdmReferentialConstraint.Builder refConstraint) {
      this.refConstraint = refConstraint;
      return this;
    }

    public String getName() {
      return name;
    }

    public static Func1<EdmAssociation.Builder, String> func1_getFQNamespaceName() {
      return new Func1<EdmAssociation.Builder, String>() {
        @Override
        public String apply(Builder input) {
          return input.getFQNamespaceName();
        }
      };
    }

  }

}

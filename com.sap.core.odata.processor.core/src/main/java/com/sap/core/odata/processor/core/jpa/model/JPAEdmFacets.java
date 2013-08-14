package com.sap.core.odata.processor.core.jpa.model;

import java.lang.reflect.AnnotatedElement;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.SimpleProperty;

public final class JPAEdmFacets {

  public static void setFacets(final Attribute<?, ?> jpaAttribute, final SimpleProperty edmProperty) {
    EdmSimpleTypeKind edmTypeKind = edmProperty.getType();
    Facets facets = new Facets();
    edmProperty.setFacets(facets);

    Column column = null;
    if (jpaAttribute.getJavaMember() instanceof AnnotatedElement) {
      column = ((AnnotatedElement) jpaAttribute
          .getJavaMember()).getAnnotation(Column.class);
    }

    if (column == null) return;

    setNullable(column, edmProperty);

    switch (edmTypeKind) {
    case Binary:
      setMaxLength(column, edmProperty);
      break;
    case DateTime:
      setPrecision(column, edmProperty);
      break;
    case DateTimeOffset:
      setPrecision(column, edmProperty);
      break;
    case Time:
      setPrecision(column, edmProperty);
      break;
    case Decimal:
      setPrecision(column, edmProperty);
      setScale(column, edmProperty);
      break;
    case String:
      setMaxLength(column, edmProperty);
      break;
    default:
      break;
    }
  }

  private static void setNullable(final Column column, final SimpleProperty edmProperty) {
    ((Facets) edmProperty.getFacets()).setNullable(column.nullable());
  }

  private static void setMaxLength(final Column column, final SimpleProperty edmProperty) {
    if (column.length() > 0) {
      ((Facets) edmProperty.getFacets()).setMaxLength(column.length());
    }
  }

  private static void setPrecision(final Column column, final SimpleProperty edmProperty) {
    if (column.precision() > 0) {
      ((Facets) edmProperty.getFacets()).setPrecision(column.precision());
    }
  }

  private static void setScale(final Column column, final SimpleProperty edmProperty) {
    if (column.scale() > 0) {
      ((Facets) edmProperty.getFacets()).setScale(column.scale());
    }
  }
}

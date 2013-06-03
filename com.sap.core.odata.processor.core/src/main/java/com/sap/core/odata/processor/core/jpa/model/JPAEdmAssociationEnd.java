package com.sap.core.odata.processor.core.jpa.model;

import java.lang.reflect.AnnotatedElement;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmAssociationEnd extends JPAEdmBaseViewImpl implements
    JPAEdmAssociationEndView {

  private JPAEdmEntityTypeView entityTypeView = null;
  private JPAEdmPropertyView propertyView = null;
  private AssociationEnd currentAssociationEnd1 = null;
  private AssociationEnd currentAssociationEnd2 = null;
  private String columnName;
  private String referencedColumnName;
  private String mappedBy;
  private String ownerPropertyName;

  public JPAEdmAssociationEnd(final JPAEdmEntityTypeView entityTypeView,
      final JPAEdmPropertyView propertyView) {
    super(entityTypeView);
    this.entityTypeView = entityTypeView;
    this.propertyView = propertyView;
  }

  @Override
  public JPAEdmBuilder getBuilder() {
    if (builder == null) {
      builder = new JPAEdmAssociationEndBuilder();
    }

    return builder;
  }

  @Override
  public AssociationEnd getEdmAssociationEnd1() {
    return currentAssociationEnd1;
  }

  @Override
  public AssociationEnd getEdmAssociationEnd2() {
    return currentAssociationEnd2;
  }

  private class JPAEdmAssociationEndBuilder implements JPAEdmBuilder {

    @Override
    public void build() throws ODataJPAModelException {

      JoinColumn joinColumn = null;

      currentAssociationEnd1 = new AssociationEnd();
      currentAssociationEnd2 = new AssociationEnd();

      JPAEdmNameBuilder.build(JPAEdmAssociationEnd.this, entityTypeView,
          propertyView);

      currentAssociationEnd1.setRole(currentAssociationEnd1.getType()
          .getName());
      currentAssociationEnd2.setRole(currentAssociationEnd2.getType()
          .getName());

      setEdmMultiplicity(propertyView.getJPAAttribute()
          .getPersistentAttributeType());

      AnnotatedElement annotatedElement = (AnnotatedElement) propertyView.getJPAAttribute()
          .getJavaMember();
      if (annotatedElement != null) {
        joinColumn = annotatedElement.getAnnotation(JoinColumn.class);
        if (joinColumn != null) {
          columnName = joinColumn.name();
          referencedColumnName = joinColumn.referencedColumnName();
        }

      }
      ownerPropertyName = propertyView.getJPAAttribute().getName();

    }

    private void setEdmMultiplicity(final PersistentAttributeType type) {
      AnnotatedElement annotatedElement = (AnnotatedElement) propertyView.getJPAAttribute()
          .getJavaMember();
      switch (type) {
      case ONE_TO_MANY:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.ONE);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.MANY);
        if (annotatedElement != null) {
          OneToMany reln = annotatedElement.getAnnotation(OneToMany.class);
          if (reln != null) {
            mappedBy = reln.mappedBy();
          }
        }
        break;
      case MANY_TO_MANY:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.MANY);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.MANY);
        if (annotatedElement != null) {
          ManyToMany reln = annotatedElement.getAnnotation(ManyToMany.class);
          if (reln != null) {
            mappedBy = reln.mappedBy();
          }
        }
        break;
      case MANY_TO_ONE:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.MANY);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.ONE);
        break;
      case ONE_TO_ONE:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.ONE);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.ONE);
        if (annotatedElement != null) {
          OneToOne reln = annotatedElement.getAnnotation(OneToOne.class);
          if (reln != null) {
            mappedBy = reln.mappedBy();
          }
        }
        break;
      default:
        break;
      }
    }
  }

  @Override
  public boolean compare(final AssociationEnd end1, final AssociationEnd end2) {
    if ((end1.getType().equals(currentAssociationEnd1.getType())
        && end2.getType().equals(currentAssociationEnd2.getType())
        && end1.getMultiplicity().equals(
            currentAssociationEnd1.getMultiplicity()) && end2
        .getMultiplicity().equals(
            currentAssociationEnd2.getMultiplicity()))
        || (end1.getType().equals(currentAssociationEnd2.getType())
            && end2.getType().equals(
                currentAssociationEnd1.getType())
            && end1.getMultiplicity().equals(
                currentAssociationEnd2.getMultiplicity()) && end2
            .getMultiplicity().equals(
                currentAssociationEnd1.getMultiplicity()))) {
      return true;
    }

    return false;
  }

  @Override
  public String getJoinColumnName() {
    return columnName;
  }

  @Override
  public String getJoinColumnReferenceColumnName() {
    return referencedColumnName;
  }

  @Override
  public String getMappedByName() {
    return mappedBy;
  }

  @Override
  public String getOwningPropertyName() {
    return ownerPropertyName;
  }

}

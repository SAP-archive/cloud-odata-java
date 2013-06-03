package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmAssociation extends JPAEdmBaseViewImpl implements
    JPAEdmAssociationView {

  private JPAEdmAssociationEndView associationEndView;

  private Association currentAssociation;
  private List<Association> consistentAssociatonList;
  private HashMap<String, Association> associationMap;
  private HashMap<String, JPAEdmAssociationEndView> associationEndMap;
  private List<JPAEdmReferentialConstraintView> inconsistentRefConstraintViewList;
  private int numberOfSimilarEndPoints;

  public JPAEdmAssociation(final JPAEdmAssociationEndView associationEndview,
      final JPAEdmEntityTypeView entityTypeView,
      final JPAEdmPropertyView propertyView, final int value) {
    super(associationEndview);
    associationEndView = associationEndview;
    numberOfSimilarEndPoints = value;
    init();
  }

  public JPAEdmAssociation(final JPAEdmSchemaView view) {
    super(view);
    init();
  }

  private void init() {
    isConsistent = false;
    consistentAssociatonList = new ArrayList<Association>();
    inconsistentRefConstraintViewList = new LinkedList<JPAEdmReferentialConstraintView>();
    associationMap = new HashMap<String, Association>();
    associationEndMap = new HashMap<String, JPAEdmAssociationEndView>();
  }

  @Override
  public JPAEdmBuilder getBuilder() {
    if (builder == null) {
      builder = new JPAEdmAssociationBuilder();
    }
    return builder;
  }

  @Override
  public Association getEdmAssociation() {
    return currentAssociation;
  }

  @Override
  public List<Association> getConsistentEdmAssociationList() {
    return consistentAssociatonList;
  }

  @Override
  public Association searchAssociation(final JPAEdmAssociationEndView view) {
    if (view != null) {
      for (String key : associationMap.keySet()) {
        Association association = associationMap.get(key);
        if (association != null) {
          if (view.compare(association.getEnd1(),
              association.getEnd2())) {
            JPAEdmAssociationEndView associationEnd = associationEndMap
                .get(association.getName());
            if (associationEnd.getJoinColumnName() != null
                && associationEnd
                    .getJoinColumnReferenceColumnName() != null
                && view.getJoinColumnName() != null
                && view.getJoinColumnReferenceColumnName() != null) {
              if (view.getJoinColumnName().equals(
                  associationEnd.getJoinColumnName())
                  && view.getJoinColumnReferenceColumnName()
                      .equals(associationEnd
                          .getJoinColumnReferenceColumnName())) {
                currentAssociation = association;
                return association;
              }

            }
            if (associationEnd.getMappedByName() != null) {
              if (associationEnd.getMappedByName().equals(
                  view.getOwningPropertyName())) {
                currentAssociation = association;
                return association;
              }
            }
            if (associationEnd.getOwningPropertyName() != null) {
              if (associationEnd.getOwningPropertyName().equals(
                  view.getMappedByName())) {
                currentAssociation = association;
                return association;
              }
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public void addJPAEdmAssociationView(final JPAEdmAssociationView associationView,
      final JPAEdmAssociationEndView associationEndView) {
    if (associationView != null) {
      currentAssociation = associationView.getEdmAssociation();
      associationMap
          .put(currentAssociation.getName(), currentAssociation);
      associationEndMap.put(currentAssociation.getName(),
          associationEndView);
      addJPAEdmRefConstraintView(associationView
          .getJPAEdmReferentialConstraintView());
    }
  }

  @Override
  public void addJPAEdmRefConstraintView(
      final JPAEdmReferentialConstraintView refView) {
    if (refView != null && refView.isExists()) {
      inconsistentRefConstraintViewList.add(refView);
    }
  }

  @Override
  public JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView() {
    if (inconsistentRefConstraintViewList.isEmpty()) {
      return null;
    }
    return inconsistentRefConstraintViewList.get(0);
  }

  private class JPAEdmAssociationBuilder implements JPAEdmBuilder {

    @Override
    public void build() throws ODataJPAModelException,
        ODataJPARuntimeException {

      if (associationEndView != null
          && searchAssociation(associationEndView) == null) {
        currentAssociation = new Association();
        currentAssociation.setEnd1(associationEndView
            .getEdmAssociationEnd1());
        currentAssociation.setEnd2(associationEndView
            .getEdmAssociationEnd2());

        JPAEdmNameBuilder.build(JPAEdmAssociation.this,
            numberOfSimilarEndPoints);

        associationMap.put(currentAssociation.getName(),
            currentAssociation);

      } else if (!inconsistentRefConstraintViewList.isEmpty()) {
        int inconsistentRefConstraintViewSize = inconsistentRefConstraintViewList
            .size();
        int index = 0;
        for (int i = 0; i < inconsistentRefConstraintViewSize; i++) {
          JPAEdmReferentialConstraintView view = inconsistentRefConstraintViewList
              .get(index);

          if (view.isExists() && !view.isConsistent()) {
            view.getBuilder().build();
          }
          if (view.isConsistent()) {
            Association newAssociation = new Association();
            copyAssociation(newAssociation, associationMap.get(view
                .getEdmRelationShipName()));
            newAssociation.setReferentialConstraint(view
                .getEdmReferentialConstraint());
            consistentAssociatonList.add(newAssociation);
            associationMap.put(view.getEdmRelationShipName(),
                newAssociation);
            inconsistentRefConstraintViewList.remove(index);
          } else {
            associationMap.remove(view.getEdmRelationShipName());
            index++;
          }
        }
      }

      if (associationMap.size() == consistentAssociatonList.size()) {
        isConsistent = true;
      } else {
        for (String key : associationMap.keySet()) {
          Association association = associationMap.get(key);
          if (!consistentAssociatonList.contains(association)) {
            consistentAssociatonList.add(association);
          }
        }
        isConsistent = true;
      }

    }

    private void copyAssociation(final Association copyToAssociation,
        final Association copyFromAssociation) {
      copyToAssociation.setEnd1(copyFromAssociation.getEnd1());
      copyToAssociation.setEnd2(copyFromAssociation.getEnd2());
      copyToAssociation.setName(copyFromAssociation.getName());
      copyToAssociation.setAnnotationAttributes(copyFromAssociation
          .getAnnotationAttributes());
      copyToAssociation.setAnnotationElements(copyFromAssociation
          .getAnnotationElements());
      copyToAssociation.setDocumentation(copyFromAssociation
          .getDocumentation());

    }
  }

  @Override
  public int getNumberOfAssociationsWithSimilarEndPoints(
      final JPAEdmAssociationEndView view) {
    int count = 0;
    AssociationEnd currentAssociationEnd1 = view.getEdmAssociationEnd1();
    AssociationEnd currentAssociationEnd2 = view.getEdmAssociationEnd2();
    AssociationEnd end1 = null;
    AssociationEnd end2 = null;
    for (String key : associationMap.keySet()) {
      Association association = associationMap.get(key);
      if (association != null) {
        end1 = association.getEnd1();
        end2 = association.getEnd2();
        if ((end1.getType().equals(currentAssociationEnd1.getType()) && end2
            .getType().equals(currentAssociationEnd2.getType()))
            || (end1.getType().equals(
                currentAssociationEnd2.getType()) && end2
                .getType().equals(
                    currentAssociationEnd1.getType()))) {
          count++;
        }
      }
    }
    return count;
  }

}

package com.sap.core.odata.processor.jpa.model;

import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmAssociationEnd extends JPAEdmBaseViewImpl implements
		JPAEdmAssociationEndView {

	private JPAEdmEntityTypeView entityTypeView = null;
	private JPAEdmPropertyView propertyView = null;
	private AssociationEnd currentAssociationEnd1 = null;
	private AssociationEnd currentAssociationEnd2 = null;

	public JPAEdmAssociationEnd(JPAEdmEntityTypeView entityTypeView,
			JPAEdmPropertyView propertyView) {
		super(entityTypeView);
		this.entityTypeView = entityTypeView;
		this.propertyView = propertyView;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmAssociationEndBuilder();
	}

	@Override
	public AssociationEnd getAssociationEnd1() {
		return this.currentAssociationEnd1;
	}

	@Override
	public AssociationEnd getAssociationEnd2() {
		return currentAssociationEnd2;
	}

	private class JPAEdmAssociationEndBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

			currentAssociationEnd1 = new AssociationEnd();
			currentAssociationEnd2 = new AssociationEnd();

			JPAEdmNameBuilder.build(JPAEdmAssociationEnd.this,entityTypeView,propertyView);

			currentAssociationEnd1.setRole(currentAssociationEnd1.getType()
					.getName());
			currentAssociationEnd2.setRole(currentAssociationEnd2.getType()
					.getName());

			setEdmMultiplicity(propertyView.getJPAAttribute()
					.getPersistentAttributeType());
		}

		private void setEdmMultiplicity(PersistentAttributeType type) {
			switch (type) {
			case ONE_TO_MANY:
				currentAssociationEnd1.setMultiplicity(EdmMultiplicity.ONE);
				currentAssociationEnd2.setMultiplicity(EdmMultiplicity.MANY);
				break;
			case MANY_TO_MANY:
				currentAssociationEnd1.setMultiplicity(EdmMultiplicity.MANY);
				currentAssociationEnd2.setMultiplicity(EdmMultiplicity.MANY);
				break;
			case MANY_TO_ONE:
				currentAssociationEnd1.setMultiplicity(EdmMultiplicity.MANY);
				currentAssociationEnd2.setMultiplicity(EdmMultiplicity.ONE);
				break;
			case ONE_TO_ONE:
				currentAssociationEnd1.setMultiplicity(EdmMultiplicity.ONE);
				currentAssociationEnd2.setMultiplicity(EdmMultiplicity.ONE);
				break;
			default:
				break;
			}
		}
	}

}

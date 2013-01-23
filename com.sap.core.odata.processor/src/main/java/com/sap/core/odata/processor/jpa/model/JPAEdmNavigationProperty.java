package com.sap.core.odata.processor.jpa.model;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;

import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmNavigationProperty extends JPAEdmBaseViewImpl implements
		JPAEdmNavigationPropertyView {

	private JPAEdmSchemaView schemaView = null;;

	public JPAEdmNavigationProperty(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmNavigationPropertyBuilder();
	}

	private class JPAEdmNavigationPropertyBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {
			NavigationProperty navigationProperty = new NavigationProperty();

			JPAEdmEntityTypeView entityTypeView = schemaView
					.getJPAEdmEntityContainerView().getJPAEdmEntitySetView()
					.getJPAEdmEntityTypeView();

			EntityType<?> jpaEntityType = entityTypeView.getJPAEntityType();
			for (Attribute<?, ?> attribute : jpaEntityType.getAttributes()) {
				PersistentAttributeType attributeType = attribute
						.getPersistentAttributeType();

				
				
				switch (attributeType) {
				case MANY_TO_MANY:

					break;
				case ONE_TO_MANY:

					break;
				case MANY_TO_ONE:
					break;
				case ONE_TO_ONE:
					break;

				default:
					break;
				}
			}

		}
	}

}

package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.api.model.JPAEdmKeyView;
import com.sap.core.odata.processor.api.model.JPAEdmPropertyView;

public class JPAEdmKey extends JPAEdmBaseViewImpl implements JPAEdmKeyView {

	private JPAEdmPropertyView propertyView;
	private JPAEdmComplexTypeView complexTypeView = null;
	private boolean isBuildModeComplexType = false;
	private Key key;

	public JPAEdmKey(JPAEdmProperty view) {
		super(view);
		this.propertyView = view;
	}

	public JPAEdmKey(JPAEdmComplexTypeView complexTypeView,
			JPAEdmPropertyView propertyView) {
		super(complexTypeView);
		this.propertyView = propertyView;
		this.complexTypeView = complexTypeView;
		isBuildModeComplexType = true;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (this.builder == null)
			this.builder = new JPAEdmKeyBuider();
		
		return builder;
	}

	@Override
	public Key getEdmKey() {
		return this.key;
	}

	private class JPAEdmKeyBuider implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

			List<PropertyRef> propertyRefList = null;
			if (key == null)
				key = new Key();

			if (key.getKeys() == null) {
				propertyRefList = new ArrayList<PropertyRef>();
				key.setKeys(propertyRefList);
			} else {
				propertyRefList = key.getKeys();
			}

			if (isBuildModeComplexType) {
				ComplexType complexType = complexTypeView
						.searchEdmComplexType(propertyView
								.getJPAAttribute().getJavaType().getName());
				normalizeComplexKey(complexType, propertyRefList);
			} else {
				PropertyRef propertyRef = new PropertyRef();
				propertyRef.setName(propertyView.getEdmSimpleProperty().getName());
				propertyRefList.add(propertyRef);
			}

		}
		//TODO think how to stop the recursion if A includes B and B includes A!!!!!!
		public void normalizeComplexKey(ComplexType complexType,List<PropertyRef> propertyRefList) {
			for (Property property : complexType.getProperties()) {
				try {

					SimpleProperty simpleProperty = (SimpleProperty) property;

					PropertyRef propertyRef = new PropertyRef();
					propertyRef.setName(simpleProperty.getName());
					propertyRefList.add(propertyRef);

				} catch (ClassCastException e) {
					ComplexProperty complexProperty = (ComplexProperty) property;
					normalizeComplexKey(complexTypeView.searchEdmComplexType(complexProperty.getType()), propertyRefList);
				}

			}
		}
	}
}

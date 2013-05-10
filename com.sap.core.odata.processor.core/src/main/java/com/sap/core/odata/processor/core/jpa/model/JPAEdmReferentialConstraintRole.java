package com.sap.core.odata.processor.core.jpa.model;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.metamodel.Attribute;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.ReferentialConstraintRole;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintRoleView;

public class JPAEdmReferentialConstraintRole extends JPAEdmBaseViewImpl
		implements JPAEdmReferentialConstraintRoleView {
	/*
	 * Static Buffer
	 */
	private static Attribute<?, ?> bufferedJPAAttribute = null;
	private static ArrayList<JoinColumn> bufferedJoinColumns = new ArrayList<JoinColumn>();
	/*
	 * Static Buffer
	 */

	private boolean firstBuild = true;

	private JPAEdmEntityTypeView entityTypeView;
	private JPAEdmReferentialConstraintRoleView.RoleType roleType;

	private Attribute<?, ?> jpaAttribute;
	private ArrayList<String> jpaColumnNames;
	private Association association;

	private boolean roleExists = false;

	private JPAEdmRefConstraintRoleBuilder builder;
	private ReferentialConstraintRole currentRole;

	public JPAEdmReferentialConstraintRole(
			JPAEdmReferentialConstraintRoleView.RoleType roleType,
			JPAEdmEntityTypeView entityTypeView,
			JPAEdmPropertyView propertyView,
			JPAEdmAssociationView associationView) {

		super(entityTypeView);
		this.entityTypeView = entityTypeView;
		this.roleType = roleType;

		jpaAttribute = propertyView.getJPAAttribute();
		association = associationView.getEdmAssociation();

	}

	@Override
	public boolean isExists() {
		return roleExists;

	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (builder == null)
			builder = new JPAEdmRefConstraintRoleBuilder();

		return builder;
	}

	@Override
	public RoleType getRoleType() {
		return roleType;
	}

	@Override
	public ReferentialConstraintRole getEdmReferentialConstraintRole() {
		return currentRole;
	}

	@Override
	public String getJPAColumnName() {
		return null;
	}

	@Override
	public String getEdmEntityTypeName() {
		return null;
	}

	@Override
	public String getEdmAssociationName() {
		return null;
	}

	private class JPAEdmRefConstraintRoleBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {
			if (firstBuild) {
				firstBuild();
			} else if (roleExists) {
				try {
					buildRole();
				} catch (SecurityException e) {
					throw ODataJPAModelException.throwException(
							ODataJPAModelException.GENERAL.addContent(e
									.getMessage()), e);
				} catch (NoSuchFieldException e) {
					throw ODataJPAModelException.throwException(
							ODataJPAModelException.GENERAL.addContent(e
									.getMessage()), e);
				}
			}

		}

		private void firstBuild() {
			firstBuild = false;
			isConsistent = false;

			extractJoinColumns();

			if (!roleExists)
				return;

			jpaColumnNames = new ArrayList<String>();

			for (JoinColumn joinColumn : bufferedJoinColumns) {
				if (roleType == RoleType.PRINCIPAL)
					jpaColumnNames.add(joinColumn.referencedColumnName());
				else if (roleType == RoleType.DEPENDENT)
					jpaColumnNames.add(joinColumn.name());
			}

		}

		private void buildRole() throws SecurityException, NoSuchFieldException {

			if (currentRole == null) {
				currentRole = new ReferentialConstraintRole();
				String jpaAttributeType = null;
				EntityType edmEntityType = null;

				if (roleType == RoleType.PRINCIPAL) {
					jpaAttributeType = jpaAttribute.getJavaType()
							.getSimpleName();
					if (jpaAttributeType.equals("List")) {
						Type type = ((ParameterizedType) jpaAttribute
								.getJavaMember().getDeclaringClass()
								.getDeclaredField(jpaAttribute.getName())
								.getGenericType()).getActualTypeArguments()[0];
						int lastIndexOfDot = type.toString().lastIndexOf(".");
						jpaAttributeType = type.toString().substring(
								lastIndexOfDot + 1);
					}
					edmEntityType = entityTypeView
							.searchEdmEntityType(jpaAttributeType);

				}

				else if (roleType == RoleType.DEPENDENT)
					edmEntityType = entityTypeView
							.searchEdmEntityType(jpaAttribute
									.getDeclaringType().getJavaType()
									.getSimpleName());

				List<PropertyRef> propertyRefs = new ArrayList<PropertyRef>();
				if (edmEntityType != null) {
					for (String columnName : jpaColumnNames) {
						for (Property property : edmEntityType.getProperties()) {
							if (columnName.equals(((JPAEdmMapping) property
									.getMapping()).getJPAColumnName())) {
								PropertyRef propertyRef = new PropertyRef();
								propertyRef.setName(property.getName());
								propertyRefs.add(propertyRef);
								break;
							}
						}
					}
					currentRole.setPropertyRefs(propertyRefs);
					if (propertyRefs.isEmpty()) {
						isConsistent = false;
						return;
					}
					AssociationEnd end = association.getEnd1();
					if (end.getType().getName().equals(edmEntityType.getName())) {
						currentRole.setRole(end.getRole());
						isConsistent = true;
					} else {
						end = association.getEnd2();
						if (end.getType().getName()
								.equals(edmEntityType.getName())) {
							currentRole.setRole(end.getRole());
							isConsistent = true;
						}
					}
				}

			}
		}

		private void extractJoinColumns() {
			/*
			 * Check against Static Buffer whether the join column was already
			 * extracted.
			 */
			if (!jpaAttribute.equals(bufferedJPAAttribute)) {
				bufferedJPAAttribute = jpaAttribute;
				bufferedJoinColumns.clear();
			} else if (bufferedJoinColumns.isEmpty()) {
				roleExists = false;
				return;
			} else {
				roleExists = true;
				return;
			}

			AnnotatedElement annotatedElement = (AnnotatedElement) jpaAttribute
					.getJavaMember();

			if (annotatedElement == null)
				return;

			JoinColumn joinColumn = annotatedElement
					.getAnnotation(JoinColumn.class);
			if (joinColumn == null) {
				JoinColumns joinColumns = annotatedElement
						.getAnnotation(JoinColumns.class);

				if (joinColumns != null) {
					JoinColumn[] joinColumnArray = joinColumns.value();

					for (int i = 0; i < joinColumnArray.length; i++)
						bufferedJoinColumns.add(joinColumnArray[i]);
				} else
					return;
			} else {
				bufferedJoinColumns.add(joinColumn);
			}
			roleExists = true;
		}
	}
}

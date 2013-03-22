package com.sap.core.odata.processor.core.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmBaseView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension;

public abstract class JPAEdmBaseViewImpl implements JPAEdmBaseView {

	protected String pUnitName = null;
	protected Metamodel metaModel = null;
	protected boolean isConsistent = true;
	protected JPAEdmBuilder builder = null;
	protected JPAEdmExtension jpaEdmExtension = null;
	private JPAEdmMappingModelAccess jpaEdmMappingModelAccess = null;

	public JPAEdmBaseViewImpl(JPAEdmBaseView view) {
		this.pUnitName = view.getpUnitName();
		this.metaModel = view.getJPAMetaModel();
		this.jpaEdmMappingModelAccess = view.getJPAEdmMappingModelAccess();
		this.jpaEdmExtension = view.getJPAEdmExtension();
	}

	public JPAEdmBaseViewImpl(ODataJPAContext context) {
		this.pUnitName = context.getPersistenceUnitName();
		this.metaModel = context.getEntityManagerFactory().getMetamodel();
		this.jpaEdmMappingModelAccess = ODataJPAFactory.createFactory()
				.getJPAAccessFactory().getJPAEdmMappingModelAccess(context);
		this.jpaEdmExtension = context.getJPAEdmExtension();
		jpaEdmMappingModelAccess.loadMappingModel();
	}

	public JPAEdmBaseViewImpl(Metamodel metaModel, String pUnitName) {
		this.metaModel = metaModel;
		this.pUnitName = pUnitName;
	}

	@Override
	public String getpUnitName() {
		return pUnitName;
	}

	@Override
	public Metamodel getJPAMetaModel() {
		return metaModel;
	}

	@Override
	public boolean isConsistent() {
		return isConsistent;
	}

	@Override
	public void clean() {
		pUnitName = null;
		metaModel = null;
		isConsistent = false;
	}

	@Override
	public JPAEdmMappingModelAccess getJPAEdmMappingModelAccess() {
		return jpaEdmMappingModelAccess;

	}

	@Override
	public JPAEdmExtension getJPAEdmExtension() {
		return this.jpaEdmExtension;
	}

}

package com.sap.core.odata.processor.core.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmBaseView;

public abstract class JPAEdmBaseViewImpl implements JPAEdmBaseView {
	
	protected String pUnitName =  null;
	protected Metamodel metaModel = null;
	protected boolean isConsistent = true;
	protected JPAEdmBuilder builder = null;
	
	public JPAEdmBaseViewImpl(JPAEdmBaseView view) {
		this.pUnitName = view.getpUnitName();
		this.metaModel = view.getJPAMetaModel();
	}
	
	public JPAEdmBaseViewImpl(ODataJPAContext context){
		this.pUnitName = context.getPersistenceUnitName();
		this.metaModel = context.getEntityManagerFactory().getMetamodel();
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
	public boolean isConsistent( ){
		return isConsistent;
	}
	
	@Override
	public void clean( ){
		pUnitName = null;
		metaModel = null;
		isConsistent = false;
	}

}

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

  public JPAEdmBaseViewImpl(final JPAEdmBaseView view) {
    pUnitName = view.getpUnitName();
    metaModel = view.getJPAMetaModel();
    jpaEdmMappingModelAccess = view.getJPAEdmMappingModelAccess();
    jpaEdmExtension = view.getJPAEdmExtension();
  }

  public JPAEdmBaseViewImpl(final ODataJPAContext context) {
    pUnitName = context.getPersistenceUnitName();
    metaModel = context.getEntityManagerFactory().getMetamodel();
    jpaEdmMappingModelAccess = ODataJPAFactory.createFactory()
        .getJPAAccessFactory().getJPAEdmMappingModelAccess(context);
    jpaEdmExtension = context.getJPAEdmExtension();
    jpaEdmMappingModelAccess.loadMappingModel();
  }

  public JPAEdmBaseViewImpl(final Metamodel metaModel, final String pUnitName) {
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
    return jpaEdmExtension;
  }

}

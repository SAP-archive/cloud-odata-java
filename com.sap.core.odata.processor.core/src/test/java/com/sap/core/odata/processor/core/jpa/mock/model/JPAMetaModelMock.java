package com.sap.core.odata.processor.core.jpa.mock.model;

import java.util.Set;

import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

public class JPAMetaModelMock implements Metamodel {

  @Override
  public <X> EmbeddableType<X> embeddable(final Class<X> arg0) {
    return null;
  }

  @Override
  public <X> EntityType<X> entity(final Class<X> arg0) {
    return null;
  }

  @Override
  public Set<EmbeddableType<?>> getEmbeddables() {
    return null;
  }

  @Override
  public Set<EntityType<?>> getEntities() {
    return null;
  }

  @Override
  public Set<ManagedType<?>> getManagedTypes() {
    return null;
  }

  @Override
  public <X> ManagedType<X> managedType(final Class<X> arg0) {
    return null;
  }

}

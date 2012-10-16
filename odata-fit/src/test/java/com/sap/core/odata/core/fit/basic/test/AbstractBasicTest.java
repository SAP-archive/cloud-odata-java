package com.sap.core.odata.core.fit.basic.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmServiceMetadata;
import com.sap.core.odata.core.producer.EntitySet;
import com.sap.core.odata.core.producer.Metadata;
import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.fit.AbstractFitTest;

public class AbstractBasicTest extends AbstractFitTest {
  
  @Override
  protected ODataProducer createProducer() {
    ODataProducer producer = mock(ODataProducer.class);

    EdmServiceMetadata edmsm = mock(EdmServiceMetadata.class);
    when(edmsm.getDataServiceVersion()).thenReturn("2.0");
    
    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(edmsm);
    
    Metadata metadata = mock(Metadata.class);
    when(metadata.getEdm()).thenReturn(edm);
    
    EntitySet entitySet = mock(EntitySet.class);
    
    when(producer.getMetadata()).thenReturn(metadata);
    when(producer.getEntitySet()).thenReturn(entitySet);
    
    return producer;
  }
}

package com.sap.core.odata.core.fit.basic.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.facet.EntitySet;
import com.sap.core.odata.api.processor.facet.Metadata;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.fit.AbstractFitTest;

public class AbstractBasicTest extends AbstractFitTest {
  
  @Override
  protected ODataSingleProcessor createProducer() throws ODataError {
    ODataSingleProcessor producer = mock(ODataSingleProcessor.class);

    EdmServiceMetadata edmsm = mock(EdmServiceMetadata.class);
    when(edmsm.getDataServiceVersion()).thenReturn("2.0");
    
    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(edmsm);
    
    Metadata metadata = mock(Metadata.class);
    when(metadata.getEdm()).thenReturn(edm);
    
    EntitySet entitySet = mock(EntitySet.class);
    
    when(producer.getMetadataProcessor()).thenReturn(metadata);
    when(producer.getEntitySetProcessor()).thenReturn(entitySet);
    
    return producer;
  }
}

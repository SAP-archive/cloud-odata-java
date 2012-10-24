package com.sap.core.odata.core.fit.basic.test;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response.Status;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.aspect.EntitySet;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.testutils.fit.AbstractFitTest;

public class AbstractBasicTest extends AbstractFitTest {

  @Override
  protected ODataProcessor createProcessor() throws ODataError {
    ODataProcessor processor = mock(ODataSingleProcessor.class, CALLS_REAL_METHODS);

    EdmServiceMetadata edmsm = mock(EdmServiceMetadata.class);
    when(edmsm.getDataServiceVersion()).thenReturn("2.0");

    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(edmsm);

    Metadata metadata = mock(Metadata.class);
    when(metadata.getEdm()).thenReturn(edm);
    when(metadata.readMetadata()).thenReturn(ODataResponse.status(Status.OK.getStatusCode()).entity("metadata").build());

    EntitySet entitySet = mock(EntitySet.class);

    when(processor.getMetadataProcessor()).thenReturn(metadata);
    when(processor.getEntitySetProcessor()).thenReturn(entitySet);

    return processor;
  }
}

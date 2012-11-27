package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmServiceMetadataImplProvTest {
 
  @Test
  public void dataServiceVersion() throws Exception{
    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);
    
    EdmServiceMetadata serviceMetadata = edmImplProv.getServiceMetadata();
    assertEquals("2.0", serviceMetadata.getDataServiceVersion());
  }
  
  @Ignore
  @Test
  public void metadata() throws Exception{
    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);
    
    EdmServiceMetadata serviceMetadata = edmImplProv.getServiceMetadata();
    assertEquals("2.0", serviceMetadata.getMetadata());
  }
}

package com.sap.core.odata.api.ep;

import java.io.InputStream;

public interface ODataEntityContent {
  
  String getETag();
  String getContentHeader();
 
  InputStream getContent();
  
}

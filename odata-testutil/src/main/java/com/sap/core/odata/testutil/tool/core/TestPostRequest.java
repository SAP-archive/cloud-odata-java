package com.sap.core.odata.testutil.tool.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TestPostRequest extends TestRequest {

  private String contentType;
  private String content;
  private String charset;

  TestPostRequest(String path, int callCount, String contentType, String content) {
    this(path, callCount, contentType, content, "UTF-8");
  }
  
  TestPostRequest(String path, int callCount, String contentType, String content, String charset) {
    super(path, callCount);
    
    this.contentType = contentType;
    this.content = content;
    this.charset = charset;
    this.requestHttpMethod = RequestHttpMethod.POST;
  }

  public String getContentType() {
    return contentType;
  }
  
  public InputStream getContentAsStream() {
    try {
      return new ByteArrayInputStream(content.getBytes(charset));
    } catch (UnsupportedEncodingException e) {
      // we know that UTF-8 is supported
      throw new RuntimeException("UTF-8 MUST be supported.", e);
    }
  }

  public String getContent() {
    return content;
  }
}

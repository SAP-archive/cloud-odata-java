package com.sap.core.odata.testutil.tool.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.sap.core.odata.testutil.TestUtilRuntimeException;

public class TestPostRequest extends TestRequest {

  private String contentType;
  private String content;
  private String charset;

  TestPostRequest(final String path, final int callCount, final String contentType, final String content) {
    this(path, callCount, contentType, content, "UTF-8");
  }

  TestPostRequest(final String path, final int callCount, final String contentType, final String content, final String charset) {
    super(path, callCount);

    this.contentType = contentType;
    this.content = content;
    this.charset = charset;
    requestHttpMethod = RequestHttpMethod.POST;
  }

  public String getContentType() {
    return contentType;
  }

  public InputStream getContentAsStream() {
    try {
      return new ByteArrayInputStream(content.getBytes(charset));
    } catch (UnsupportedEncodingException e) {
      // we know that UTF-8 is supported
      throw new TestUtilRuntimeException("UTF-8 MUST be supported.", e);
    }
  }

  public String getContent() {
    return content;
  }
}

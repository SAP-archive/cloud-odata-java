/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.testutil.tool.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.sap.core.odata.testutil.TestUtilRuntimeException;

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
      throw new TestUtilRuntimeException("UTF-8 MUST be supported.", e);
    }
  }

  public String getContent() {
    return content;
  }
}

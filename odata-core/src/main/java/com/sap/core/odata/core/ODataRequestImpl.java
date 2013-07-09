package com.sap.core.odata.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.core.commons.ContentType;

/**
 * @author SAP AG
 */
public class ODataRequestImpl extends ODataRequest {

  private ODataHttpMethod method;
  private Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
  private InputStream body;
  private PathInfo pathInfo;
  private Map<String, String> queryParameters;
  private List<String> acceptHeaders;
  private ContentType contentType;
  private List<Locale> acceptableLanguages;

  @Override
  public Map<String, String> getQueryParameters() {
    return queryParameters;
  }

  @Override
  public List<String> getAcceptHeaders() {
    return acceptHeaders;
  }

  @Override
  public String getContentType() {
    return contentType == null ? null : contentType.toContentTypeString();
  }

  @Override
  public List<Locale> getAcceptableLanguages() {
    return acceptableLanguages;
  }

  @Override
  public String getRequestHeaderValue(final String name) {
    final List<String> headerList = requestHeaders.get(name);
    return headerList == null || headerList.isEmpty() ? null : headerList.get(0);
  }

  @Override
  public Map<String, List<String>> getRequestHeaders() {
    return Collections.unmodifiableMap(requestHeaders);
  }

  @Override
  public InputStream getBody() {
    return body;
  }

  @Override
  public ODataHttpMethod getMethod() {
    return method;
  }

  @Override
  public PathInfo getPathInfo() {
    return pathInfo;
  }

  public class ODataRequestBuilderImpl extends ODataRequestBuilder {
    private ODataHttpMethod method;
    private Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
    private InputStream body;
    private PathInfo pathInfo;
    private Map<String, String> queryParameters;
    private List<String> acceptHeaders;
    private ContentType contentType;
    private List<Locale> acceptableLanguages;

    @Override
    public ODataRequest build() {
      ODataRequestImpl.this.method = method;
      ODataRequestImpl.this.requestHeaders = requestHeaders;
      ODataRequestImpl.this.body = body;
      ODataRequestImpl.this.pathInfo = pathInfo;
      ODataRequestImpl.this.queryParameters = queryParameters;
      ODataRequestImpl.this.acceptHeaders = acceptHeaders;
      ODataRequestImpl.this.contentType = contentType;
      ODataRequestImpl.this.acceptableLanguages = acceptableLanguages;
      return ODataRequestImpl.this;
    }

    @Override
    public ODataRequestBuilder requestHeaders(final Map<String, List<String>> headers) {
      requestHeaders = headers;
      return this;
    }

    @Override
    public ODataRequestBuilder body(final InputStream body) {
      this.body = body;
      return this;
    }

    @Override
    public ODataRequestBuilder pathInfo(final PathInfo pathInfo) {
      this.pathInfo = pathInfo;
      return this;
    }

    @Override
    public ODataRequestBuilder method(final ODataHttpMethod method) {
      this.method = method;
      return this;
    }

    @Override
    public ODataRequestBuilder acceptableLanguages(final List<Locale> acceptableLanguages) {
      this.acceptableLanguages = acceptableLanguages;
      return this;
    }

    @Override
    public ODataRequestBuilder acceptHeaders(final List<String> acceptHeaders) {
      this.acceptHeaders = acceptHeaders;
      return this;
    }

    @Override
    public ODataRequestBuilder queryParameters(final Map<String, String> queryParameters) {
      this.queryParameters = queryParameters;
      return this;
    }

    @Override
    public ODataRequestBuilder contentType(final String contentType) {
      this.contentType = ContentType.create(contentType);
      return this;
    }

    @Override
    public ODataRequestBuilder fromRequest(final ODataRequest request) {
      pathInfo = request.getPathInfo();
      method = request.getMethod();
      body = request.getBody();
      if (request.getContentType() != null) {
        contentType = ContentType.create(request.getContentType());
      }
      requestHeaders = request.getRequestHeaders();

      if (request.getAcceptHeaders() != null) {
        acceptHeaders = new ArrayList<String>();
        for (String acceptHeader : request.getAcceptHeaders()) {
          acceptHeaders.add(acceptHeader);
        }
      }
      if (request.getAcceptableLanguages() != null) {
        acceptableLanguages = new ArrayList<Locale>();
        for (Locale acceptLanguage : request.getAcceptableLanguages()) {
          acceptableLanguages.add(acceptLanguage);
        }
      }
      if (request.getQueryParameters() != null) {
        queryParameters = new HashMap<String, String>();
        for (Map.Entry<String, String> queryParameter : request.getQueryParameters().entrySet()) {
          String queryParameterName = queryParameter.getKey();
          queryParameters.put(queryParameterName, queryParameter.getValue());
        }
      }
      return this;
    }

  }
}

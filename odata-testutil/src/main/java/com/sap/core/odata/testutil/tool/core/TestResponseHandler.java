package com.sap.core.odata.testutil.tool.core;

import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public interface TestResponseHandler {

  public abstract void handle(URI baseUri, TestRequest testPath, HttpRequest request, HttpResponse response);

  public abstract void handle(TestRequest testPath, HttpRequest request, Exception e);

  public abstract String getResult();

  public abstract String getErrors();

}
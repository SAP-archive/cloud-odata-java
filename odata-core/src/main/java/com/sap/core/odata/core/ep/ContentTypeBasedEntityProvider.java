package com.sap.core.odata.core.ep;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * Interface for all none basic (content type <b>dependent</b>) provider methods.
 * 
 * @author SAP AG
 */
public interface ContentTypeBasedEntityProvider {

  ODataFeed readFeed(EdmEntitySet entitySet, InputStream content, EntityProviderReadProperties properties) throws EntityProviderException;

  ODataEntry readEntry(EdmEntitySet entitySet, InputStream content, EntityProviderReadProperties properties) throws EntityProviderException;

  Map<String, Object> readProperty(EdmProperty edmProperty, InputStream content, EntityProviderReadProperties properties) throws EntityProviderException;

  String readLink(EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

  List<String> readLinks(EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

  ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException;

  ODataResponse writeFeed(EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderWriteProperties properties) throws EntityProviderException;

  ODataResponse writeEntry(EdmEntitySet entitySet, Map<String, Object> data, EntityProviderWriteProperties properties) throws EntityProviderException;

  ODataResponse writeProperty(EdmProperty edmProperty, Object value) throws EntityProviderException;

  ODataResponse writeLink(EdmEntitySet entitySet, Map<String, Object> data, EntityProviderWriteProperties properties) throws EntityProviderException;

  ODataResponse writeLinks(EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderWriteProperties properties) throws EntityProviderException;

  ODataResponse writeFunctionImport(EdmFunctionImport functionImport, Object data, EntityProviderWriteProperties properties) throws EntityProviderException;

  ODataResponse writeErrorDocument(HttpStatusCodes status, String errorCode, String message, Locale locale, String innerError) throws EntityProviderException;
}

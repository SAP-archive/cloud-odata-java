package com.sap.core.odata.api.service;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.aspect.Batch;
import com.sap.core.odata.api.processor.aspect.Entity;
import com.sap.core.odata.api.processor.aspect.EntityComplexProperty;
import com.sap.core.odata.api.processor.aspect.EntityLink;
import com.sap.core.odata.api.processor.aspect.EntityLinks;
import com.sap.core.odata.api.processor.aspect.EntityMedia;
import com.sap.core.odata.api.processor.aspect.EntitySet;
import com.sap.core.odata.api.processor.aspect.EntitySimpleProperty;
import com.sap.core.odata.api.processor.aspect.EntitySimplePropertyValue;
import com.sap.core.odata.api.processor.aspect.FunctionImport;
import com.sap.core.odata.api.processor.aspect.FunctionImportValue;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;

public interface ODataService {

  String getODataVersion() throws ODataException;

  Edm getEntityDataModel() throws ODataException;

  Metadata getMetadataProcessor() throws ODataException;

  ServiceDocument getServiceDocumentProcessor() throws ODataException;

  Entity getEntityProcessor() throws ODataException;

  EntitySet getEntitySetProcessor() throws ODataException;

  EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataException;

  EntityLink getEntityLinkProcessor() throws ODataException;

  EntityLinks getEntityLinksProcessor() throws ODataException;

  EntityMedia getEntityMediaProcessor() throws ODataException;

  EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataException;

  EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataException;

  FunctionImport getFunctionImportProcessor() throws ODataException;

  FunctionImportValue getFunctionImportValueProcessor() throws ODataException;

  Batch getBatchProcessor() throws ODataException;

}

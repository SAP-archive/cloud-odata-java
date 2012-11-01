package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.exception.ODataRuntimeException;

public interface EdmProvider {

  EntityType getEntityType(FullQualifiedName edmFQName) throws ODataRuntimeException, ODataMessageException;

  ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataRuntimeException, ODataMessageException;

  Association getAssociation(FullQualifiedName edmFQName) throws ODataRuntimeException, ODataMessageException;

  EntitySet getEntitySet(String entityContainer, String name) throws ODataRuntimeException, ODataMessageException;

  AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataRuntimeException, ODataMessageException;

  FunctionImport getFunctionImport(String entityContainer, String name) throws ODataRuntimeException, ODataMessageException;

//TODO required for $metadata
//  List<EdmNamespaceInfo> getNamespaceInfos() throws ODataRuntimeException, ODataMessageException;

//TODO required for $metadata
//  List<Schema> getSchemas() throws ODataRuntimeException, ODataMessageException;
}
package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataMessageException;

public interface EdmProvider {

  EntityContainer getEntityContainer(String name) throws ODataMessageException;

  EntityType getEntityType(FullQualifiedName edmFQName) throws ODataMessageException;

  ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataMessageException;

  Association getAssociation(FullQualifiedName edmFQName) throws ODataMessageException;

  EntitySet getEntitySet(String entityContainer, String name) throws ODataMessageException;

  AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataMessageException;

  FunctionImport getFunctionImport(String entityContainer, String name) throws ODataMessageException;

  Collection<Schema> getSchemas() throws ODataMessageException;

  //TODO required for validation if namspace is defined????
  //List<EdmNamespaceInfo> getNamespaceInfos() throws ODataMessageException;
}
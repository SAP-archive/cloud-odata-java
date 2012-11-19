package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataException;

public interface EdmProvider {

  EntityContainer getEntityContainer(String name) throws ODataException;

  EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException;

  ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException;

  Association getAssociation(FullQualifiedName edmFQName) throws ODataException;

  EntitySet getEntitySet(String entityContainer, String name) throws ODataException;

  AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException;

  FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException;

  Collection<Schema> getSchemas() throws ODataException;

  //TODO required for validation if namspace is defined????
  //List<EdmNamespaceInfo> getNamespaceInfos() throws ODataMessageException;
}
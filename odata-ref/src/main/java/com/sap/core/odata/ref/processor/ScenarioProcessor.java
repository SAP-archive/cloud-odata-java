package com.sap.core.odata.ref.processor;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.facet.EntitySet;
import com.sap.core.odata.api.processor.facet.Metadata;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmServiceMetadata;

public class ScenarioProcessor extends ODataSingleProcessor implements EntitySet, Metadata {

  private static final Logger log = LoggerFactory.getLogger(ScenarioProcessor.class);

  private String segment1;
  private String segment2;

  public String getSegment1() {
    return segment1;
  }

  public String getSegment2() {
    return segment2;
  }

  public void injectServiceResolutionPath(String segment1, String segment2) {
    this.segment1 = segment1;
    this.segment2 = segment2;
    ScenarioProcessor.log.debug("service resolution segment1: " + this.segment1);
    ScenarioProcessor.log.debug("service resolution segment2: " + this.segment2);
  }


  @Override
  public Edm getEdm() {
    return new EdmImpl();
  }

  @Override
  public ODataResponse readMetadata() {
    return ODataResponse.status(Status.OK.getStatusCode()).entity("$metadata").build();
  }

  private class EdmImpl implements Edm {

    @Override
    public EdmEntityContainer getEntityContainer(String name) {
      return null;
    }

    @Override
    public EdmEntityType getEntityType(String namespace, String name) {
      return null;
    }

    @Override
    public EdmComplexType getComplexType(String namespace, String name) {
      return null;
    }

    @Override
    public EdmAssociation getAssociation(String namespace, String name) {
      return null;
    }

    @Override
    public EdmServiceMetadata getServiceMetadata() {
      return new EdmServiceMetadataImpl();
    }

    @Override
    public EdmEntityContainer getDefaultEntityContainer() {
      return null;
    }

  }

  private class EdmServiceMetadataImpl implements EdmServiceMetadata {

    @Override
    public byte[] getMetadata() {
      return null;
    }

    @Override
    public String getDataServiceVersion() {
      return "2.0";
    }

  }

  @Override
  public ODataResponse readEntitySet() {
    return null;
  }

  @Override
  public ODataResponse countEntitySet() {
    return null;
  }

  @Override
  public ODataResponse createEntitySet() {
    return null;
  }

}

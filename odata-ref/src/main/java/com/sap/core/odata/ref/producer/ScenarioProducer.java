package com.sap.core.odata.ref.producer;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmAssociation;
import com.sap.core.odata.core.edm.EdmComplexType;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntityType;
import com.sap.core.odata.core.edm.EdmServiceMetadata;
import com.sap.core.odata.core.producer.Metadata;
import com.sap.core.odata.core.producer.ODataProducer;

public class ScenarioProducer extends ODataProducer implements Metadata {

  private static final Logger log = LoggerFactory.getLogger(ScenarioProducer.class);

  private String segment1;

  public String getSegment1() {
    return segment1;
  }

  public String getSegment2() {
    return segment2;
  }

  private String segment2;

  public void injectServiceResolutionPath(String segment1, String segment2) {
    this.segment1 = segment1;
    this.segment2 = segment2;
    ScenarioProducer.log.debug("service resolution segment1: " + this.segment1);
    ScenarioProducer.log.debug("service resolution segment2: " + this.segment2);
  }

  @Override
  public Edm getEdm() {
    return new EdmImpl();
  }

  @Override
  public Response read() {
    return null;
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
      return new EdmServiceMetadataImp();
    }

    @Override
    public EdmEntityContainer getDefaultEntityContainer() {
      return null;
    }
    
  }
  
  private class EdmServiceMetadataImp implements EdmServiceMetadata {

    @Override
    public byte[] getMetadata() {
      return null;
    }

    @Override
    public String getDataServiceVersion() {
      return "2.0";
    }
    
  }

}

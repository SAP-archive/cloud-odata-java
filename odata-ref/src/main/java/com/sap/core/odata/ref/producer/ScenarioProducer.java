package com.sap.core.odata.ref.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.core.producer.ODataProducer;

public class ScenarioProducer implements ODataProducer {

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

}

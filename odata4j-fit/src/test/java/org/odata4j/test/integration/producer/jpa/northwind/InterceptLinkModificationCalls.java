package org.odata4j.test.integration.producer.jpa.northwind;

import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.ODataProducerDelegate;

public class InterceptLinkModificationCalls extends ODataProducerDelegate {

  public LinksProducerCall lastCall;

  enum LinksMethod {
    CREATE, UPDATE, DELETE
  }

  public static class LinksProducerCall {

    public final LinksMethod methodCalled;
    public final OEntityId sourceEntity;
    public final String targetNavProp;
    public final OEntityKey oldTargetEntityKey;

    public final OEntityId newTargetEntity;

    public LinksProducerCall(LinksMethod methodCalled, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
      this.methodCalled = methodCalled;
      this.sourceEntity = sourceEntity;
      this.targetNavProp = targetNavProp;
      this.oldTargetEntityKey = oldTargetEntityKey;
      this.newTargetEntity = newTargetEntity;
    }
  }

  private final ODataProducer jpa;

  public InterceptLinkModificationCalls(ODataProducer jpa) {
    this.jpa = jpa;
  }

  public ODataProducer getDelegate() {
    return jpa;
  }

  @Override
  public void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
    lastCall = new LinksProducerCall(LinksMethod.UPDATE, sourceEntity, targetNavProp, oldTargetEntityKey, newTargetEntity);
  }

  @Override
  public void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
    lastCall = new LinksProducerCall(LinksMethod.CREATE, sourceEntity, targetNavProp, null, targetEntity);
  }

  @Override
  public void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
    lastCall = new LinksProducerCall(LinksMethod.DELETE, sourceEntity, targetNavProp, targetEntityKey, null);
  }
}

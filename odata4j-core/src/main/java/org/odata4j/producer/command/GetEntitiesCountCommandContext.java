package org.odata4j.producer.command;

import org.odata4j.producer.CountResponse;
import org.odata4j.producer.QueryInfo;

public interface GetEntitiesCountCommandContext extends ProducerCommandContext<CountResponse> {

  String getEntitySetName();

  QueryInfo getQueryInfo();

}

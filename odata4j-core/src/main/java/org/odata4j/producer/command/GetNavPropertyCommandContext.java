package org.odata4j.producer.command;

import org.odata4j.core.OEntityKey;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.QueryInfo;

public interface GetNavPropertyCommandContext extends ProducerCommandContext<BaseResponse> {

  String getEntitySetName();

  OEntityKey getEntityKey();

  String getNavProp();

  QueryInfo getQueryInfo();

}

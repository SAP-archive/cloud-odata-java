package org.odata4j.producer.command;

import java.util.Map;

import org.odata4j.core.OFunctionParameter;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.QueryInfo;

public interface CallFunctionCommandContext extends ProducerCommandContext<BaseResponse> {

  EdmFunctionImport getName();

  Map<String, OFunctionParameter> getParams();

  QueryInfo getQueryInfo();

}

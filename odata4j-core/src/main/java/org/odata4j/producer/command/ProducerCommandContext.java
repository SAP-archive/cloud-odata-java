package org.odata4j.producer.command;

import org.odata4j.command.CommandContext;

public interface ProducerCommandContext<TResult> extends CommandContext {

  TResult getResult();

  void setResult(TResult result);

}

package org.odata4j.command;

public interface FilterCommand<TContext extends CommandContext> extends Command<TContext> {

  FilterResult postProcess(TContext context, Exception e);

}

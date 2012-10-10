package org.odata4j.command;

public interface CommandExecution {

  <TContext extends CommandContext, TCommand extends Command<TContext>>
      void execute(TCommand command, TContext context) throws Exception;

  public final CommandExecution DEFAULT = new CommandExecution() {

    @Override
    public <TContext extends CommandContext, TCommand extends Command<TContext>> void execute(TCommand command, TContext context) throws Exception {
      if (command instanceof FilterCommand) {
        try {
          command.execute(context);
        } catch (Exception e) {
          FilterCommand<TContext> filterCommand = (FilterCommand<TContext>) command;
          FilterResult postProcessResult = filterCommand.postProcess(context, e);
          if (postProcessResult != FilterResult.HANDLED) {
            throw e;
          }
        }
      } else {
        command.execute(context);
      }
    }
  };

}

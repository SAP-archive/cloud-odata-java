package org.odata4j.command;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.core.UmbrellaException;

public class ChainCommand<TContext extends CommandContext> implements Command<TContext> {

  public static class Builder<TContext extends CommandContext> {
    private final List<Command<TContext>> commands = new ArrayList<Command<TContext>>();

    @SuppressWarnings("unchecked")
    public Builder<TContext> add(Command<?> command) {
      if (command != null)
        commands.add((Command<TContext>) command);
      return this;
    }

    public Builder<TContext> addAll(Iterable<Command<?>> commands) {
      if (commands != null)
        for (Command<?> command : commands)
          add(command);
      return this;
    }

    public ChainCommand<TContext> build() {
      return new ChainCommand<TContext>(commands);
    }

  }

  public static <TContext extends CommandContext> Builder<TContext> newBuilder() {
    return new Builder<TContext>();
  }

  private final List<Command<TContext>> commands;

  public ChainCommand(List<Command<TContext>> commands) {
    this.commands = commands;
  }

  @Override
  public CommandResult execute(TContext context) throws Exception {
    CommandResult commandResult = null;
    Exception executionException = null;
    int i = 0;
    int n = commands.size();
    for (i = 0; i < n; i++) {
      try {
        commandResult = commands.get(i).execute(context);
        if (commandResult == CommandResult.COMPLETE) {
          break;
        }
      } catch (Exception e) {
        executionException = e;
      }
    }

    // Call postprocess methods on Filters in reverse order
    if (i >= n) { // Fell off the end of the chain
      i--;
    }
    boolean executionExceptionHandled = false;
    List<Exception> postProcessExceptions = null;
    for (int j = i; j >= 0; j--) {
      if (commands.get(j) instanceof FilterCommand) {
        FilterCommand<TContext> filterCommand = (FilterCommand<TContext>) commands.get(j);
        try {
          FilterResult postProcessResult = filterCommand.postProcess(context, executionException);
          if (postProcessResult == FilterResult.HANDLED) {
            executionExceptionHandled = true;
          }
        } catch (Exception e) {
          // keep going to honor the postProcess contract of the others
          // but capture each exception and rethrow afterwards
          if (postProcessExceptions == null)
            postProcessExceptions = new ArrayList<Exception>();
          postProcessExceptions.add(e);
        }
      }
    }

    if (postProcessExceptions != null)
      throw new UmbrellaException(postProcessExceptions);

    if (executionException != null && !executionExceptionHandled)
      throw executionException;

    return commandResult;
  }

}

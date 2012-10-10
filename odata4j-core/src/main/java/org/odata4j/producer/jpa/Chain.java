package org.odata4j.producer.jpa;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.odata4j.exceptions.NotFoundException;

public class Chain implements Command {

  List<Command> commands;

  public Chain(List<Command> commands) {
    this.commands = Collections.unmodifiableList(commands);
  }

  /**
   * copied from http://commons.apache.org/chain. 
   */
  @Override
  public boolean execute(JPAContext context) {
    boolean saveResult = false;
    RuntimeException saveException = null;
    int i = 0;
    int n = commands.size();
    for (i = 0; i < n; i++) {
      try {
        saveResult = commands.get(i).execute(context);
        if (saveResult) {
          break;
        }
      } catch (RuntimeException e) {
        saveException = e;
        break;
      }
    }

    // Call postprocess methods on Filters in reverse order
    if (i >= n) { // Fell off the end of the chain
      i--;
    }
    boolean handled = false;
    boolean result = false;
    for (int j = i; j >= 0; j--) {
      if (commands.get(j) instanceof Filter) {
        try {
          result =
              ((Filter) commands.get(j)).postProcess(context,
                  saveException);
          if (result) {
            handled = true;
          }
        } catch (Exception e) {
          // Silently ignore
        }
      }
    }

    // Return the exception or result state from the last execute()
    if ((saveException != null) && !handled)
      if (saveException instanceof EntityNotFoundException)
        throw new NotFoundException(saveException);
      else
        throw saveException;
    else
      return saveResult;
  }
}
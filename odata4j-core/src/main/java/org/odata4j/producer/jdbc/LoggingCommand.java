package org.odata4j.producer.jdbc;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.odata4j.command.Command;
import org.odata4j.command.CommandResult;
import org.odata4j.core.Throwables;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.PrintExpressionVisitor;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.command.CloseCommandContext;
import org.odata4j.producer.command.CreateEntityCommandContext;
import org.odata4j.producer.command.DeleteEntityCommandContext;
import org.odata4j.producer.command.GetEntitiesCommandContext;
import org.odata4j.producer.command.GetEntityCommandContext;
import org.odata4j.producer.command.GetMetadataCommandContext;
import org.odata4j.producer.command.ProducerCommandContext;

public class LoggingCommand implements Command<ProducerCommandContext<?>> {
  private static final Logger log = Logger.getLogger(LoggingCommand.class.getName());

  @Override
  public CommandResult execute(ProducerCommandContext<?> context) throws Exception {
    if (context instanceof CloseCommandContext) {
      log("close");
    } else if (context instanceof GetMetadataCommandContext) {
      log("getMetadata");
    } else if (context instanceof GetEntitiesCommandContext) {
      GetEntitiesCommandContext c = (GetEntitiesCommandContext) context;
      log("getEntities",
          "entitySetName", c.getEntitySetName(),
          "queryInfo", c.getQueryInfo());
    } else if (context instanceof GetEntityCommandContext) {
      GetEntityCommandContext c = (GetEntityCommandContext) context;
      log("getEntity",
          "entitySetName", c.getEntitySetName(),
          "entityKey", c.getEntityKey(),
          "queryInfo", c.getQueryInfo());
    } else if (context instanceof CreateEntityCommandContext) {
      CreateEntityCommandContext c = (CreateEntityCommandContext) context;
      log("createEntity",
          "entitySetName", c.getEntitySetName(),
          "entity", c.getEntity());
    } else if (context instanceof DeleteEntityCommandContext) {
      DeleteEntityCommandContext c = (DeleteEntityCommandContext) context;
      log("deleteEntity",
          "entitySetName", c.getEntitySetName(),
          "entityKey", c.getEntityKey());
    } else {
      throw new UnsupportedOperationException("TODO implement logging for : " + context);
    }
    return CommandResult.CONTINUE;
  }

  private static Object format(Object arg) {
    if (arg instanceof QueryInfo) {
      arg = reflectionFormat(arg);
    }
    if (arg instanceof CommonExpression) {
      arg = PrintExpressionVisitor.asString((CommonExpression) arg);
    }
    return arg;
  }

  private static String reflectionFormat(Object arg) {
    StringBuilder sb = new StringBuilder();
    try {
      for (Field f : arg.getClass().getFields()) {
        Object val = f.get(arg);
        if (val == null
            || Collection.class.isAssignableFrom(val.getClass()) && ((Collection<?>) val).isEmpty()
            || Map.class.isAssignableFrom(val.getClass()) && ((Map<?, ?>) val).isEmpty())
          continue;

        if (sb.length() > 0)
          sb.append(',');
        sb.append(f.getName());
        sb.append(':');
        sb.append(format(val));
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
    return "{" + sb.toString() + "}";
  }

  private void log(String methodName, Object... args) {
    StringBuilder sb = new StringBuilder(methodName);
    for (int i = 0; i < args.length; i += 2) {
      sb.append(' ');
      sb.append(args[i]);
      sb.append('=');
      sb.append(format(args[i + 1]));
    }
    log.info(sb.toString());
  }

}
package org.odata4j.producer.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odata4j.command.Command;
import org.odata4j.command.CommandContext;
import org.odata4j.command.CommandExecution;
import org.odata4j.core.Throwables;
import org.odata4j.producer.command.CommandProducer;
import org.odata4j.producer.command.ProducerCommandContext;

public class JdbcProducer extends CommandProducer {

  public static class Builder {

    private final Map<Class<?>, Object> instances = new HashMap<Class<?>, Object>();
    private final Map<Class<?>, List<Command<?>>> preCommands = new HashMap<Class<?>, List<Command<?>>>();
    private final Map<Class<?>, List<Command<?>>> postCommands = new HashMap<Class<?>, List<Command<?>>>();

    private Jdbc jdbc;

    public Builder jdbc(Jdbc jdbc) {
      this.jdbc = jdbc;
      return this;
    }

    public <TContext extends ProducerCommandContext<?>> Builder insert(Class<TContext> contextType, Command<?> command) {
      return preOrPost(contextType, command, preCommands);
    }

    public <TContext extends ProducerCommandContext<?>> Builder append(Class<TContext> contextType, Command<?> command) {
      return preOrPost(contextType, command, postCommands);
    }

    private <TContext extends ProducerCommandContext<?>> Builder preOrPost(Class<TContext> contextType, Command<?> command,
        Map<Class<?>, List<Command<?>>> map) {
      if (!map.containsKey(contextType))
        map.put(contextType, new ArrayList<Command<?>>());
      map.get(contextType).add(command);
      return this;
    }

    public JdbcProducer build() {
      if (jdbc == null)
        throw new IllegalArgumentException("Jdbc is mandatory");

      JdbcProducerBackend jdbcBackend = new JdbcProducerBackend() {

        @Override
        public CommandExecution getCommandExecution() {
          return CommandExecution.DEFAULT;
        }

        @Override
        public Jdbc getJdbc() {
          return jdbc;
        }

        @Override
        protected <TContext extends CommandContext> List<Command<?>> getPreCommands(Class<TContext> contextType) {
          return preCommands.get(contextType);
        }

        @Override
        protected <TContext extends CommandContext> List<Command<?>> getPostCommands(Class<TContext> contextType) {
          return postCommands.get(contextType);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <T> T get(Class<T> instanceType) {
          Object rt = instances.get(instanceType);
          if (rt == null) {
            try {
              rt = instanceType.newInstance();
            } catch (Exception e) {
              throw Throwables.propagate(e);
            }
          }
          return (T) rt;
        }

      };
      return new JdbcProducer(jdbcBackend);
    }

    public <T> Builder register(Class<T> instanceType, T instance) {
      instances.put(instanceType, instance);
      return this;
    }

  }

  private final JdbcProducerBackend jdbcBackend;

  protected JdbcProducer(JdbcProducerBackend jdbcBackend) {
    super(jdbcBackend);
    this.jdbcBackend = jdbcBackend;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Jdbc getJdbc() {
    return jdbcBackend.getJdbc();
  }

}

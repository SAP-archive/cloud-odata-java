package org.odata4j.producer.jdbc;

import org.odata4j.command.Command;
import org.odata4j.command.CommandResult;
import org.odata4j.producer.command.GetMetadataCommandContext;

public class JdbcGetMetadataCommand implements Command<GetMetadataCommandContext> {

  @Override
  public CommandResult execute(GetMetadataCommandContext context) throws Exception {
    JdbcProducerCommandContext jdbcContext = (JdbcProducerCommandContext) context;

    // 1. generate jdbc model
    JdbcModel model = generateModel(jdbcContext);

    // 2. apply model cleanup
    cleanupModel(model);

    // 3. project jdbc model into edm metadata
    JdbcMetadataMapping mapping = modelToMapping(jdbcContext, model);

    context.setResult(mapping);
    return CommandResult.CONTINUE;
  }

  public JdbcModel generateModel(JdbcProducerCommandContext jdbcContext) {
    return jdbcContext.getJdbc().execute(new GenerateJdbcModel());
  }

  public void cleanupModel(JdbcModel model) {
    new LimitJdbcModelToDefaultSchema().apply(model);
  }

  public JdbcMetadataMapping modelToMapping(JdbcProducerCommandContext jdbcContext, JdbcModel model) {
    return jdbcContext.get(JdbcModelToMetadata.class).apply(model);
  }

}
package org.odata4j.producer.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.core4j.ThrowingFunc1;
import org.odata4j.command.Command;
import org.odata4j.command.CommandResult;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.Responses;
import org.odata4j.producer.command.CreateEntityCommandContext;
import org.odata4j.producer.command.GetEntityCommandContext;

public class JdbcCreateEntityCommand implements Command<CreateEntityCommandContext> {

  @Override
  public CommandResult execute(CreateEntityCommandContext context) throws Exception {
    JdbcProducerCommandContext jdbcContext = (JdbcProducerCommandContext) context;

    String entitySetName = context.getEntitySetName();

    final JdbcMetadataMapping mapping = jdbcContext.getBackend().getMetadataMapping();
    final EdmEntitySet entitySet = mapping.getMetadata().findEdmEntitySet(entitySetName);
    if (entitySet == null)
      throw new NotFoundException();

    GenerateSqlInsert insertGen = jdbcContext.get(GenerateSqlInsert.class);
    final SqlStatement sqlStatement = insertGen.generate(mapping, entitySet, context.getEntity());
    jdbcContext.getJdbc().execute(new ThrowingFunc1<Connection, Void>() {
      @Override
      public Void apply(Connection conn) throws Exception {
        PreparedStatement stmt = sqlStatement.asPreparedStatement(conn);
        int updated = stmt.executeUpdate();
        if (updated == 0)
          throw new BadRequestException("Entity not inserted");
        return null;
      }
    });

    // now re-query for inserted entity
    OEntityKey entityKey = OEntityKey.infer(entitySet, context.getEntity().getProperties());
    GetEntityCommandContext getEntityCommandContext = jdbcContext.getBackend().newGetEntityCommandContext(entitySetName, entityKey, null);
    jdbcContext.getBackend().getCommand(GetEntityCommandContext.class).execute(getEntityCommandContext);
    OEntity newEntity = getEntityCommandContext.getResult().getEntity();

    EntityResponse response = Responses.entity(newEntity);
    context.setResult(response);
    return CommandResult.CONTINUE;
  }

}

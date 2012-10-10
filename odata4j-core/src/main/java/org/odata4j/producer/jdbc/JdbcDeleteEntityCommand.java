package org.odata4j.producer.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.core4j.ThrowingFunc1;
import org.odata4j.command.Command;
import org.odata4j.command.CommandResult;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.producer.command.DeleteEntityCommandContext;

public class JdbcDeleteEntityCommand extends JdbcBaseCommand implements Command<DeleteEntityCommandContext> {

  @Override
  public CommandResult execute(DeleteEntityCommandContext context) throws Exception {
    JdbcProducerCommandContext jdbcContext = (JdbcProducerCommandContext) context;

    String entitySetName = context.getEntitySetName();

    final JdbcMetadataMapping mapping = jdbcContext.getBackend().getMetadataMapping();
    final EdmEntitySet entitySet = mapping.getMetadata().findEdmEntitySet(entitySetName);
    if (entitySet == null)
      throw new NotFoundException();

    GenerateSqlDelete deleteGen = jdbcContext.get(GenerateSqlDelete.class);
    BoolCommonExpression filter = prependPrimaryKeyFilter(mapping, entitySet.getType(), context.getEntityKey(), null);
    final SqlStatement sqlStatement = deleteGen.generate(mapping, entitySet, filter);
    jdbcContext.getJdbc().execute(new ThrowingFunc1<Connection, Void>() {
      @Override
      public Void apply(Connection conn) throws Exception {
        PreparedStatement stmt = sqlStatement.asPreparedStatement(conn);
        int updated = stmt.executeUpdate();
        if (updated == 0)
          throw new BadRequestException("Entity not deleted");
        if (updated > 1)
          throw new BadRequestException(updated + " entities deleted");
        return null;
      }
    });

    return CommandResult.CONTINUE;
  }

}

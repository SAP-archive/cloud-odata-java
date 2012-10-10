package org.odata4j.producer.jdbc;

import org.odata4j.core.Action1;
import org.odata4j.producer.jdbc.JdbcModel.JdbcPrimaryKey;
import org.odata4j.producer.jdbc.JdbcModel.JdbcTable;

public class HsqlAddSystemTables implements Action1<JdbcModel> {

  @Override
  public void apply(JdbcModel info) {
    addPrimaryKey(info, "INFORMATION_SCHEMA", "SYSTEM_TABLES", "TABLE_SCHEM", "TABLE_NAME");
  }

  private static void addPrimaryKey(JdbcModel model, String schemaName, String tableName, String... keyColumns) {
    JdbcTable table = model.findTable(schemaName, tableName);
    if (table == null)
      return;

    for (int i = 0; i < keyColumns.length; i++) {
      JdbcPrimaryKey key = new JdbcPrimaryKey();
      key.columnName = keyColumns[i];
      key.sequenceNumber = i + 1;
      table.primaryKeys.add(key);
    }
  }

}

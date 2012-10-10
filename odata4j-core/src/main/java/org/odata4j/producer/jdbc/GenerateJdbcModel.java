package org.odata4j.producer.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.core4j.ThrowingFunc1;
import org.odata4j.producer.jdbc.JdbcModel.JdbcColumn;
import org.odata4j.producer.jdbc.JdbcModel.JdbcPrimaryKey;
import org.odata4j.producer.jdbc.JdbcModel.JdbcSchema;
import org.odata4j.producer.jdbc.JdbcModel.JdbcTable;

public class GenerateJdbcModel implements ThrowingFunc1<Connection, JdbcModel> {

  @Override
  public JdbcModel apply(Connection conn) throws Exception {
    JdbcModel model = new JdbcModel();
    DatabaseMetaData meta = conn.getMetaData();

    // schemas
    ResultSet schemas = meta.getSchemas();
    while (schemas.next()) {
      String schemaName = schemas.getString("TABLE_SCHEM");
      JdbcSchema schema = model.getOrCreateSchema(schemaName);
      schema.catalogName = schemas.getString("TABLE_CATALOG");
      schema.isDefault = schemas.getBoolean("IS_DEFAULT");
    }

    // tables
    ResultSet tables = meta.getTables(null, null, null, null);
    while (tables.next()) {
      String schemaName = tables.getString("TABLE_SCHEM");
      String tableName = tables.getString("TABLE_NAME");
      JdbcTable table = model.getOrCreateTable(schemaName, tableName);
      table.tableType = tables.getString("TABLE_TYPE");
    }

    // columns
    ResultSet columns = meta.getColumns(null, null, null, null);
    while (columns.next()) {
      String schemaName = columns.getString("TABLE_SCHEM");
      String tableName = columns.getString("TABLE_NAME");
      String columnName = columns.getString("COLUMN_NAME");
      JdbcColumn column = model.getOrCreateColumn(schemaName, tableName, columnName);
      column.columnType = columns.getInt("DATA_TYPE");
      column.columnTypeName = columns.getString("TYPE_NAME");
      column.columnSize = (Integer) columns.getObject("COLUMN_SIZE");
      column.isNullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
      column.ordinalPosition = columns.getInt("ORDINAL_POSITION");
    }

    // primary keys
    ResultSet primaryKeys = meta.getPrimaryKeys(null, null, null);
    while (primaryKeys.next()) {
      String schemaName = primaryKeys.getString("TABLE_SCHEM");
      String tableName = primaryKeys.getString("TABLE_NAME");
      JdbcTable table = model.getTable(schemaName, tableName);
      JdbcPrimaryKey primaryKey = new JdbcPrimaryKey();
      primaryKey.columnName = primaryKeys.getString("COLUMN_NAME");
      primaryKey.sequenceNumber = primaryKeys.getInt("KEY_SEQ");
      primaryKey.primaryKeyName = primaryKeys.getString("PK_NAME");
      table.primaryKeys.add(primaryKey);
    }
    return model;
  }

}
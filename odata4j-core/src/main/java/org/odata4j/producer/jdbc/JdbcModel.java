package org.odata4j.producer.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Enumerable;
import org.core4j.Predicate1;

public class JdbcModel {

  public final List<JdbcModel.JdbcSchema> schemas = new ArrayList<JdbcModel.JdbcSchema>();

  public static class JdbcSchema {
    public String schemaName;
    public String catalogName;
    public boolean isDefault;

    public final List<JdbcModel.JdbcTable> tables = new ArrayList<JdbcModel.JdbcTable>();
  }

  public static class JdbcTable {
    public String tableName;
    public String tableType;

    public final List<JdbcModel.JdbcColumn> columns = new ArrayList<JdbcModel.JdbcColumn>();
    public final List<JdbcModel.JdbcPrimaryKey> primaryKeys = new ArrayList<JdbcModel.JdbcPrimaryKey>();
  }

  public static class JdbcPrimaryKey {
    public String columnName;
    public int sequenceNumber;
    public String primaryKeyName;
  }

  public static class JdbcColumn {
    public String columnName;
    public int columnType; // java.sql.Types
    public String columnTypeName;
    public Integer columnSize;
    public boolean isNullable;
    public int ordinalPosition;
  }

  public JdbcSchema getOrCreateSchema(String schemaName) {
    JdbcSchema schema = Enumerable.create(schemas).firstOrNull(schemaNameEquals(schemaName));
    if (schema == null) {
      schema = new JdbcSchema();
      schema.schemaName = schemaName;
      schemas.add(schema);
    }
    return schema;
  }

  public JdbcTable getOrCreateTable(String schemaName, String tableName) {
    JdbcSchema schema = Enumerable.create(schemas).first(schemaNameEquals(schemaName));
    JdbcTable table = Enumerable.create(schema.tables).firstOrNull(tableNameEquals(tableName));
    if (table == null) {
      table = new JdbcTable();
      table.tableName = tableName;
      schema.tables.add(table);
    }
    return table;
  }

  public JdbcColumn getOrCreateColumn(String schemaName, String tableName, String columnName) {
    JdbcTable table = getTable(schemaName, tableName);
    JdbcColumn column = Enumerable.create(table.columns).firstOrNull(columnNameEquals(columnName));
    if (column == null) {
      column = new JdbcColumn();
      column.columnName = columnName;
      table.columns.add(column);
    }
    return column;
  }

  public static final Predicate1<JdbcSchema> schemaNameEquals(final String schemaName) {
    return new Predicate1<JdbcSchema>() {
      @Override
      public boolean apply(JdbcSchema schema) {
        return schema.schemaName.equals(schemaName);
      }
    };
  }

  public static final Predicate1<JdbcTable> tableNameEquals(final String tableName) {
    return new Predicate1<JdbcTable>() {
      @Override
      public boolean apply(JdbcTable table) {
        return table.tableName.equals(tableName);
      }
    };
  }

  public static final Predicate1<JdbcColumn> columnNameEquals(final String columnName) {
    return new Predicate1<JdbcColumn>() {
      @Override
      public boolean apply(JdbcColumn column) {
        return column.columnName.equals(columnName);
      }
    };
  }

  public JdbcTable getTable(String schemaName, String tableName) {
    return Enumerable.create(Enumerable.create(schemas).first(schemaNameEquals(schemaName)).tables).first(tableNameEquals(tableName));
  }

  public JdbcTable findTable(String schemaName, String tableName) {
    JdbcSchema schema = Enumerable.create(schemas).firstOrNull(schemaNameEquals(schemaName));
    return schema == null ? null : Enumerable.create(schema.tables).firstOrNull(tableNameEquals(tableName));
  }

}
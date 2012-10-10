package org.odata4j.test.unit.producer.jdbc;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.core4j.ThrowingFunc1;
import org.core4j.xml.XDocument;
import org.core4j.xml.XmlFormat;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.format.xml.EdmxFormatWriter;
import org.odata4j.producer.jdbc.GenerateJdbcModel;
import org.odata4j.producer.jdbc.HsqlAddSystemTables;
import org.odata4j.producer.jdbc.Jdbc;
import org.odata4j.producer.jdbc.JdbcModel;
import org.odata4j.producer.jdbc.JdbcModel.JdbcColumn;
import org.odata4j.producer.jdbc.JdbcModel.JdbcSchema;
import org.odata4j.producer.jdbc.JdbcModel.JdbcTable;
import org.odata4j.producer.jdbc.JdbcModelToMetadata;

@SuppressWarnings("unused")
public class JdbcTest {

  public static Jdbc HSQL_DB = new Jdbc("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:example", "sa", "");

  //@Test
  public void jdbcHsql() throws Exception {
    populateExample();

    HSQL_DB.execute(new ThrowingFunc1<Connection, Void>() {

      @Override
      public Void apply(Connection conn) throws Exception {

        JdbcModel model = new GenerateJdbcModel().apply(conn);
        new HsqlAddSystemTables().apply(model);
        dump(model);
        EdmDataServices edm = new JdbcModelToMetadata().apply(model).getMetadata();
        dump(edm);
        dump(conn.getMetaData().getCatalogs());
        dump(conn.getMetaData().getSchemas());
        dump(conn.getMetaData().getTables(null, null, null, null));
        dump(conn.getMetaData().getPrimaryKeys(null, null, null));
        dump(conn.getMetaData().getColumns(null, "INFORMATION_SCHEMA", "SYSTEM_TABLES", null));

        //        dumpTable(conn, "INFORMATION_SCHEMA.SYSTEM_TABLES");

        return null;
      }
    });

  }

  public static void dump(EdmDataServices metadata) {
    StringWriter sw = new StringWriter();
    EdmxFormatWriter.write(metadata, sw);
    XDocument edmX = XDocument.parse(sw.toString());
    System.out.println(edmX.toString(XmlFormat.INDENTED));
  }

  public static void populateExample() {
    HSQL_DB.execute(new ThrowingFunc1<Connection, Void>() {
      @Override
      public Void apply(Connection conn) throws Exception {
        conn.createStatement().execute("CREATE TABLE CUSTOMER (CUSTOMER_ID INTEGER NOT NULL, CUSTOMER_NAME VARCHAR(25) NOT NULL, PRIMARY KEY (CUSTOMER_ID))");
        conn.createStatement().execute("INSERT INTO CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME) VALUES (1, 'Customer One')");
        conn.createStatement().execute("INSERT INTO CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME) VALUES (2, 'Customer Two')");

        conn.createStatement().execute("CREATE TABLE PRODUCT (PRODUCT_ID INTEGER NOT NULL, PRODUCT_NAME VARCHAR(25) NOT NULL, PRIMARY KEY (PRODUCT_ID))");
        conn.createStatement().execute("INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME) VALUES (1, 'Product One')");

        conn.createStatement().execute("CREATE TABLE CUSTOMER_PRODUCT (CUSTOMER_ID INTEGER NOT NULL, PRODUCT_ID INTEGER NOT NULL, PRIMARY KEY (CUSTOMER_ID, PRODUCT_ID))");
        conn.createStatement().execute("INSERT INTO CUSTOMER_PRODUCT (CUSTOMER_ID, PRODUCT_ID) VALUES (1, 1)");
        conn.createStatement().execute("INSERT INTO CUSTOMER_PRODUCT (CUSTOMER_ID, PRODUCT_ID) VALUES (2, 1)");
        return null;
      }
    });
  }

  private static String pad(String value, int size) {
    if (value != null && value.length() < size) {
      value = value + Enumerable.range(0, size - value.length()).select(new Func1<Integer, String>() {
        @Override
        public String apply(Integer i) {
          return " ";
        }
      }).join("");
    }
    return value;
  }

  private static void dump(ResultSet result) throws SQLException {
    for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
      System.out.print(pad(result.getMetaData().getColumnName(i), 20));
    }
    System.out.println();
    while (result.next()) {
      for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
        String val = result.getString(i);
        val = val == null ? "null" : val;
        System.out.print(pad(val, 20));
      }
      System.out.println();
    }
    result.close();
  }

  private static void dumpTable(Connection conn, String tableName) throws SQLException {
    Statement stmt = conn.createStatement();
    ResultSet result = stmt.executeQuery("select * from " + tableName);
    dump(result);
    stmt.close();
  }

  private static void dump(JdbcModel info) {
    StringBuilder sb = new StringBuilder();
    for (JdbcSchema schema : info.schemas) {
      sb.append("Schema: " + schema.schemaName + "\n");
      for (JdbcTable table : schema.tables) {
        sb.append("  Table: " + table.tableName + "\n");
        for (JdbcColumn column : table.columns) {
          sb.append("    Column: " + column.columnName + "\n");
        }
      }
    }
    System.out.println(sb.toString());
  }

}

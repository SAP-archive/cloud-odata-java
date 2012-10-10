package org.odata4j.examples.producer.jpa;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.odata4j.core.ODataConstants.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

  public static void fillDatabase(String namespace, String pathToSqlFile) {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
    } catch (Exception ex) {
      DatabaseUtils.LOGGER.error("ERROR: failed to load HSQLDB JDBC driver.", ex);
      return;
    }

    Connection conn = null;
    String line = "";
    try {
      conn = DriverManager.getConnection(
          "jdbc:hsqldb:mem:" + namespace,
          "sa",
          "");

      Statement statement = conn.createStatement();

      InputStream xml = DatabaseUtils.class.getResourceAsStream(
          pathToSqlFile);

      BufferedReader br = new BufferedReader(
          new InputStreamReader(xml, Charsets.Upper.UTF_8));

      while ((line = br.readLine()) != null) {
        line = line.replace("`", "");
        line = line.replace(");", ")");
        line = line.replace("'0x", "'");

        if (line.length() > 5) {
          statement.executeUpdate(line);
        }
      }

      br.close();
      statement.close();

    } catch (Exception ex) {
      DatabaseUtils.LOGGER.error(ex.getMessage(), ex);
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException ex) {
          DatabaseUtils.LOGGER.error(ex.getMessage(), ex);
        }
      }
    }
  }
}

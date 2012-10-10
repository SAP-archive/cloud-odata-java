package org.odata4j.producer.jdbc;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.core4j.Func1;
import org.odata4j.core.ImmutableMap;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.producer.jdbc.JdbcModel.JdbcColumn;
import org.odata4j.producer.jdbc.JdbcModel.JdbcPrimaryKey;
import org.odata4j.producer.jdbc.JdbcModel.JdbcSchema;
import org.odata4j.producer.jdbc.JdbcModel.JdbcTable;

public class JdbcModelToMetadata implements Func1<JdbcModel, JdbcMetadataMapping> {

  private static final Map<Integer, EdmType> SIMPLE_TYPE_MAPPING = ImmutableMap.<Integer, EdmType> of(
      Types.INTEGER, EdmSimpleType.INT32,
      Types.VARCHAR, EdmSimpleType.STRING,
      Types.BOOLEAN, EdmSimpleType.BOOLEAN);

  public String getModelNamespace() {
    return "JdbcModel";
  }

  public String getContainerNamespace(String entityContainerName) {
    return "JdbcEntities." + entityContainerName;
  }

  public String getEntityContainerName(String schemaName) {
    return rename(schemaName);
  }

  public String getEntityTypeName(String tableName) {
    return rename(tableName);
  }

  public String getEntitySetName(String tableName) {
    return rename(tableName);
  }

  public String getPropertyName(String columnName) {
    return rename(columnName);
  }

  public String rename(String dbName) {
    return dbName;
  }

  public EdmType getEdmType(int jdbcType, String columnTypeName, Integer columnSize) {
    if (!SIMPLE_TYPE_MAPPING.containsKey(jdbcType))
      throw new UnsupportedOperationException("TODO implement edmtype conversion for jdbc type: " + jdbcType);
    return SIMPLE_TYPE_MAPPING.get(jdbcType);
  }

  @Override
  public JdbcMetadataMapping apply(JdbcModel jdbcModel) {
    String modelNamespace = getModelNamespace();

    List<EdmEntityType.Builder> entityTypes = new ArrayList<EdmEntityType.Builder>();
    List<EdmEntityContainer.Builder> entityContainers = new ArrayList<EdmEntityContainer.Builder>();
    List<EdmEntitySet.Builder> entitySets = new ArrayList<EdmEntitySet.Builder>();

    Map<EdmEntitySet.Builder, JdbcTable> entitySetMapping = new HashMap<EdmEntitySet.Builder, JdbcTable>();
    Map<EdmProperty.Builder, JdbcColumn> propertyMapping = new HashMap<EdmProperty.Builder, JdbcColumn>();

    for (JdbcSchema jdbcSchema : jdbcModel.schemas) {
      for (JdbcTable jdbcTable : jdbcSchema.tables) {
        if (jdbcTable.primaryKeys.isEmpty()) {
          System.err.println("Skipping JdbcTable " + jdbcTable.tableName + ", no keys");
          continue;
        }

        String entityTypeName = getEntityTypeName(jdbcTable.tableName);
        EdmEntityType.Builder entityType = EdmEntityType.newBuilder()
            .setName(entityTypeName)
            .setNamespace(modelNamespace);
        entityTypes.add(entityType);

        for (JdbcPrimaryKey primaryKey : jdbcTable.primaryKeys) {
          String propertyName = getPropertyName(primaryKey.columnName);
          entityType.addKeys(propertyName);
        }

        for (JdbcColumn jdbcColumn : jdbcTable.columns) {
          String propertyName = getPropertyName(jdbcColumn.columnName);
          EdmType propertyType = getEdmType(jdbcColumn.columnType, jdbcColumn.columnTypeName, jdbcColumn.columnSize);
          EdmProperty.Builder property = EdmProperty.newBuilder(propertyName)
              .setType(propertyType)
              .setNullable(jdbcColumn.isNullable);
          entityType.addProperties(property);
          propertyMapping.put(property, jdbcColumn);
        }

        String entitySetName = getEntitySetName(jdbcTable.tableName);
        EdmEntitySet.Builder entitySet = EdmEntitySet.newBuilder()
            .setName(entitySetName)
            .setEntityType(entityType);
        entitySets.add(entitySet);
        entitySetMapping.put(entitySet, jdbcTable);
      }

      String entityContainerName = getEntityContainerName(jdbcSchema.schemaName);
      EdmEntityContainer.Builder entityContainer = EdmEntityContainer.newBuilder()
          .setName(entityContainerName)
          .setIsDefault(jdbcSchema.isDefault)
          .addEntitySets(entitySets);
      entityContainers.add(entityContainer);
    }

    List<EdmSchema.Builder> edmSchemas = new ArrayList<EdmSchema.Builder>();
    EdmSchema.Builder modelSchema = EdmSchema.newBuilder()
        .setNamespace(modelNamespace)
        .addEntityTypes(entityTypes);
    edmSchemas.add(modelSchema);
    for (EdmEntityContainer.Builder entityContainer : entityContainers) {
      String containerSchemaNamespace = getContainerNamespace(entityContainer.getName());
      EdmSchema.Builder containerSchema = EdmSchema.newBuilder()
          .setNamespace(containerSchemaNamespace)
          .addEntityContainers(entityContainer);
      edmSchemas.add(containerSchema);
    }
    EdmDataServices metadata = EdmDataServices.newBuilder()
        .addSchemas(edmSchemas)
        .build();

    Map<EdmEntitySet, JdbcTable> finalEntitySetMapping = new HashMap<EdmEntitySet, JdbcTable>();
    for (Map.Entry<EdmEntitySet.Builder, JdbcTable> entry : entitySetMapping.entrySet()) {
      finalEntitySetMapping.put(entry.getKey().build(), entry.getValue());
    }
    Map<EdmProperty, JdbcColumn> finalPropertyMapping = new HashMap<EdmProperty, JdbcColumn>();
    for (Map.Entry<EdmProperty.Builder, JdbcColumn> entry : propertyMapping.entrySet()) {
      finalPropertyMapping.put(entry.getKey().build(), entry.getValue());
    }
    return new JdbcMetadataMapping(metadata, jdbcModel, finalEntitySetMapping, finalPropertyMapping);
  }

}

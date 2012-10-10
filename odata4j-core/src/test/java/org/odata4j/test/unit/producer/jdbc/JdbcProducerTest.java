package org.odata4j.test.unit.producer.jdbc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import junit.framework.Assert;

import org.core4j.Func;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.expression.Expression;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.command.ProducerCommandContext;
import org.odata4j.producer.jdbc.JdbcModelToMetadata;
import org.odata4j.producer.jdbc.JdbcProducer;
import org.odata4j.producer.jdbc.LoggingCommand;
import org.odata4j.test.unit.Asserts;

public class JdbcProducerTest {

  private static final String CUSTOMER = "Customer";
  private static final String CUSTOMER_ID = "CustomerId";
  private static final String CUSTOMER_NAME = "CustomerName";

  private static final String CUSTOMER_PRODUCT = "CustomerProduct";

  private static String constantToPascalCase(String constantCase) {
    String[] tokens = constantCase.split("_");
    StringBuilder sb = new StringBuilder();
    for (String token : tokens) {
      if (token.isEmpty())
        continue;
      sb.append(Character.toUpperCase(token.charAt(0)));
      if (token.length() > 1)
        sb.append(token.substring(1).toLowerCase());
    }
    return sb.toString();
  }

  @BeforeClass
  public static void setupClass() {
    // a ResponseBuilder instance is required by negative tests checking for an exception (e.g. NotFoundException)
    ResponseBuilder rbMock = mock(ResponseBuilder.class);
    RuntimeDelegate rdMock = mock(RuntimeDelegate.class);
    when(rdMock.createResponseBuilder()).thenReturn(rbMock);
    RuntimeDelegate.setInstance(rdMock);
  }

  @Test
  public void jdbcProducer() {

    JdbcTest.populateExample();

    JdbcModelToMetadata modelToMetadata = new JdbcModelToMetadata() {
      @Override
      public String rename(String dbName) {
        return constantToPascalCase(dbName);
      }
    };

    JdbcProducer producer = JdbcProducer.newBuilder()
        .jdbc(JdbcTest.HSQL_DB)
        .insert(ProducerCommandContext.class, new LoggingCommand())
        .register(JdbcModelToMetadata.class, modelToMetadata)
        .build();

    // getMetadata
    EdmDataServices metadata = producer.getMetadata();
    Assert.assertNotNull(metadata);
    JdbcTest.dump(metadata);
    EdmEntitySet customerEntitySet = metadata.findEdmEntitySet(CUSTOMER);
    Assert.assertNotNull(customerEntitySet);
    Assert.assertEquals(CUSTOMER, customerEntitySet.getName());

    // getEntity - simple key
    EntityResponse entityResponse = producer.getEntity(null, CUSTOMER, OEntityKey.create(1), null);
    Assert.assertNotNull(entityResponse);
    Assert.assertNotNull(entityResponse.getEntity());
    Assert.assertEquals("Customer One", entityResponse.getEntity().getProperty(CUSTOMER_NAME).getValue());

    // getEntity - not found
    Asserts.assertThrows(NotFoundException.class, getEntity(producer, CUSTOMER, OEntityKey.create(-1), null));

    // getEntity - found, but filtered out
    BoolCommonExpression filter = Expression.boolean_(false);
    Asserts.assertThrows(NotFoundException.class, getEntity(producer, CUSTOMER, OEntityKey.create(1), EntityQueryInfo.newBuilder().setFilter(filter).build()));

    // getEntity - complex key
    entityResponse = producer.getEntity(null, CUSTOMER_PRODUCT, OEntityKey.create("CustomerId", 1, "ProductId", 1), null);
    Assert.assertNotNull(entityResponse);
    Assert.assertNotNull(entityResponse.getEntity());

    // getEntities - no query
    EntitiesResponse entitiesResponse = producer.getEntities(null, CUSTOMER, null);
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(2, entitiesResponse.getEntities().size());

    // getEntities - not found
    Asserts.assertThrows(NotFoundException.class, getEntities(producer, "badEntitySet", null));

    // getEntities - id = 1
    filter = Expression.eq(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(1));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(1, entitiesResponse.getEntities().size());
    Assert.assertEquals("Customer One", entitiesResponse.getEntities().get(0).getProperty(CUSTOMER_NAME).getValue());

    // getEntities - name = 'Customer Two'
    filter = Expression.eq(Expression.simpleProperty(CUSTOMER_NAME), Expression.literal("Customer Two"));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(1, entitiesResponse.getEntities().size());
    Assert.assertEquals("Customer Two", entitiesResponse.getEntities().get(0).getProperty(CUSTOMER_NAME).getValue());

    // getEntities - 1 = id
    filter = Expression.eq(Expression.literal(1), Expression.simpleProperty(CUSTOMER_ID));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(1, entitiesResponse.getEntities().size());
    Assert.assertEquals("Customer One", entitiesResponse.getEntities().get(0).getProperty(CUSTOMER_NAME).getValue());

    // getEntities - no results
    filter = Expression.eq(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(-1));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(0, entitiesResponse.getEntities().size());

    // getEntities - id <> 1
    filter = Expression.ne(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(1));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(1, entitiesResponse.getEntities().size());
    Assert.assertEquals("Customer Two", entitiesResponse.getEntities().get(0).getProperty(CUSTOMER_NAME).getValue());

    // getEntities - id > 1
    filter = Expression.gt(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(1));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(1, entitiesResponse.getEntities().size());

    // getEntities - id >= 1
    filter = Expression.ge(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(1));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(2, entitiesResponse.getEntities().size());

    // getEntities - id < 2
    filter = Expression.lt(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(2));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(1, entitiesResponse.getEntities().size());

    // getEntities - id <= 2
    filter = Expression.le(Expression.simpleProperty(CUSTOMER_ID), Expression.literal(2));
    entitiesResponse = producer.getEntities(null, CUSTOMER, QueryInfo.newBuilder().setFilter(filter).build());
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(2, entitiesResponse.getEntities().size());

    // createEntity - id = 3
    entityResponse = producer.createEntity(null, CUSTOMER, newCustomer(customerEntitySet, 3, "Customer Three"));
    Assert.assertNotNull(entityResponse);
    Assert.assertNotNull(entityResponse.getEntity());
    entitiesResponse = producer.getEntities(null, CUSTOMER, null);
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(3, entitiesResponse.getEntities().size());

    // deleteEntity - id = 3
    producer.deleteEntity(null, CUSTOMER, OEntityKey.create(3));
    entitiesResponse = producer.getEntities(null, CUSTOMER, null);
    Assert.assertNotNull(entitiesResponse);
    Assert.assertEquals(CUSTOMER, entitiesResponse.getEntitySet().getName());
    Assert.assertEquals(2, entitiesResponse.getEntities().size());

    // close
    producer.close();
  }

  private static OEntity newCustomer(EdmEntitySet entitySet, int id, String name) {
    List<OProperty<?>> properties = new ArrayList<OProperty<?>>();
    properties.add(OProperties.int32(CUSTOMER_ID, id));
    properties.add(OProperties.string(CUSTOMER_NAME, name));
    return OEntities.createRequest(entitySet, properties, null);
  }

  private static Func<EntitiesResponse> getEntities(final ODataProducer producer, final String entitySet, final QueryInfo queryInfo) {
    return new Func<EntitiesResponse>() {
      @Override
      public EntitiesResponse apply() {
        return producer.getEntities(null, entitySet, queryInfo);
      }
    };
  }

  private static Func<EntityResponse> getEntity(final ODataProducer producer, final String entitySet, final OEntityKey key, final EntityQueryInfo queryInfo) {
    return new Func<EntityResponse>() {
      @Override
      public EntityResponse apply() {
        return producer.getEntity(null, entitySet, key, queryInfo);
      }
    };
  }

}

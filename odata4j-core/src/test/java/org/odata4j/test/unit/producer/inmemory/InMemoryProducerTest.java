package org.odata4j.test.unit.producer.inmemory;

import java.util.List;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.Func1;
import org.core4j.Funcs;
import org.junit.Test;
import org.odata4j.core.OAtomStreamEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmProperty;
import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.expression.Expression;
import org.odata4j.producer.CountResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.InlineCount;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.OptionsQueryParser;

@SuppressWarnings("unused")
public class InMemoryProducerTest {
  private final QueryInfo NULL_QUERY = new QueryInfo(InlineCount.ALLPAGES, null, null, null, null, null, null, null, null);
  private final EntityQueryInfo NULL_ENTITY_QUERY = new EntityQueryInfo(null, null, null, null);

  @Test
  public void inlineCountWithOneShotIterable() {
    InMemoryProducer producer = new InMemoryProducer("inlineCountWithOneShotIterable");
    final List<String> testData = Enumerable.create("one", "two", "three").toList();
    Func<Iterable<String>> getTestData = new Func<Iterable<String>>() {
      @Override
      public Iterable<String> apply() {
        // worst case - a one shot iterable
        return Enumerable.createFromIterator(Funcs.constant(testData.iterator()));
      }
    };
    producer.register(String.class, String.class, "TestData", getTestData, Funcs.identity(String.class));

    EntitiesResponse response = producer.getEntities(null, "TestData", null);
    Assert.assertEquals(3, response.getEntities().size());
    Assert.assertNull(response.getInlineCount());

    response = producer.getEntities(null, "TestData", NULL_QUERY);
    Assert.assertEquals(3, response.getEntities().size());
    Assert.assertEquals(Integer.valueOf(3), response.getInlineCount());
  }

  @Test
  public void testStreamEntity() {
    final InMemoryProducer p = new InMemoryProducer("testStreamEntity");
    p.register(StreamEntity.class, "setName", new Func<Iterable<StreamEntity>>() {
      @Override
      public Iterable<StreamEntity> apply() {
        return Enumerable.create(new StreamEntity());
      }
    }, "Id");
    p.register(String.class, String.class, "ss", new Func<Iterable<String>>() {
      @Override
      public Iterable<String> apply() {
        return Enumerable.create("aaa");
      }
    }, Funcs.identity(String.class));

    final EdmEntitySet setName = p.getMetadata().findEdmEntitySet("setName");
    Assert.assertNotNull(setName);
    Assert.assertTrue(setName.getType().getHasStream());

    final EdmEntitySet ss = p.getMetadata().findEdmEntitySet("ss");
    Assert.assertNotNull(ss);
    Assert.assertFalse(ss.getType().getHasStream());
  }

  @Test
  public void testSetNameAndType() {
    final SimpleEntity e1 = new SimpleEntity();
    String namespace = "testSetNameAndType";
    InMemoryProducer p = new InMemoryProducer(namespace);
    p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        return Enumerable.create(e1, new SimpleEntity());
      }
    }, "Id");

    Assert.assertEquals(2, p.getEntities(null, "setName", NULL_QUERY).getEntities().size());
    Assert.assertNotNull(p.getEntity(null, "setName", OEntityKey.create(e1.getId()), NULL_ENTITY_QUERY).getEntity());

    Assert.assertNotNull(p.getMetadata().findEdmEntitySet("setName"));
    Assert.assertNotNull(p.getMetadata().findEdmEntityType(namespace + ".typeName"));
  }

  @Test
  public void complexQuery() {
    class Entry {
      public String getFoo() {
        return "322COMMON333";
      }

      public int getId() {
        return System.identityHashCode(this);
      }
    }

    InMemoryProducer producer = new InMemoryProducer("complexQuery");
    final List<Entry> testData = Enumerable.create(new Entry(), new Entry()).toList();
    Func<Iterable<Entry>> getTestData = new Func<Iterable<Entry>>() {
      @Override
      public Iterable<Entry> apply() {
        // worst case - a one shot iterable
        return Enumerable.createFromIterator(Funcs.constant(testData.iterator()));
      }
    };
    producer.register(Entry.class, Integer.class, "TestData", getTestData, new Func1<Entry, Integer>() {
      @Override
      public Integer apply(Entry entry) {
        return entry.getId();
      }
    });

    QueryInfo qi = new QueryInfo(InlineCount.ALLPAGES, null, null,
        OptionsQueryParser.parseFilter("(Foo ne null) and substringof('common',tolower(Foo))"), null, null, null, null, null);
    EntitiesResponse data = producer.getEntities(null, "TestData", qi);
    Assert.assertEquals(data.getEntities().size(), 2);
  }

  @Test
  public void testSimpleCount() {
    InMemoryProducer p = new InMemoryProducer("testSimpleCount");
    p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        return Enumerable.create(new SimpleEntity(), new SimpleEntity());
      }
    }, "Id");

    CountResponse response = p.getEntitiesCount(null, "setName", null);
    Assert.assertEquals(2L, response.getCount());
  }

  @Test
  public void testFilteredCount() {
    InMemoryProducer p = new InMemoryProducer("testFilteredCount");
    p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        return Enumerable.create(new SimpleEntity(1), new SimpleEntity(2));
      }
    }, "Id");

    BoolCommonExpression filter = Expression.gt(Expression.simpleProperty("Integer"), Expression.integral(1));
    CountResponse response = p.getEntitiesCount(null, "setName", new QueryInfo(InlineCount.NONE, null, null, filter, null, null, null, null, null));
    Assert.assertEquals(1L, response.getCount());
  }

  @Test
  public void testTopCount() {
    InMemoryProducer p = new InMemoryProducer("testTopCount");
    p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        return Enumerable.create(new SimpleEntity(1), new SimpleEntity(2), new SimpleEntity(3), new SimpleEntity(4), new SimpleEntity(5));
      }
    }, "Id");

    CountResponse response = p.getEntitiesCount(null, "setName", new QueryInfo(InlineCount.NONE, 3, null, null, null, null, null, null, null));
    Assert.assertEquals(3L, response.getCount());
  }

  @Test
  public void testSkipCount() {
    InMemoryProducer p = new InMemoryProducer("testSkipCount");
    p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        return Enumerable.create(new SimpleEntity(1), new SimpleEntity(2), new SimpleEntity(3), new SimpleEntity(4), new SimpleEntity(5));
      }
    }, "Id");

    CountResponse response = p.getEntitiesCount(null, "setName", new QueryInfo(InlineCount.NONE, null, 3, null, null, null, null, null, null));
    Assert.assertEquals(2L, response.getCount());
  }

  @Test
  public void testMetadataContainerName() {
    InMemoryProducer p = new InMemoryProducer("testMetadataContainerName", "Foo", 20, null, null);
    String name = p.getMetadata().getSchemas().iterator().next().getEntityContainers().iterator().next().getName();
    Assert.assertEquals("Foo", name);
  }

  @Test
  public void testKeysAreNotNull() {
    InMemoryProducer p = new InMemoryProducer("testKeysAreNotNull");
    p.register(SimpleEntity.class, "QQ", new Func<Iterable<SimpleEntity>>() {
      @Override
      public Iterable<SimpleEntity> apply() {
        return Enumerable.create(new SimpleEntity());
      }
    }, "Id", "String");

    Enumerable<EdmProperty> properties = p.getMetadata().getEdmEntitySet("QQ").getType().getProperties();
    boolean found = false;
    for (EdmProperty property : properties) {
      if (property.getName().equals("Id") || property.getName().equals("String")) {
        Assert.assertFalse(property.isNullable());
        found = true;
      }
    }
    Assert.assertTrue("There should be keys", found);
  }

  private static class SimpleEntity {
    private final int integer;

    public SimpleEntity() {
      this(0);
    }

    public SimpleEntity(int integer) {
      this.integer = integer;
    }

    public String getId() {
      return String.valueOf(System.identityHashCode(this));
    }

    public String getString() {
      return "string-" + getId();
    }

    public boolean getBool() {
      return false;
    }

    public int getInteger() {
      return integer;
    }
  }

  private static class StreamEntity extends SimpleEntity implements OAtomStreamEntity {
    @Override
    public String getAtomEntityType() {
      return "application/zip";
    }

    @Override
    public String getAtomEntitySource() {
      return "somewhere";
    }
  }

}

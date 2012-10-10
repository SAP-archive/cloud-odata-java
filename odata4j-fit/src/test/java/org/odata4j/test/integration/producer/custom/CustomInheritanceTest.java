package org.odata4j.test.integration.producer.custom;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.ORelatedEntitiesLink;
import org.odata4j.core.ORelatedEntitiesLinkInline;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.format.FormatType;

public class CustomInheritanceTest extends CustomBaseTest {

  public CustomInheritanceTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void testGetEntityPolymorphic() throws Exception {
    // GET an entity whose type is actually a subclass of the requested entity set.
    testGetEntity(FormatType.JSON);
  }

  @Test
  public void testGetEntityPolymorphicAtom() throws Exception {
    // GET an entity whose type is actually a subclass of the requested entity set.
    testGetEntity(FormatType.ATOM);
  }

  private void testGetEntity(FormatType ft) throws Exception {
    ODataConsumer consumer = createConsumer(ft);

    //this.dumpResource("FileSystemItems('Dir-3')", ft);
    //this.dumpResource("FileSystemItems('File-2-Dir-2')", ft);

    checkEntityType(consumer.getEntity("FileSystemItems", "Dir-3").execute());
    checkEntityType(consumer.getEntity("FileSystemItems", "File-2-Dir-2").execute());
  }

  @Test
  public void testGetInlineEntityPolymorphic() throws Exception {
    // expand a nav prop where the associated entity is-a subclass of the nav props type.
    testGetInlineEntity(FormatType.JSON);
  }

  @Test
  public void testGetInlineEntityPolymorphicAtom() throws Exception {
    // expand a nav prop where the associated entity is-a subclass of the nav props type.
    testGetInlineEntity(FormatType.ATOM);
  }

  private void testGetInlineEntity(FormatType ft) throws Exception {
    ODataConsumer consumer = createConsumer(ft);

    this.dumpResource("FileSystemItems('Dir-3')?$expand=Items,NewestItem", ft);
    OEntity e = consumer.getEntity("FileSystemItems", "Dir-3").expand("Items,NewestItem").execute();
    checkEntityType(e);
    for (OEntity item : e.getLink("Items", ORelatedEntitiesLink.class).getRelatedEntities()) {
      checkEntityType(item);
    }
    checkEntityType(e.getLink("NewestItem", ORelatedEntityLink.class).getRelatedEntity());
  }

  @Test
  public void testGetEntitiesPolymorphic() throws Exception {
    testGetEntities(FormatType.JSON);
  }

  @Test
  public void testGetEntitiesPolymorphicAtom() throws Exception {
    testGetEntities(FormatType.ATOM);
  }

  private void testGetEntities(FormatType ft) throws Exception {
    ODataConsumer consumer = createConsumer(ft);

    dumpResource("FileSystemItems", ft);
    // GET some entities whose types are actually a subclasses of the requested entity set.
    for (OEntity e : consumer.getEntities("FileSystemItems").execute()) {
      checkEntityType(e);
    }
  }

  private void checkEntityType(OEntity e) {
    System.out.println("check entity: " + e.getEntityKey());
    String name = e.getProperty("Name", String.class).getValue();
    if (name.startsWith("Dir"))
      assertThat(e.getEntityType().getName(), is("Directory"));
    else if (name.startsWith("File"))
      assertThat(e.getEntityType().getName(), is("File"));
    else
      fail();
  }

  @Test
  public void testGetInlineEntitiesPolymorphic() throws Exception {
    // expand a nav prop where the associated entity is-a subclass of the nav props type.
    testGetInlineEntities(FormatType.JSON);
  }

  @Test
  public void testGetInlineEntitiesPolymorphicAtom() throws Exception {
    // expand a nav prop where the associated entity is-a subclass of the nav props type.
    testGetInlineEntities(FormatType.ATOM);
  }

  private void testGetInlineEntities(FormatType ft) throws Exception {
    ODataConsumer consumer = createConsumer(ft);

    this.dumpResourceJSON("Directories?$expand=Items");
    // GET some entities whose types are actually a subclasses of the requested entity set.
    for (OEntity e : consumer.getEntities("Directories").expand("Items").execute()) {
      checkEntityType(e);
      ORelatedEntitiesLink l = e.getLink("Items", ORelatedEntitiesLinkInline.class);
      if (l.getRelatedEntities() != null) {
        for (OEntity i : l.getRelatedEntities()) {
          checkEntityType(i);
        }
      }
    }
  }
}

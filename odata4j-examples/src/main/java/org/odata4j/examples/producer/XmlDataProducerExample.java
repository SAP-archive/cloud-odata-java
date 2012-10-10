package org.odata4j.examples.producer;

import static org.odata4j.examples.JaxRsImplementation.JERSEY;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.odata4j.core.ODataConstants.Charsets;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.core.Throwables;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.examples.AbstractExample;
import org.odata4j.examples.ODataServerFactory;
import org.odata4j.examples.producer.jpa.DatabaseUtils;
import org.odata4j.examples.producer.jpa.northwind.Customers;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.CountResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityIdResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.edm.MetadataProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;

/**
 * This example shows how to expose xml data as an atom feed.
 */
public class XmlDataProducerExample extends AbstractExample {

  public static final String endpointUri = "http://localhost:8010/XmlDataProducerExample.svc";

  public static void main(String[] args) {
    XmlDataProducerExample example = new XmlDataProducerExample();
    example.run(args);
  }

  private void run(String[] args) {

    System.out.println("Please direct your browser to " + endpointUri + "Customers");

    // register the producer as the static instance, then launch the http server
    DefaultODataProducerProvider.setInstance(new XmlDataProducer());
    new ODataServerFactory(JERSEY).hostODataServer(endpointUri);

    // generateXmlTestData();
  }

  @XmlRootElement
  public class CustomersList {
    @XmlElement
    Customers[] customers;
  }

  public void generateXmlTestData() throws Exception {
    EntityManagerFactory emf;
    String persistenceUnitName = "NorthwindServiceEclipseLink";

    // create an fill temporary database
    emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    emf.createEntityManager().close();
    DatabaseUtils.fillDatabase("northwind", "/META-INF/northwind_insert.sql");

    // select the customers
    EntityManager em = emf.createEntityManager();
    try {
      Query q = em.createQuery("SELECT c FROM Customers c");

      // marshal them to the test data file
      Marshaller marshaller = JAXBContext.newInstance(CustomersList.class).createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_ENCODING, Charsets.Upper.UTF_8);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      Writer out = new OutputStreamWriter(new FileOutputStream("/META-INF/xmlDataProducerExampleTestData.xml"), Charsets.Upper.UTF_8);
      try {
        List<?> res = q.getResultList();
        CustomersList c = new CustomersList();
        c.customers = res.toArray(new Customers[res.size()]);
        marshaller.marshal(c, out);
        out.flush();
      } finally {
        out.close();
      }
    } finally {
      em.close();
    }
  }

  /**
   * Sample ODataProducer for providing xml data as an atom feed.
   */
  public class XmlDataProducer implements ODataProducer {

    private final EdmDataServices metadata;
    private XMLInputFactory xmlInputFactory;

    public XmlDataProducer() {
      // build the metadata here hardcoded as example
      // one would probably generate it from xsd schema or something else
      String namespace = "XmlExample";

      List<EdmProperty.Builder> properties = new ArrayList<EdmProperty.Builder>();
      properties.add(EdmProperty.newBuilder("address").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("city").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("companyName").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("contactName").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("contactTitle").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("country").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("customerID").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("fax").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("phone").setType(EdmSimpleType.STRING));
      properties.add(EdmProperty.newBuilder("postalCode").setType(EdmSimpleType.STRING));

      List<EdmEntityType.Builder> entityTypes = new ArrayList<EdmEntityType.Builder>();
      EdmEntityType.Builder type = EdmEntityType.newBuilder().setNamespace(namespace).setName("Customers").addKeys("customerID").addProperties(properties);
      entityTypes.add(type);

      List<EdmEntitySet.Builder> entitySets = new ArrayList<EdmEntitySet.Builder>();
      entitySets.add(EdmEntitySet.newBuilder().setName("Customers").setEntityType(type));

      EdmEntityContainer.Builder container = EdmEntityContainer.newBuilder().setName(namespace + "Entities").setIsDefault(true).addEntitySets(entitySets);
      EdmSchema.Builder modelSchema = EdmSchema.newBuilder().setNamespace(namespace + "Model").addEntityTypes(entityTypes);
      EdmSchema.Builder containerSchema = EdmSchema.newBuilder().setNamespace(namespace + "Container").addEntityContainers(container);

      metadata = EdmDataServices.newBuilder().addSchemas(containerSchema, modelSchema).build();

      xmlInputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public EdmDataServices getMetadata() {
      return this.metadata;
    }

    /**
     * Returns OEntities build from xml data. In the real world the xml data
     * could be filtered using the provided <code>queryInfo.filter</code>.
     * The real implementation should also respect
     * <code>queryInfo.top</code> and <code>queryInfo.skip</code>.
     */
    @Override
    public EntitiesResponse getEntities(ODataContext context, String entitySetName, QueryInfo queryInfo) {
      EdmEntitySet ees = getMetadata().getEdmEntitySet(entitySetName);

      InputStream is = getClass().getResourceAsStream("/META-INF/xmlDataProducerExampleTestData.xml");
      XMLEventReader reader = null;
      try {
        // transform the xml to OEntities with OProperties.
        // links are omitted for simplicity
        reader = xmlInputFactory.createXMLEventReader(is);

        List<OEntity> entities = new ArrayList<OEntity>();
        List<OProperty<?>> properties = new ArrayList<OProperty<?>>();
        boolean inCustomer = false;
        String id = null;
        String data = null;
        while (reader.hasNext()) {
          XMLEvent event = reader.nextEvent();

          if (event.isStartElement()) {
            if ("customers".equals(event.asStartElement().getName().getLocalPart())) {
              inCustomer = true;
            }
          } else if (event.isEndElement()) {
            String name = event.asEndElement().getName().getLocalPart();
            if ("customers".equals(name)) {
              entities.add(OEntities.create(ees, OEntityKey.create(id), properties, null));
              properties = new ArrayList<OProperty<?>>();
              inCustomer = false;
            } else if (inCustomer) {
              if ("customerID".equals(name)) {
                id = data;
              }
              properties.add(OProperties.string(name, data));
            }
          } else if (event.isCharacters()) {
            data = event.asCharacters().getData();
          }
        }

        return Responses.entities(entities, ees, null, null);
      } catch (XMLStreamException ex) {
        throw Throwables.propagate(ex);
      } finally {
        try {
          if (reader != null) reader.close();
        } catch (XMLStreamException ignore) {}

        try {
          is.close();
        } catch (IOException ignore) {}
      }
    }

    @Override
    public CountResponse getEntitiesCount(ODataContext context, String entitySetName, QueryInfo queryInfo) {
      throw new NotImplementedException();
    }

    @Override
    public EntitiesResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
      throw new NotImplementedException();
    }

    @Override
    public CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
      throw new NotImplementedException();
    }

    @Override
    public void close() {}

    @Override
    public EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity) {
      throw new NotImplementedException();
    }

    @Override
    public EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
      throw new NotImplementedException();
    }

    @Override
    public void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey) {
      throw new NotImplementedException();
    }

    @Override
    public void mergeEntity(ODataContext context, String entitySetName, OEntity entity) {
      throw new NotImplementedException();
    }

    @Override
    public void updateEntity(ODataContext context, String entitySetName, OEntity entity) {
      throw new NotImplementedException();
    }

    @Override
    public EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
      throw new NotImplementedException();
    }

    @Override
    public EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp) {
      throw new NotImplementedException();
    }

    @Override
    public void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
      throw new NotImplementedException();
    }

    @Override
    public void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
      throw new NotImplementedException();
    }

    @Override
    public void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
      throw new NotImplementedException();
    }

    @Override
    public BaseResponse callFunction(ODataContext context, EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
      throw new NotImplementedException();
    }

    @Override
    public MetadataProducer getMetadataProducer() {
      return null;
    }

    @Override
    public <TExtension extends OExtension<ODataProducer>> TExtension findExtension(Class<TExtension> clazz) {
      return null;
    }

  }
}

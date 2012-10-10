package org.odata4j.consumer;

import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.core4j.Enumerable;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatType;
import org.odata4j.format.SingleLink;
import org.odata4j.format.xml.AtomCollectionInfo;
import org.odata4j.format.xml.AtomServiceDocumentFormatParser;
import org.odata4j.format.xml.AtomSingleLinkFormatParser;
import org.odata4j.format.xml.AtomWorkspaceInfo;
import org.odata4j.format.xml.EdmxFormatParser;
import org.odata4j.stax2.XMLEventReader2;

/**
 * Useful base class for {@link ODataClient} implementations with common functionality.
 */
public abstract class AbstractODataClient implements ODataClient {

  private FormatType formatType;

  protected AbstractODataClient(FormatType formatType) {
    this.formatType = formatType;
  }

  public FormatType getFormatType() {
    return this.formatType;
  }

  public EdmDataServices getMetadata(ODataClientRequest request) throws ODataProducerException {
    ODataClientResponse response = doRequest(FormatType.ATOM, request, Status.OK);
    EdmDataServices metadata = new EdmxFormatParser().parseMetadata(toXml(response));
    response.close();
    return metadata;
  }

  public Iterable<AtomCollectionInfo> getCollections(ODataClientRequest request) throws ODataProducerException {
    ODataClientResponse response = doRequest(FormatType.ATOM, request, Status.OK);
    Enumerable<AtomCollectionInfo> collections = Enumerable.create(AtomServiceDocumentFormatParser.parseWorkspaces(toXml(response)))
        .selectMany(AtomWorkspaceInfo.GET_COLLECTIONS);
    response.close();
    return collections;
  }

  public Iterable<SingleLink> getLinks(ODataClientRequest request) throws ODataProducerException {
    ODataClientResponse response = doRequest(FormatType.ATOM, request, Status.OK);
    Iterable<SingleLink> links = AtomSingleLinkFormatParser.parseLinks(toXml(response));
    response.close();
    return links;
  }

  public ODataClientResponse getEntity(ODataClientRequest request) throws ODataProducerException {
    return doRequest(getFormatType(), request, Status.OK, Status.NO_CONTENT);
  }

  public ODataClientResponse getEntities(ODataClientRequest request) throws ODataProducerException {
    return doRequest(getFormatType(), request, Status.OK);
  }

  public ODataClientResponse callFunction(ODataClientRequest request) throws ODataProducerException {
    return doRequest(getFormatType(), request, Status.OK, Status.NO_CONTENT);
  }

  public ODataClientResponse createEntity(ODataClientRequest request) throws ODataProducerException {
    return doRequest(getFormatType(), request, Status.CREATED);
  }

  public void updateEntity(ODataClientRequest request) throws ODataProducerException {
    doRequest(getFormatType(), request, Status.OK, Status.NO_CONTENT).close();
  }

  public void deleteEntity(ODataClientRequest request) throws ODataProducerException {
    doRequest(getFormatType(), request, Status.OK, Status.NO_CONTENT).close();
  }

  public void deleteLink(ODataClientRequest request) throws ODataProducerException {
    doRequest(getFormatType(), request, Status.NO_CONTENT).close();
  }

  public void createLink(ODataClientRequest request) throws ODataProducerException {
    doRequest(getFormatType(), request, Status.NO_CONTENT).close();
  }

  public void updateLink(ODataClientRequest request) throws ODataProducerException {
    doRequest(getFormatType(), request, Status.NO_CONTENT).close();
  }

  public Entry createRequestEntry(EdmEntitySet entitySet, OEntityKey entityKey, List<OProperty<?>> props, List<OLink> links) {
    final OEntity entity = entityKey == null
        ? OEntities.createRequest(entitySet, props, links)
        : OEntities.create(entitySet, entityKey, props, links);

    return new Entry() {

      public String getUri() {
        return null;
      }

      public OEntity getEntity() {
        return entity;
      }

    };
  }

  protected abstract ODataClientResponse doRequest(FormatType reqType, ODataClientRequest request, StatusType... expectedResponseStatus) throws ODataProducerException;

  protected abstract XMLEventReader2 toXml(ODataClientResponse response);

}

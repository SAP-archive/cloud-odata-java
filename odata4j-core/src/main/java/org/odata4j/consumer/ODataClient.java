package org.odata4j.consumer;

import java.io.Reader;
import java.util.List;

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

/**
 * <code>ODataClient</code> is one abstraction layer below <code>ODataConsumer</code>.
 *
 * <p>This api is mostly implemented in terms of request + response objects that closely
 * resemble the underlying http request + response.</p>
 */
public interface ODataClient {

  FormatType getFormatType();

  EdmDataServices getMetadata(ODataClientRequest request) throws ODataProducerException;

  Iterable<AtomCollectionInfo> getCollections(ODataClientRequest request) throws ODataProducerException;

  Iterable<SingleLink> getLinks(ODataClientRequest request) throws ODataProducerException;

  ODataClientResponse getEntity(ODataClientRequest request) throws ODataProducerException;

  ODataClientResponse getEntities(ODataClientRequest request) throws ODataProducerException;

  ODataClientResponse callFunction(ODataClientRequest request) throws ODataProducerException;

  ODataClientResponse createEntity(ODataClientRequest request) throws ODataProducerException;

  void updateEntity(ODataClientRequest request) throws ODataProducerException;

  void deleteEntity(ODataClientRequest request) throws ODataProducerException;

  void deleteLink(ODataClientRequest request) throws ODataProducerException;

  void createLink(ODataClientRequest request) throws ODataProducerException;

  void updateLink(ODataClientRequest request) throws ODataProducerException;

  Entry createRequestEntry(EdmEntitySet entitySet, OEntityKey entityKey, List<OProperty<?>> props, List<OLink> links);

  String requestBody(FormatType formatType, ODataClientRequest request) throws ODataProducerException;

  Reader getFeedReader(ODataClientResponse response);
}

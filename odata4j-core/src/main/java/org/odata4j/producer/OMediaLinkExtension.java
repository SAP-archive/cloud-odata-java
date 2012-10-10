package org.odata4j.producer;

import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.core.HttpHeaders;

import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.edm.EdmEntitySet;

/**
 * An optional extension that a producer can expose to work with Media Link Entries.
 */
public interface OMediaLinkExtension extends OExtension<ODataProducer> {

  /**
   * Gets an InputStream that streams the bytes of the media resource associated
   * with the given media link entry entity.
   *
   * @param mle  the media link entry entity
   * @param etag  for future extension
   * @param query  additional request information
   * @return InputStream for the media resource
   */
  InputStream getInputStreamForMediaLinkEntry(ODataContext odataContext, OEntity mle, String etag, EntityQueryInfo query);

  /**
   * Gets an OutputStream for the purpose of creating a media resource.
   *
   * @param mle  the media link entry entity
   * @param etag  for future extension
   * @param query  additional request information
   * @return stream to write the resource
   */
  OutputStream getOutputStreamForMediaLinkEntryCreate(ODataContext odataContext, OEntity mle, String etag, QueryInfo query);

  /**
   * Gets an OutputStream for the purpose of updating an existing media resource.
   *
   * @param mle  the media link entry entity
   * @param etag  for future extension
   * @param query  additional request information
   * @return stream to update the resource
   */
  OutputStream getOutputStreamForMediaLinkEntryUpdate(ODataContext odataContext, OEntity mle, String etag, QueryInfo query);

  /**
   * Deletes the media resource defined by the given media link entry entity.
   *
   * @param mle  an existing media link entry
   * @param query  additional request information
   */
  void deleteStream(ODataContext odataContext, OEntity mle, QueryInfo query);

  /**
   * Gets the mime content type for the given media link entry entity.
   *
   * @param mle  an existing media link entry
   * @return the mime content type
   */
  String getMediaLinkContentType(ODataContext taContext, OEntity mle);

  /**
   * Gets the mime content disposition for the given media link entry entity.
   *
   * @param mle  an existing media link entry
   * @return the mime content disposition
   */
  String getMediaLinkContentDisposition(ODataContext odataContext, OEntity mle);

  /**
   * Creates an OEntity for a new media link entry request just received.
   *
   * @param entitySet  entity-set
   * @param httpHeaders  Atom protocol says the Slug header can contain additional create info.
   * @return the new entity
   */
  OEntity createMediaLinkEntry(ODataContext odataContext, EdmEntitySet entitySet, HttpHeaders httpHeaders);

  /**
   * Gets an OEntity for an existing media link entry with the given key.
   *
   * @param entitySet  entity-set
   * @param key  entity key
   * @param httpHeaders  Atom protocol says the Slug header can contain additional create info.
   * @return the entity
   */
  OEntity getMediaLinkEntryForUpdateOrDelete(ODataContext odataContext, EdmEntitySet entitySet, OEntityKey key, HttpHeaders httpHeaders);

  /**
   * Updates an OEntity for an existing media link entry.
   *
   * After a create or update of a media resource, more information may be
   * available about the media link entity that was created before the media
   * resource bits were processed.  updateMediaLinkEntry will be called after the
   * media resource bits have been written to outStream and outStream has been
   * closed.
   *
   * Note: this is only necessary because OEntity is immutable
   *
   * @param mle  an existing media link entry
   * @param outStream  media stream
   * @return an updated Media Link Entity
   */
  OEntity updateMediaLinkEntry(ODataContext odataContext, OEntity mle, OutputStream outStream);

}

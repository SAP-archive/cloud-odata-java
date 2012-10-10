package org.odata4j.producer.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;

import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataHttpMethod;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityIds;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.MethodNotAllowedException;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataContextImpl;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.OMediaLinkExtension;

@Path("{entitySetName: [^/()]+?}{id: \\(.+?\\)}")
public class EntityRequestResource extends BaseResource {

  private static final Logger log = Logger.getLogger(EntityRequestResource.class.getName());

  @PUT
  public Response updateEntity(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo,
      @Context ContextResolver<ODataProducer> producerResolver,
      @Context SecurityContext securityContext,
      @PathParam("entitySetName") String entitySetName,
      @PathParam("id") String id,
      InputStream payload) throws Exception {

    log.info(String.format("updateEntity(%s,%s)", entitySetName, id));

    ODataProducer producer = producerResolver.getContext(ODataProducer.class);

    // is this a new media resource?
    // check for HasStream
    EdmEntitySet entitySet = producer.getMetadata().findEdmEntitySet(entitySetName);
    if (entitySet == null) {
      throw new NotFoundException();
    }

    OEntityKey entityKey = OEntityKey.parse(id);

    ODataContext odataContext = ODataContextImpl.builder()
        .aspect(httpHeaders)
        .aspect(securityContext)
        .aspect(producer)
        .aspect(entitySet)
        .aspect(uriInfo)
        .aspect(entityKey)
        .build();

    if (Boolean.TRUE.equals(entitySet.getType().getHasStream())) { // getHasStream can return null
      // yes it is!
      return updateMediaLinkEntry(httpHeaders, uriInfo, producer, entitySet, payload, OEntityKey.parse(id), odataContext);
    }

    OEntity entity = this.getRequestEntity(httpHeaders, uriInfo, payload, producer.getMetadata(), entitySetName, OEntityKey.parse(id));
    producer.updateEntity(odataContext, entitySetName, entity);

    // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
    return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
  }

  /**
   * Updates an entity given a String payload.
   * Note: currently this exists because EntitiesRequestResource processBatch needs
   *       a version with a String payload.  It may be possible (desirable?) to
   *       re-write batch handling completely such that it streamed individual batch
   *       items instead of loading the entire batch payload into memory and then
   *       processing the batch items.
   */
  protected Response updateEntity(HttpHeaders httpHeaders, UriInfo uriInfo, SecurityContext securityContext,
      ContextResolver<ODataProducer> producerResolver,
      String entitySetName,
      String id,
      String payload,
      ODataContext odataContext) throws Exception {

    log.info(String.format("updateEntity(%s,%s)", entitySetName, id));

    ODataProducer producer = producerResolver.getContext(ODataProducer.class);

    // is this a new media resource?
    // check for HasStream
    EdmEntitySet entitySet = producer.getMetadata().findEdmEntitySet(entitySetName);
    if (entitySet == null) {
      throw new NotFoundException();
    }

    OEntityKey entityKey = OEntityKey.parse(id);

    if (Boolean.TRUE.equals(entitySet.getType().getHasStream())) { // getHasStream can return null
      // yes it is!
      ByteArrayInputStream inStream = new ByteArrayInputStream(payload.getBytes());
      try {
        return updateMediaLinkEntry(httpHeaders, uriInfo, producer, entitySet, inStream, OEntityKey.parse(id), odataContext);
      } finally {
        inStream.close();
      }
    }

    OEntity entity = this.getRequestEntity(httpHeaders, uriInfo, payload, producer.getMetadata(), entitySetName, OEntityKey.parse(id));
    producer.updateEntity(odataContext, entitySetName, entity);

    // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
    return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
  }

  /**
   * Updates the media resource found in the payload for the media link entry (mle)
   * identified by the given key.
   *
   * @return HTTP 204 No Content response if successful.
   */
  protected Response updateMediaLinkEntry(HttpHeaders httpHeaders,
      UriInfo uriInfo, ODataProducer producer, EdmEntitySet entitySet, InputStream payload, OEntityKey key,
      ODataContext odataContext) throws IOException {

    @SuppressWarnings("unused")
    OEntity mle = super.createOrUpdateMediaLinkEntry(httpHeaders, uriInfo, entitySet, producer, payload, key, odataContext);

    // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
    return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
  }

  @POST
  public Response mergeEntity(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo,
      @Context ContextResolver<ODataProducer> producerResolver,
      @Context SecurityContext securityContext,
      @PathParam("entitySetName") String entitySetName,
      @PathParam("id") String id,
      String payload) {

    log.info(String.format("mergeEntity(%s,%s)", entitySetName, id));

    ODataProducer producer = producerResolver.getContext(ODataProducer.class);

    OEntityKey entityKey = OEntityKey.parse(id);
    ODataContext context = ODataContextImpl.builder().aspect(httpHeaders).aspect(securityContext).aspect(producer).build();

    String method = httpHeaders.getRequestHeaders().getFirst(ODataConstants.Headers.X_HTTP_METHOD);
    if ("MERGE".equals(method)) {
      OEntity entity = this.getRequestEntity(httpHeaders, uriInfo, payload, producer.getMetadata(), entitySetName, entityKey);
      producer.mergeEntity(context, entitySetName, entity);

      // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
      return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
    }

    if ("DELETE".equals(method)) {
      producer.deleteEntity(context, entitySetName, entityKey);

      // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
      return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
    }

    if ("PUT".equals(method)) {
      OEntity entity = this.getRequestEntity(httpHeaders, uriInfo, payload, producer.getMetadata(), entitySetName, OEntityKey.parse(id));
      producer.updateEntity(context, entitySetName, entity);

      // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
      return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
    }

    if (method != null)
      throw new RuntimeException("Expected a tunnelled PUT, MERGE or DELETE");
    else
      throw new MethodNotAllowedException("POST is not allowed for an entity");
  }

  @DELETE
  public Response deleteEntity(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo,
      @Context ContextResolver<ODataProducer> producerResolver,
      @Context SecurityContext securityContext,
      @QueryParam("$format") String format,
      @QueryParam("$callback") String callback,
      @PathParam("entitySetName") String entitySetName,
      @PathParam("id") String id) throws Exception {

    log.info(String.format("deleteEntity(%s,%s)", entitySetName, id));

    ODataProducer producer = producerResolver.getContext(ODataProducer.class);

    // the OData URI scheme makes it impossible to have unique @Paths that refer
    // to functions and entity sets
    if (producer.getMetadata().findEdmFunctionImport(entitySetName) != null) {
      // functions that return collections of entities should support the
      // same set of query options as entity set queries so give them everything.
      return FunctionResource.callFunction(ODataHttpMethod.DELETE, httpHeaders, uriInfo, securityContext, producer, entitySetName, format, callback, null);
    }

    OEntityKey entityKey = OEntityKey.parse(id);

    // is this a new media resource?
    // check for HasStream
    EdmEntitySet entitySet = producer.getMetadata().findEdmEntitySet(entitySetName);
    if (entitySet == null) {
      throw new NotFoundException();
    }

    ODataContext odataContext = ODataContextImpl.builder()
        .aspect(httpHeaders)
        .aspect(securityContext)
        .aspect(producer)
        .aspect(entitySet)
        .aspect(uriInfo)
        .aspect(entityKey)
        .build();

    if (Boolean.TRUE.equals(entitySet.getType().getHasStream())) { // getHasStream can return null
      // yes it is!
      // first, the producer must support OMediaLinkExtension
      OMediaLinkExtension mediaLinkExtension = getMediaLinkExtension(httpHeaders, uriInfo, entitySet, producer, odataContext);

      // get a media link entry from the extension
      OEntity mle = mediaLinkExtension.getMediaLinkEntryForUpdateOrDelete(odataContext, entitySet, entityKey, httpHeaders);
      mediaLinkExtension.deleteStream(odataContext, mle, null /* QueryInfo, may need to get rid of */);
      // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
      return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
    }

    producer.deleteEntity(odataContext, entitySetName, entityKey);

    // TODO: hmmh..isn't this supposed to be HTTP 204 No Content?
    return Response.ok().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
  }

  @GET
  @Produces({ ODataConstants.APPLICATION_ATOM_XML_CHARSET_UTF8, ODataConstants.TEXT_JAVASCRIPT_CHARSET_UTF8, ODataConstants.APPLICATION_JAVASCRIPT_CHARSET_UTF8 })
  public Response getEntity(@Context HttpHeaders httpHeaders,
      @Context UriInfo uriInfo,
      @Context ContextResolver<ODataProducer> producerResolver,
      @Context SecurityContext securityContext,
      @PathParam("entitySetName") String entitySetName,
      @PathParam("id") String id,
      @QueryParam("$format") String format,
      @QueryParam("$callback") String callback,
      @QueryParam("$expand") String expand,
      @QueryParam("$select") String select) {

    ODataProducer producer = producerResolver.getContext(ODataProducer.class);
    return getEntityImpl(httpHeaders, uriInfo, securityContext, producer, entitySetName, id, format, callback, expand, select);
  }

  protected Response getEntityImpl(HttpHeaders httpHeaders, UriInfo uriInfo,
      SecurityContext securityContext,
      ODataProducer producer,
      String entitySetName,
      String id,
      String format,
      String callback,
      String expand,
      String select) {

    EntityQueryInfo query = new EntityQueryInfo(
        null,
        OptionsQueryParser.parseCustomOptions(uriInfo),
        OptionsQueryParser.parseExpand(expand),
        OptionsQueryParser.parseSelect(select));

    log.info(String.format(
        "getEntity(%s,%s,%s,%s)",
        entitySetName,
        id,
        expand,
        select));

    EntityResponse response;
    try {
      response = producer.getEntity(ODataContextImpl.builder().aspect(httpHeaders).aspect(securityContext).aspect(producer).build(),
          entitySetName, OEntityKey.parse(id), query);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Illegal key " + id, e);
    }

    StringWriter sw = new StringWriter();
    FormatWriter<EntityResponse> fw = FormatWriterFactory.getFormatWriter(EntityResponse.class, httpHeaders.getAcceptableMediaTypes(), format, callback);
    fw.write(uriInfo, sw, response);
    String entity = sw.toString();

    return Response.ok(entity, fw.getContentType()).header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
  }

  @Path("{first: \\$}links/{targetNavProp:.+?}{targetId: (\\(.+?\\))?}")
  public LinksRequestResource getLinks(
      @PathParam("entitySetName") String entitySetName,
      @PathParam("id") String id,
      @PathParam("targetNavProp") String targetNavProp,
      @PathParam("targetId") String targetId) {

    OEntityKey targetEntityKey = targetId == null || targetId.isEmpty() ? null : OEntityKey.parse(targetId);

    return new LinksRequestResource(OEntityIds.create(entitySetName, OEntityKey.parse(id)), targetNavProp, targetEntityKey);
  }

  @Path("{first: \\$}value")
  public ValueRequestResource getValue() {
    return new ValueRequestResource();
  }

  @Path("{navProp: .+}")
  public PropertyRequestResource getNavProperty() {
    return new PropertyRequestResource();
  }

  @Path("{navProp: .+?}{optionalParens: ((\\(\\)))}")
  public PropertyRequestResource getSimpleNavProperty() {
    return new PropertyRequestResource();
  }

}
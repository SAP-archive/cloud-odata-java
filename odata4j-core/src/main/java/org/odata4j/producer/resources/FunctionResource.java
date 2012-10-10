package org.odata4j.producer.resources;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataHttpMethod;
import org.odata4j.core.ODataVersion;
import org.odata4j.core.OEntity;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OFunctionParameters;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmFunctionParameter;
import org.odata4j.exceptions.MethodNotAllowedException;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.CollectionResponse;
import org.odata4j.producer.ComplexObjectResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataContextImpl;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.PropertyResponse;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.SimpleResponse;

/**
 * Handles function calls.
 *
 * <p>Unfortunately the OData URI scheme makes it
 * impossible to differentiate a function call "resource" from an EntitySet.
 * So, we hack:  EntitiesRequestResource delegates to this class if it determines
 * that a function is being referenced.
 *
 * <ul>TODO:
 *   <li>function parameter facets (required, value ranges, etc).  For now, all
 *    validation is up to the function handler in the producer.
 *   <li>non-simple function parameter types
 *   <li>make sure this works for GET and POST
 */
public class FunctionResource extends BaseResource {

  /**
   * Handles function call resource access by gathering function call info from
   * the request and delegating to the producer.
   */
  @SuppressWarnings("rawtypes")
  public static Response callFunction(
      ODataHttpMethod callingMethod,
      HttpHeaders httpHeaders,
      UriInfo uriInfo,
      SecurityContext securityContext,
      ODataProducer producer,
      String functionName,
      String format,
      String callback,
      QueryInfo queryInfo) throws Exception {

    // do we have this function?
    EdmFunctionImport function = producer.getMetadata().findEdmFunctionImport(functionName);
    if (function == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String expectedHttpMethodString = function.getHttpMethod();
    if (expectedHttpMethodString != null && !"".equals(expectedHttpMethodString)) {
      ODataHttpMethod expectedHttpMethod = ODataHttpMethod.fromString(expectedHttpMethodString);
      if (expectedHttpMethod != callingMethod) {
        throw new MethodNotAllowedException();
      }
    }

    BaseResponse response = producer.callFunction(ODataContextImpl.builder().aspect(httpHeaders).aspect(securityContext).aspect(producer).build(),
        function, getFunctionParameters(function, queryInfo.customOptions), queryInfo);

    if (response == null) {
      return Response.status(Status.NO_CONTENT).build();
    }

    ODataVersion version = ODataConstants.DATA_SERVICE_VERSION;

    StringWriter sw = new StringWriter();
    FormatWriter<?> fwBase;

    // hmmh...we are missing an abstraction somewhere..
    if (response instanceof ComplexObjectResponse) {
      FormatWriter<ComplexObjectResponse> fw =
          FormatWriterFactory.getFormatWriter(
              ComplexObjectResponse.class,
              httpHeaders.getAcceptableMediaTypes(),
              format,
              callback);

      fw.write(uriInfo, sw, (ComplexObjectResponse) response);
      fwBase = fw;
    } else if (response instanceof CollectionResponse) {
      CollectionResponse<?> collectionResponse = (CollectionResponse<?>) response;

      if (collectionResponse.getCollection().getType() instanceof EdmEntityType) {
        FormatWriter<EntitiesResponse> fw = FormatWriterFactory.getFormatWriter(
            EntitiesResponse.class,
            httpHeaders.getAcceptableMediaTypes(),
            format,
            callback);

        // collection of entities.
        // Does anyone else see this in the v2 spec?  I sure don't.  This seems
        // reasonable though given that inlinecount and skip tokens might be included...
        ArrayList<OEntity> entities = new ArrayList<OEntity>(collectionResponse.getCollection().size());
        Iterator iter = collectionResponse.getCollection().iterator();
        while (iter.hasNext()) {
          entities.add((OEntity) iter.next());
        }
        EntitiesResponse er = Responses.entities(entities,
            collectionResponse.getEntitySet(),
            collectionResponse.getInlineCount(),
            collectionResponse.getSkipToken());
        fw.write(uriInfo, sw, er);
        fwBase = fw;
      } else {
        // non-entities
        FormatWriter<CollectionResponse> fw = FormatWriterFactory.getFormatWriter(
            CollectionResponse.class,
            httpHeaders.getAcceptableMediaTypes(),
            format,
            callback);
        fw.write(uriInfo, sw, collectionResponse);
        fwBase = fw;
      }
    } else if (response instanceof EntitiesResponse) {
      FormatWriter<EntitiesResponse> fw = FormatWriterFactory.getFormatWriter(
          EntitiesResponse.class,
          httpHeaders.getAcceptableMediaTypes(),
          format,
          callback);

      fw.write(uriInfo, sw, (EntitiesResponse) response);
      fwBase = fw;
    } else if (response instanceof PropertyResponse) {
      FormatWriter<PropertyResponse> fw =
          FormatWriterFactory.getFormatWriter(
              PropertyResponse.class,
              httpHeaders.getAcceptableMediaTypes(),
              format,
              callback);

      fw.write(uriInfo, sw, (PropertyResponse) response);
      fwBase = fw;
    } else if (response instanceof SimpleResponse) {
      FormatWriter<SimpleResponse> fw =
          FormatWriterFactory.getFormatWriter(
              SimpleResponse.class,
              httpHeaders.getAcceptableMediaTypes(),
              format,
              callback);

      fw.write(uriInfo, sw, (SimpleResponse) response);
      fwBase = fw;
    } else if (response instanceof EntityResponse) {
      FormatWriter<EntityResponse> fw =
          FormatWriterFactory.getFormatWriter(
              EntityResponse.class,
              httpHeaders.getAcceptableMediaTypes(),
              format,
              callback);

      fw.write(uriInfo, sw, (EntityResponse) response);
      fwBase = fw;
    } else {
      // TODO add in other response types.
      throw new NotImplementedException("Unknown BaseResponse type: " + response.getClass().getName());
    }

    String entity = sw.toString();
    return Response.ok(entity, fwBase.getContentType())
        .header(ODataConstants.Headers.DATA_SERVICE_VERSION, version.asString)
        .build();
  }

  /**
   * Takes a Map<String,String> filled with the request URIs custom parameters and
   * turns them into a map of strongly-typed OFunctionParameter objects.
   *
   * @param function  the function being called
   * @param opts  request URI custom parameters
   */
  private static Map<String, OFunctionParameter> getFunctionParameters(EdmFunctionImport function, Map<String, String> opts) {
    Map<String, OFunctionParameter> m = new HashMap<String, OFunctionParameter>();
    for (EdmFunctionParameter p : function.getParameters()) {
      String val = opts.get(p.getName());
      m.put(p.getName(), val == null ? null : OFunctionParameters.parse(p.getName(), p.getType(), val));
    }
    return m;
  }

}
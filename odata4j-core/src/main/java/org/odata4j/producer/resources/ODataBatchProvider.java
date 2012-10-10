package org.odata4j.producer.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(ODataBatchProvider.MULTIPART_MIXED)
public class ODataBatchProvider implements MessageBodyReader<List<BatchBodyPart>> {

  @Context
  HttpHeaders httpHeaders;
  @Context
  UriInfo uriInfo;

  public enum HTTP_METHOD {
    GET,
    PUT,
    POST,
    MERGE,
    DELETE
  };

  public static final String MULTIPART_MIXED = "multipart/mixed";

  public static String createResponseBodyPart(BatchBodyPart bodyPart, Response response) {
    final String CONTENT_ID = "Content-ID";
    StringBuilder body = new StringBuilder("\nHTTP/1.1 ");

    Status status = Response.Status.fromStatusCode(response.getStatus());
    body.append(status.getStatusCode());
    body.append(' ');
    body.append(status.getReasonPhrase());
    body.append('\n');

    if (bodyPart.getHeaders().containsKey(CONTENT_ID)) {
      body.append(CONTENT_ID);
      body.append(": ");
      body.append(bodyPart.getHeaders().getFirst(CONTENT_ID));
      body.append('\n');
    }

    for (String key : response.getMetadata().keySet()) {
      body.append(key).append(": ");
      for (Object value : response.getMetadata().get(key)) {
        body.append(value).append(";");
      }
      body.append('\n');
    }

    body.append('\n');
    if (response.getEntity() != null) {
      body.append(response.getEntity().toString());
    }

    body.append('\n');
    return body.toString();
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] antns, MediaType mt) {
    if (genericType instanceof ParameterizedType) {
      for (Type gType : ((ParameterizedType) genericType).getActualTypeArguments()) {
        if (gType == BatchBodyPart.class && type == List.class) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public List<BatchBodyPart> readFrom(
      Class<List<BatchBodyPart>> type,
      Type genericType,
      Annotation[] antns,
      MediaType mt,
      MultivaluedMap<String, String> mm,
      InputStream inputStream) throws IOException, WebApplicationException {
    List<BatchBodyPart> parts = new ArrayList<BatchBodyPart>();

    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
    final String ContentType = "content-type:";
    String currentLine = "";

    while ((currentLine = br.readLine()) != null) {
      if (currentLine.toLowerCase().startsWith(ContentType)) {
        String ctype = currentLine.substring(ContentType.length()).trim();
        if (ctype.toLowerCase().startsWith("application/http")) {
          parts.add(parseBodyPart(br));
        }
      }
    }

    br.close();
    return parts;
  }

  private BatchBodyPart parseBodyPart(BufferedReader br) throws IOException {
    BatchBodyPart block = new BatchBodyPart(httpHeaders, uriInfo);
    final int SKIP_CONTENT_BEGIN = 2;

    String line = "";
    while ((line = br.readLine()) != null) {
      if (line.equals("")) {
        continue;
      }
      if (line.startsWith("--")) {
        return validateBodyPart(block);
      }

      if (block.getHttpMethod() == null) {
        for (HTTP_METHOD method : HTTP_METHOD.values()) {
          if (line.startsWith(method.name())) {
            String uri = line.substring(method.name().length() + 1);
            int lastIdx = uri.lastIndexOf(" ");
            if (lastIdx != -1) {
              uri = uri.substring(0, lastIdx);
            }

            block.setHttpMethod(method);
            block.setUri(uri);
            break;
          }
        }
      } else {
        Integer idx = line.indexOf(':');
        String key = line.substring(0, idx);
        String value = line.substring(idx + 1).trim();
        block.getHeaders().putSingle(key, value);

        if (key.toLowerCase().equals("content-length")) {
          int capacity = Integer.parseInt(value) - SKIP_CONTENT_BEGIN;
          char[] buf = new char[capacity];
          int offset = 0;

          br.skip(SKIP_CONTENT_BEGIN);

          while (offset != capacity) {
            offset += br.read(buf, offset, capacity - offset);
          }

          block.setEntity(new String(buf));
          return validateBodyPart(block);
        }
      }
    }

    throw new IllegalArgumentException("Cann't parse block");
  }

  private static BatchBodyPart validateBodyPart(BatchBodyPart block) {
    if (block.getHttpMethod() == null ? "" == null : block.getHttpMethod().toString().equals("")) {
      throw new IllegalArgumentException("Block HTTP METHOD is empty.");
    }

    if (block.getUri() == null ? "" == null : block.getUri().equals("")) {
      throw new IllegalArgumentException("Block URI is empty.");
    }

    return block;
  }

}

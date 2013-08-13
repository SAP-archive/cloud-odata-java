package com.sap.core.odata.core.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.client.batch.BatchSingleResponse;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class BatchResponseParser {

  private static final String LF = "\n";
  private static final String REG_EX_OPTIONAL_WHITESPACE = "\\s?";
  private static final String REG_EX_ZERO_OR_MORE_WHITESPACES = "\\s*";
  private static final String ANY_CHARACTERS = ".*";

  private static final Pattern REG_EX_BLANK_LINE = Pattern.compile("(|" + REG_EX_ZERO_OR_MORE_WHITESPACES + ")");
  private static final Pattern REG_EX_HEADER = Pattern.compile("([a-zA-Z\\-]+):" + REG_EX_OPTIONAL_WHITESPACE + "(.*)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_VERSION = Pattern.compile("(?:HTTP/[0-9]\\.[0-9])");
  private static final Pattern REG_EX_ANY_BOUNDARY_STRING = Pattern.compile("--" + ANY_CHARACTERS + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_STATUS_LINE = Pattern.compile(REG_EX_VERSION + "\\s" + "([0-9]{3})\\s([\\S ]+)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_BOUNDARY_PARAMETER = Pattern.compile(REG_EX_OPTIONAL_WHITESPACE + "boundary=(\".*\"|.*)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_CONTENT_TYPE = Pattern.compile(REG_EX_OPTIONAL_WHITESPACE + HttpContentType.MULTIPART_MIXED);

  private static final String REG_EX_BOUNDARY = "([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+ \\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\""; // See RFC 2046

  private String contentTypeMime;
  private String boundary;
  private String currentContentId;
  private int currentLineNumber = 0;

  public BatchResponseParser(final String contentType) {
    contentTypeMime = contentType;
  }

  public List<BatchSingleResponse> parse(final InputStream in) throws BatchException {
    Scanner scanner = new Scanner(in, BatchHelper.DEFAULT_ENCODING).useDelimiter(LF);
    List<BatchSingleResponse> responseList;
    try {
      responseList = parseBatchResponse(scanner);
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      scanner.close();
      try {
        in.close();
      } catch (IOException e) {
        throw new ODataRuntimeException(e);
      }
    }
    return responseList;
  }

  private List<BatchSingleResponse> parseBatchResponse(final Scanner scanner) throws BatchException {
    List<BatchSingleResponse> responses = new ArrayList<BatchSingleResponse>();
    if (contentTypeMime != null) {
      boundary = getBoundary(contentTypeMime);
      parsePreamble(scanner);
      String closeDelimiter = "--" + boundary + "--" + REG_EX_ZERO_OR_MORE_WHITESPACES;
      while (scanner.hasNext() && !scanner.hasNext(closeDelimiter)) {
        responses.addAll(parseMultipart(scanner, boundary, false));
        //parseNewLine(scanner);
      }
      if (scanner.hasNext(closeDelimiter)) {
        scanner.next(closeDelimiter);
        currentLineNumber++;
      } else {
        throw new BatchException(BatchException.MISSING_CLOSE_DELIMITER.addContent(currentLineNumber));
      }
    } else {
      throw new BatchException(BatchException.MISSING_CONTENT_TYPE);
    }
    return responses;

  }

  //The method parses additional information prior to the first boundary delimiter line
  private void parsePreamble(final Scanner scanner) {
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      scanner.next();
      currentLineNumber++;
    }
  }

  private List<BatchSingleResponse> parseMultipart(final Scanner scanner, final String boundary, final boolean isChangeSet) throws BatchException {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    List<BatchSingleResponse> responses = new ArrayList<BatchSingleResponse>();
    if (scanner.hasNext("--" + boundary + REG_EX_ZERO_OR_MORE_WHITESPACES)) {
      scanner.next();
      currentLineNumber++;
      mimeHeaders = parseMimeHeaders(scanner);
      currentContentId = mimeHeaders.get(BatchHelper.HTTP_CONTENT_ID.toLowerCase(Locale.ENGLISH));

      String contentType = mimeHeaders.get(HttpHeaders.CONTENT_TYPE.toLowerCase(Locale.ENGLISH));
      if (contentType == null) {
        throw new BatchException(BatchException.MISSING_CONTENT_TYPE);
      }
      if (isChangeSet) {
        if (HttpContentType.APPLICATION_HTTP.equalsIgnoreCase(contentType)) {
          validateEncoding(mimeHeaders.get(BatchHelper.HTTP_CONTENT_TRANSFER_ENCODING.toLowerCase(Locale.ENGLISH)));
          parseNewLine(scanner);// mandatory
          BatchSingleResponseImpl response = parseResponse(scanner, isChangeSet);
          responses.add(response);
        } else {
          throw new BatchException(BatchException.INVALID_CONTENT_TYPE.addContent(HttpContentType.APPLICATION_HTTP));
        }
      } else {
        if (HttpContentType.APPLICATION_HTTP.equalsIgnoreCase(contentType)) {
          validateEncoding(mimeHeaders.get(BatchHelper.HTTP_CONTENT_TRANSFER_ENCODING.toLowerCase(Locale.ENGLISH)));
          parseNewLine(scanner);// mandatory
          BatchSingleResponseImpl response = parseResponse(scanner, isChangeSet);
          responses.add(response);
        } else if (contentType.matches(REG_EX_OPTIONAL_WHITESPACE + HttpContentType.MULTIPART_MIXED + ANY_CHARACTERS)) {
          String changeSetBoundary = getBoundary(contentType);
          if (boundary.equals(changeSetBoundary)) {
            throw new BatchException(BatchException.INVALID_CHANGESET_BOUNDARY.addContent(currentLineNumber));
          }
          parseNewLine(scanner);// mandatory
          Pattern changeSetCloseDelimiter = Pattern.compile("--" + changeSetBoundary + "--" + REG_EX_ZERO_OR_MORE_WHITESPACES);
          while (!scanner.hasNext(changeSetCloseDelimiter)) {
            responses.addAll(parseMultipart(scanner, changeSetBoundary, true));
          }
          scanner.next(changeSetCloseDelimiter);
          currentLineNumber++;
          parseNewLine(scanner);
        } else {
          throw new BatchException(BatchException.INVALID_CONTENT_TYPE.addContent(HttpContentType.MULTIPART_MIXED + " or " + HttpContentType.APPLICATION_HTTP));
        }
      }
    } else if (scanner.hasNext(boundary + REG_EX_ZERO_OR_MORE_WHITESPACES)) {
      currentLineNumber++;
      throw new BatchException(BatchException.INVALID_BOUNDARY_DELIMITER.addContent(currentLineNumber));
    } else if (scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      currentLineNumber++;
      throw new BatchException(BatchException.NO_MATCH_WITH_BOUNDARY_STRING.addContent(boundary).addContent(currentLineNumber));
    } else {
      currentLineNumber++;
      throw new BatchException(BatchException.MISSING_BOUNDARY_DELIMITER.addContent(currentLineNumber));
    }
    return responses;

  }

  private BatchSingleResponseImpl parseResponse(final Scanner scanner, final boolean isChangeSet) throws BatchException {
    BatchSingleResponseImpl response = new BatchSingleResponseImpl();
    if (scanner.hasNext(REG_EX_STATUS_LINE)) {
      scanner.next(REG_EX_STATUS_LINE);
      currentLineNumber++;
      String statusCode = null;
      String statusInfo = null;
      MatchResult result = scanner.match();
      if (result.groupCount() == 2) {
        statusCode = result.group(1);
        statusInfo = result.group(2);
      } else {
        currentLineNumber++;
        throw new BatchException(BatchException.INVALID_STATUS_LINE.addContent(scanner.next()).addContent(currentLineNumber));
      }

      Map<String, String> headers = parseResponseHeaders(scanner);
      parseNewLine(scanner);
      String contentLengthHeader = getHeaderValue(headers, HttpHeaders.CONTENT_LENGTH);
      String body = (contentLengthHeader != null) ? parseBody(scanner, Integer.parseInt(contentLengthHeader)) : parseBody(scanner);
      response.setStatusCode(statusCode);
      response.setStatusInfo(statusInfo);
      response.setHeaders(headers);
      response.setContentId(currentContentId);
      response.setBody(body);
    } else {
      currentLineNumber++;
      throw new BatchException(BatchException.INVALID_STATUS_LINE.addContent(scanner.next()).addContent(currentLineNumber));
    }
    return response;
  }

  private void validateEncoding(final String encoding) throws BatchException {
    if (!BatchHelper.BINARY_ENCODING.equalsIgnoreCase(encoding)) {
      throw new BatchException(BatchException.INVALID_CONTENT_TRANSFER_ENCODING);
    }
  }

  private Map<String, String> parseMimeHeaders(final Scanner scanner) throws BatchException {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !(scanner.hasNext(REG_EX_BLANK_LINE))) {
      if (scanner.hasNext(REG_EX_HEADER)) {
        scanner.next(REG_EX_HEADER);
        currentLineNumber++;
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim().toLowerCase(Locale.ENGLISH);
          String headerValue = result.group(2).trim();
          headers.put(headerName, headerValue);
        }
      } else {
        throw new BatchException(BatchException.INVALID_HEADER.addContent(scanner.next()));
      }
    }
    return headers;
  }

  private Map<String, String> parseResponseHeaders(final Scanner scanner) throws BatchException {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_BLANK_LINE)) {
      if (scanner.hasNext(REG_EX_HEADER)) {
        scanner.next(REG_EX_HEADER);
        currentLineNumber++;
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          if (BatchHelper.HTTP_CONTENT_ID.equalsIgnoreCase(headerName)) {
            if (currentContentId == null) {
              currentContentId = headerValue;
            }
          } else {
            headers.put(headerName, headerValue);
          }
        }
      } else {
        currentLineNumber++;
        throw new BatchException(BatchException.INVALID_HEADER.addContent(scanner.next()).addContent(currentLineNumber));
      }
    }
    return headers;
  }

  private String getHeaderValue(final Map<String, String> headers, final String headerName) {
    for (Map.Entry<String, String> header : headers.entrySet()) {
      if (headerName.equalsIgnoreCase(header.getKey())) {
        return header.getValue();
      }
    }
    return null;
  }

  private String parseBody(final Scanner scanner) {
    String body = null;
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      if (!scanner.hasNext(REG_EX_ZERO_OR_MORE_WHITESPACES)) {
        String nextLine = scanner.next();
        if (body == null) {
          body = nextLine;
        } else {
          body = body + LF + nextLine;
        }
      } else {
        scanner.next();
      }
      currentLineNumber++;
    }
    return body;
  }

  private String parseBody(final Scanner scanner, final int contentLength) {
    String body = null;
    int length = 0;
    while (scanner.hasNext() && length < contentLength) {
      if (!scanner.hasNext(REG_EX_ZERO_OR_MORE_WHITESPACES)) {
        String nextLine = scanner.next();
        length += BatchHelper.getBytes(nextLine).length;
        if (body == null) {
          body = nextLine;
        } else {
          body = body + LF + nextLine;
        }
      } else {
        scanner.next();
      }
      currentLineNumber++;
      if (scanner.hasNext() && scanner.hasNext(REG_EX_BLANK_LINE)) {
        scanner.next();
        currentLineNumber++;
      }
    }
    return body;
  }

  private String getBoundary(final String contentType) throws BatchException {
    Scanner contentTypeScanner = new Scanner(contentType).useDelimiter(";\\s?");
    if (contentTypeScanner.hasNext(REG_EX_CONTENT_TYPE)) {
      contentTypeScanner.next(REG_EX_CONTENT_TYPE);
    } else {
      contentTypeScanner.close();
      throw new BatchException(BatchException.INVALID_CONTENT_TYPE.addContent(HttpContentType.MULTIPART_MIXED));
    }
    if (contentTypeScanner.hasNext(REG_EX_BOUNDARY_PARAMETER)) {
      contentTypeScanner.next(REG_EX_BOUNDARY_PARAMETER);
      MatchResult result = contentTypeScanner.match();
      contentTypeScanner.close();
      if (result.groupCount() == 1 && result.group(1).trim().matches(REG_EX_BOUNDARY)) {
        return trimQuota(result.group(1).trim());
      } else {
        throw new BatchException(BatchException.INVALID_BOUNDARY);
      }
    } else {
      contentTypeScanner.close();
      throw new BatchException(BatchException.MISSING_PARAMETER_IN_CONTENT_TYPE);
    }
  }

  private void parseNewLine(final Scanner scanner) throws BatchException {
    if (scanner.hasNext() && scanner.hasNext(REG_EX_BLANK_LINE)) {
      scanner.next();
      currentLineNumber++;
    } else {
      currentLineNumber++;
      if (scanner.hasNext()) {
        throw new BatchException(BatchException.MISSING_BLANK_LINE.addContent(scanner.next()).addContent(currentLineNumber));
      } else {
        throw new BatchException(BatchException.TRUNCATED_BODY.addContent(currentLineNumber));

      }
    }
  }

  private String trimQuota(String boundary) {
    if (boundary.matches("\".*\"")) {
      boundary = boundary.replace("\"", "");
    }
    boundary = boundary.replaceAll("\\)", "\\\\)");
    boundary = boundary.replaceAll("\\(", "\\\\(");
    boundary = boundary.replaceAll("\\?", "\\\\?");
    boundary = boundary.replaceAll("\\+", "\\\\+");
    return boundary;
  }

}

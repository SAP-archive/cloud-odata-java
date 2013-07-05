package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.sap.core.odata.api.batch.BatchException;

/**
 * @author SAP AG
 */
public class AcceptParser {

  private static final String BAD_REQUEST = "400";
  private static final String ALL = "*";
  private static final String REG_EX_QUALITY_FACTOR = "q=((?:1\\.0{0,3})|(?:0\\.[0-9]{0,2}[1-9]))";
  private static final String REG_EX_OPTIONAL_WHITESPACE = "\\s?";
  private static final Pattern REG_EX_ACCEPT = Pattern.compile("([a-z\\*]+/[a-z0-9\\+\\*\\-=;\\s]+)");
  private static final Pattern REG_EX_ACCEPT_WITH_Q_FACTOR = Pattern.compile(REG_EX_ACCEPT + "(?:;" + REG_EX_OPTIONAL_WHITESPACE + REG_EX_QUALITY_FACTOR + ")?");
  private static final Pattern REG_EX_ACCEPT_LANGUAGES = Pattern.compile("((?:(?:[a-z]{1,8})|(?:\\*))\\-?(?:[a-zA-Z]{1,8})?)");
  private static final Pattern REG_EX_ACCEPT_LANGUAGES_WITH_Q_FACTOR = Pattern.compile(REG_EX_ACCEPT_LANGUAGES + "(?:;" + REG_EX_OPTIONAL_WHITESPACE + REG_EX_QUALITY_FACTOR + ")?");

  private static final double QUALITY_PARAM_FACTOR = 0.001;

  public static List<String> parseAcceptHeaders(final String headerValue) throws BatchException {
    TreeSet<Accept> acceptTree = getAcceptTree();
    List<String> acceptHeaders = new ArrayList<String>();
    Scanner acceptHeaderScanner = new Scanner(headerValue).useDelimiter(",\\s?");
    while (acceptHeaderScanner.hasNext()) {
      if (acceptHeaderScanner.hasNext(REG_EX_ACCEPT_WITH_Q_FACTOR)) {
        acceptHeaderScanner.next(REG_EX_ACCEPT_WITH_Q_FACTOR);
        MatchResult result = acceptHeaderScanner.match();
        if (result.groupCount() == 2) {
          String acceptHeaderValue = result.group(1);
          double qualityFactor = result.group(2) != null ? Double.parseDouble(result.group(2)) : 1d;
          qualityFactor = getQualityFactor(acceptHeaderValue, qualityFactor);
          Accept acceptHeader = new Accept().setQuality(qualityFactor).setValue(acceptHeaderValue);
          acceptTree.add(acceptHeader);
        } else {
          String header = acceptHeaderScanner.next();
          acceptHeaderScanner.close();
          throw new BatchException(BatchException.INVALID_ACCEPT_HEADER.addContent(header), BAD_REQUEST);
        }
      } else {
        String header = acceptHeaderScanner.next();
        acceptHeaderScanner.close();
        throw new BatchException(BatchException.INVALID_ACCEPT_HEADER.addContent(header), BAD_REQUEST);
      }
    }
    for (Accept accept : acceptTree) {
      acceptHeaders.add(accept.getValue());
    }
    acceptHeaderScanner.close();
    return acceptHeaders;
  }

  private static double getQualityFactor(final String acceptHeaderValue, double qualityFactor) {
    int paramNumber = 0;
    double typeFactor = 0.0;
    double subtypeFactor = 0.0;
    String[] mediaRange = acceptHeaderValue.split("(?=[^;]+);");
    String[] mediaTypes = mediaRange[0].split("/");
    if (mediaTypes.length == 2) {
      String type = mediaTypes[0];
      String subtype = mediaTypes[1];
      if (!ALL.equals(type)) {
        typeFactor = 0.001;
      }
      if (!ALL.equals(subtype)) {
        subtypeFactor = 0.001;
      }
    }
    if (mediaRange.length == 2) {
      String[] parameters = mediaRange[1].split(";\\s?");
      paramNumber = parameters.length;
    }
    qualityFactor = qualityFactor + paramNumber * QUALITY_PARAM_FACTOR + typeFactor + subtypeFactor;
    return qualityFactor;
  }

  public static List<Locale> parseAcceptableLanguages(final String headerValue) throws BatchException {
    List<Locale> acceptLanguages = new LinkedList<Locale>();
    TreeSet<Accept> acceptTree = getAcceptTree();
    Scanner acceptLanguageScanner = new Scanner(headerValue).useDelimiter(",\\s?");
    while (acceptLanguageScanner.hasNext()) {
      if (acceptLanguageScanner.hasNext(REG_EX_ACCEPT_LANGUAGES_WITH_Q_FACTOR)) {
        acceptLanguageScanner.next(REG_EX_ACCEPT_LANGUAGES_WITH_Q_FACTOR);
        MatchResult result = acceptLanguageScanner.match();
        if (result.groupCount() == 2) {
          String languagerange = result.group(1);
          double qualityFactor = result.group(2) != null ? Double.parseDouble(result.group(2)) : 1d;
          acceptTree.add(new Accept().setQuality(qualityFactor).setValue(languagerange));
        } else {
          String acceptLanguage = acceptLanguageScanner.next();
          acceptLanguageScanner.close();
          throw new BatchException(BatchException.INVALID_ACCEPT_LANGUAGE_HEADER.addContent(acceptLanguage), BAD_REQUEST);
        }
      } else {
        String acceptLanguage = acceptLanguageScanner.next();
        acceptLanguageScanner.close();
        throw new BatchException(BatchException.INVALID_ACCEPT_LANGUAGE_HEADER.addContent(acceptLanguage), BAD_REQUEST);
      }
    }
    for (Accept accept : acceptTree) {
      String[] p = accept.getValue().split("-");
      String language = p[0];
      String country = "";
      if (p.length == 2) {
        country = p[p.length - 1];
      }
      Locale locale = new Locale(language, country);
      acceptLanguages.add(locale);
    }
    acceptLanguageScanner.close();
    return acceptLanguages;
  }

  private static TreeSet<Accept> getAcceptTree() {
    TreeSet<Accept> treeSet = new TreeSet<Accept>(new Comparator<Accept>() {
      @Override
      public int compare(final Accept o1, final Accept o2) {
        if (o1.getQuality() <= o2.getQuality()) {
          return 1;
        } else {
          return -1;
        }
      }
    });
    return treeSet;
  }

  private static class Accept {
    private double quality;
    private String value;

    public String getValue() {
      return value;
    }

    public Accept setValue(final String value) {
      this.value = value;
      return this;
    }

    public double getQuality() {
      return quality;
    }

    public Accept setQuality(final double quality) {
      this.quality = quality;
      return this;
    }

  }
}

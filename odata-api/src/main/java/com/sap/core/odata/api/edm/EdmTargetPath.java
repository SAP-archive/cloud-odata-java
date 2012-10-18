package com.sap.core.odata.api.edm;


public enum EdmTargetPath {

  AUTHOR_NAME("SyndicationAuthorName"), AUTHOR_EMAIL("SyndicationAuthorEmail"), AUTHOR_URI("SyndicationAuthorUri"), PUBLISHED("SyndicationPublished"), RIGHTS("SyndicationRights"), TITLE("SyndicationTitle"), UPDATED("SyndicationUpdated"), CONTRIBTR_NAME(
      "SyndicationContributorName"), CONTRIBTR_EMAIL("SyndicationContributorEmail"), CONTRIBTR_URI("SyndicationContributorUri"), SOURCE("SyndicationSource"), SUMMARY("SyndicationSummary");

  private final String symbolString;

  private EdmTargetPath(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmTargetPath fromSymbolString(String symbolString) {
    for (EdmTargetPath m : EdmTargetPath.values()) {
      if (m.getSymbolString().equals(symbolString))
        return m;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }

}

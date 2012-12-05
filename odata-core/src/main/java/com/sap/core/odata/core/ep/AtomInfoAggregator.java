package com.sap.core.odata.core.ep;

import java.util.HashMap;

import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTargetPath;

class AtomInfoAggregator {

  private HashMap<String, String> name2ETagValue = new HashMap<String, String>();

  private String title;
  private String updated;
  private String summary;
  private String authorEmail;
  private String authorName;
  private String authorUri;
  private String contributorEmail;
  private String contributorName;
  private String contributorUri;
  private String published;
  private String rights;
  private String source;

  private String updatedPropertyName;

  public AtomInfoAggregator() {
  }
  
  

  public void addInfo(EdmProperty edmProperty, String value) throws EdmException {
    addTargetPathInfo(edmProperty, value);
    addEtagInfo(edmProperty, value);
  }

  private void addEtagInfo(EdmProperty edmProperty, String value) throws EdmException {
    if (edmProperty.getFacets() != null && edmProperty.getFacets().getConcurrencyMode() == EdmConcurrencyMode.Fixed) {
      this.name2ETagValue.put(edmProperty.getName(), value);
    }
  }

  private void addTargetPathInfo(EdmProperty property, String valueAsString) throws EdmException {
    EdmCustomizableFeedMappings customizableFeedMappings = property.getCustomizableFeedMappings();
    if (customizableFeedMappings != null) {
      String targetPath = customizableFeedMappings.getFcTargetPath();
      if (EdmTargetPath.SYNDICATION_TITLE.equals(targetPath)) {
        this.title = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_UPDATED.equals(targetPath)) {
        this.updated = valueAsString;
        this.updatedPropertyName = property.getName();
      } else if (EdmTargetPath.SYNDICATION_SUMMARY.equals(targetPath)) {
        this.summary = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_SOURCE.equals(targetPath)) {
        this.source = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_RIGHTS.equals(targetPath)) {
        this.rights = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_PUBLISHED.equals(targetPath)) {
        this.published = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_CONTRIBUTORURI.equals(targetPath)) {
        this.contributorUri = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_CONTRIBUTORNAME.equals(targetPath)) {
        this.contributorName = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_CONTRIBUTOREMAIL.equals(targetPath)) {
        this.contributorEmail = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_AUTHORURI.equals(targetPath)) {
        this.authorUri = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_AUTHORNAME.equals(targetPath)) {
        this.authorName = valueAsString;
      } else if (EdmTargetPath.SYNDICATION_AUTHOREMAIL.equals(targetPath)) {
        this.authorEmail = valueAsString;
      }
    }
  }

  public String getTitle() {
    return title;
  }

  public String getUpdated() {
    return updated;
  }

  public String getSummary() {
    return summary;
  }

  public String getAuthorEmail() {
    return authorEmail;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getAuthorUri() {
    return authorUri;
  }

  public String getContributorEmail() {
    return contributorEmail;
  }

  public String getContributorName() {
    return contributorName;
  }

  public String getContributorUri() {
    return contributorUri;
  }

  public String getPublished() {
    return published;
  }

  public String getRights() {
    return rights;
  }

  public String getSource() {
    return source;
  }

  public String getUpdatedPropertyName() {
    return updatedPropertyName;
  }

}

package com.sap.core.odata.api.edm;

/**
 * EdmContentType is used for Feed Customization.
 * It specifies the content type of the value of the property being mapped via a customizable feed mapping.
 * This value can be "text", "html" or "xhtml".
 * 
 * @author SAP AG
 */
public enum EdmContentKind {

  text, html, xhtml;
}
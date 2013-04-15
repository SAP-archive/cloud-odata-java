/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmContentType is used for Feed Customization.
 * <p>It specifies the content type of the value of the property being mapped via a customizable feed mapping.
 * This value can be "text", "html" or "xhtml". 
 * @author SAP AG
 */
public enum EdmContentKind {

  text, html, xhtml;
}
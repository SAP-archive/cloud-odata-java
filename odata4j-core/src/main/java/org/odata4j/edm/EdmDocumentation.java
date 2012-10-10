package org.odata4j.edm;

/**
 * A CSDL Documentation element.
 *
 * <p>The Documentation element in conceptual schema definition language (CSDL) can be used
 * to provide information about an object that is defined in a parent element.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb738641.aspx">[msdn] Documentation Element (CSDL)</a>
 */
public class EdmDocumentation {

  private final String summary;
  private final String longDescription;

  public EdmDocumentation(String summary, String longDescription) {
    this.summary = summary;
    this.longDescription = longDescription;
  }

  /** A brief description of the parent element. */
  public String getSummary() {
    return this.summary;
  }

  /** An extensive description of the parent element. */
  public String getLongDescription() {
    return this.longDescription;
  }

}

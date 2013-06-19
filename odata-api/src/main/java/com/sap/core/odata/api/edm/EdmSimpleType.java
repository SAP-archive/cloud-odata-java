package com.sap.core.odata.api.edm;

/**
 * <p>EdmSimpleType is a primitive type as defined in the Entity Data Model (EDM).</p>
 * <p>There are methods to convert EDM simple types from and to Java objects, respectively.
 * The following Java types are supported:
 * <table frame="hsides" rules="groups">
 * <thead>
 * <tr><th>EDM simple type</th><th>Java types</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>Binary</td><td>byte[], {@link Byte}[]</td></tr>
 * <tr><td>Boolean</td><td>{@link Boolean}</td></tr>
 * <tr><td>Byte</td><td>{@link Short}, {@link Byte}, {@link Integer}, {@link Long}</td></tr>
 * <tr><td>DateTime</td><td>{@link java.util.Calendar}, {@link java.util.Date}, {@link Long}</td></tr>
 * <tr><td>DateTimeOffset</td><td>{@link java.util.Calendar}, {@link java.util.Date}, {@link Long}</td></tr>
 * <tr><td>Decimal</td><td>{@link java.math.BigDecimal}, {@link java.math.BigInteger}, {@link Double}, {@link Float}, {@link Byte}, {@link Short}, {@link Integer}, {@link Long}</td></tr>
 * <tr><td>Double</td><td>{@link Double}, {@link Float}, {@link java.math.BigDecimal}, {@link Byte}, {@link Short}, {@link Integer}, {@link Long}</td></tr>
 * <tr><td>Guid</td><td>{@link java.util.UUID}</td></tr>
 * <tr><td>Int16</td><td>{@link Short}, {@link Byte}, {@link Integer}, {@link Long}</td></tr>
 * <tr><td>Int32</td><td>{@link Integer}, {@link Byte}, {@link Short}, {@link Long}</td></tr>
 * <tr><td>Int64</td><td>{@link Long}, {@link Byte}, {@link Short}, {@link Integer}, {@link java.math.BigInteger}</td></tr>
 * <tr><td>SByte</td><td>{@link Byte}, {@link Short}, {@link Integer}, {@link Long}</td></tr>
 * <tr><td>Single</td><td>{@link Float}, {@link Double}, {@link java.math.BigDecimal}, {@link Byte}, {@link Short}, {@link Integer}, {@link Long}</td></tr>
 * <tr><td>String</td><td>{@link String}</td></tr>
 * <tr><td>Time</td><td>{@link java.util.Calendar}, {@link java.util.Date}, {@link Long}</td></tr>
 * </tbody>
 * </table></p>
 * <p>The first Java type is the default type for the respective EDM simple type.</p>
 * <p>For all EDM simple types, the {@link EdmFacets facet} <code>Nullable</code> is
 * taken into account.
 * For <code>Binary</code>, <code>MaxLength</code> is also applicable.
 * For <code>String</code>, the facets <code>MaxLength</code> and <code>Unicode</code>
 * are also considered.
 * The EDM simple types <code>DateTime</code>, <code>DateTimeOffset</code>, and
 * <code>Time</code> can have a <code>Precision</code> facet.
 * <code>Decimal</code> can have the facets <code>Precision</code> and <code>Scale</code>.</p> 
 * <p>
 * <table frame="box" rules="all">
 * <thead>
 * <tr><th>EDM simple type</th><th>Parsing details</th></tr>
 * </thead>
 * <tbody>
 * <tr><td><b>DateTimeOffset</b></td>
 * <td>
 * When an time string is parsed to an according <code>EdmDateTimeOffset</code> object it is assumed that this time string represents the local time with a timezone set.
 * <br/>
 * As an example, when the following time string <code>"2012-02-29T15:33:00-04:00"</code> is parsed it is assumed that we have the local time ("15:33:00") which is in a timezone with an offset from UTC of "-04:00".
 * Hence the result is a calendar object within the local time (which is "15:33:00") and the according timezone offset ("-04:00") which then results in the UTC time of "19:33:00+00:00" ("15:33:00" - "-04:00" -> "19:33:00 UTC").
 * <br/>
 * As further explanation about our date time handling I reference to the following ISO specification: ISO 8601 - http://en.wikipedia.org/wiki/ISO_8601 and the copied section:
 * Time_offsets_from_UTC - http://en.wikipedia.org/wiki/ISO_8601#Time_offsets_from_UTC
 * <blockquote>>
 * The following times all refer to the same moment: "18:30Z", "22:30+04:00", and "15:00-03:30". Nautical time zone letters are not used with the exception of Z. 
 * To calculate UTC time one has to subtract the offset from the local time, e.g. for "15:00-03:30" do 15:00 - (-03:30) to get 18:30 UTC.
 * </blockquote>
 * <em>The behavior of our ABAP OData Library and Microsoft examples is the same as described above.</em> 
 * </td>
 * </tr>
 * </tbody>
 * </table></p>
 * </p>
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
 */
public interface EdmSimpleType extends EdmType {

  public static final String EDM_NAMESPACE = "Edm";
  public static final String SYSTEM_NAMESPACE = "System";

  /**
   * Checks type compatibility.
   *
   * @param simpleType  the {@link EdmSimpleType} to be tested for compatibility
   * @return <code>true</code> if the provided type is compatible to this type
   */
  public boolean isCompatible(EdmSimpleType simpleType);

  /**
   * Returns the default Java type for this EDM simple type as described in
   * the documentation of {@link EdmSimpleType}.
   * @return the default Java type
   */
  public Class<?> getDefaultType();

  /**
   * Validates literal value.
   *
   * @param value        the literal value
   * @param literalKind  the kind of literal representation of value
   * @param facets       additional constraints for parsing (optional)
   * @return <code>true</code> if the validation is successful
   * @see EdmLiteralKind
   * @see EdmFacets
   */
  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets);

  /**
   * Converts literal representation of value to system data type.
   *
   * @param value        the literal representation of value
   * @param literalKind  the kind of literal representation of value
   * @param facets       additional constraints for parsing (optional)
   * @param returnType   the class of the returned value; it must be one of the
   *                     list in the documentation of {@link EdmSimpleType}
   * @return the value as an instance of the class the parameter <code>returnType</code> indicates
   * @see EdmLiteralKind
   * @see EdmFacets
   */
  public <T> T valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets, Class<T> returnType) throws EdmSimpleTypeException;

  /**
   * <p>Converts system data type to literal representation of value.</p>
   * <p>Returns <code>null</code> if value is <code>null</code>
   * and the facets allow the <code>null</code> value.</p>
   *
   * @param value  the Java value as Object; its type must be one of the list
   *               in the documentation of {@link EdmSimpleType}
   * @param literalKind  the requested kind of literal representation
   * @param facets       additional constraints for formatting (optional)
   * @return literal representation as String
   * @see EdmLiteralKind
   * @see EdmFacets
   */
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets) throws EdmSimpleTypeException;

  /**
   * Converts default literal representation to URI literal representation.
   *
   * @param literal  the literal in default representation
   * @return URI literal representation as String
   */
  public String toUriLiteral(String literal) throws EdmSimpleTypeException;
}
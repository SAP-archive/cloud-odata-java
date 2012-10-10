package org.odata4j.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.Expression;
import org.odata4j.expression.ExpressionParser;
import org.odata4j.expression.LiteralExpression;

/**
 * An immutable entity-key, made up of either a single unnamed-value or multiple named-values.
 * Every entity must have an entity-key.  The entity-key must be unique within the entity-set, and thus defines an entity's identity.  (see {@link OEntityId})
 * <p>An entity-key made up a a single unnamed-value is called a single key.  An entity-key made up of multiple named-values is called a complex key.</p>
 * <p>The string representation of an entity-key is wrapped with parentheses, such as <code>(2)</code>, <code>('foo')</code>  or <code>(a=1,foo='bar')</code>.</p>
 * <p>Entity-keys are equal if their string representations are equal.</p>
 */
public class OEntityKey {

  /**
   * SINGLE or COMPLEX
   */
  public enum KeyType {
    SINGLE,
    COMPLEX
  }

  private final Object[] values;
  private final String keyString;

  private OEntityKey(Object[] values) {
    this.values = values;
    this.keyString = keyString(values);
  }

  /**
   * Creates an entity-key.
   * <ul>
   *   <li><code>OEntityKey.create(2)</code></li>,
   *   <li><code>OEntityKey.create("foo")</code></li>
   *   <li><code>OEntityKey.create("a",1,"foo","bar")</code></li>
   *   <li><code>OEntityKey.create(NamedValues.create("a",1),NamedValues.create("foo","bar"))</code></li>
   * </ul></p>
   *
   * @param values  the key values
   * @return a newly-created entity-key
   */
  @SuppressWarnings("unchecked")
  public static OEntityKey create(Object... values) {
    if (values != null && values.length == 1 && values[0] instanceof Iterable<?>)
      return create(Enumerable.create((Iterable<Object>) values[0]).toArray(Object.class));
    if (values != null && values.length == 1 && values[0] instanceof OEntityKey)
      return (OEntityKey) values[0];
    if (values != null && values.length == 1 && values[0] instanceof OEntityId)
      return ((OEntityId) values[0]).getEntityKey();
    if (values != null && values.length > 1 && values.length % 2 == 0 && values[0] instanceof String) {
      Map<String, Object> rt = new HashMap<String, Object>();
      for (int i = 0; i < values.length; i += 2) {
        String name = (String) values[i];
        Object value = values[i + 1];
        rt.put(name, value);
      }
      return create(rt);
    }
    Object[] v = validate(values);
    return new OEntityKey(v);
  }

  /**
   * Creates an entity-key from a map of names and values.
   *
   * @param values  the map of names and values
   * @return a newly-created entity-key
   */
  public static OEntityKey create(Map<String, Object> values) {
    return create(NamedValues.fromMap(values));
  }

  /**
   * Creates an entity-key using key information from the given entity-set and values from the given property list.
   *
   * @param entitySet  an entity-set to provide key information
   * @param props  a list of properties to provide key values
   * @return a newly-created entity-key
   */
  public static OEntityKey infer(EdmEntitySet entitySet, List<OProperty<?>> props) {
    if (entitySet == null)
      throw new IllegalArgumentException("EdmEntitySet cannot be null");
    if (props == null)
      throw new IllegalArgumentException("props cannot be null");
    EdmEntityType eet = entitySet.getType();
    if (eet == null)
      throw new IllegalArgumentException("EdmEntityType cannot be null");
    List<String> keys = eet.getKeys();
    if (keys.size() == 0) {
      String idProp = Enumerable.create(eet.getProperties())
          .select(OFuncs.name(EdmProperty.class))
          .firstOrNull(OPredicates.equalsIgnoreCase("id"));
      if (idProp != null)
        keys.add(idProp);
    }
    Object[] v = new Object[keys.size()];
    for (int i = 0; i < v.length; i++) {
      String keyPropertyName = keys.get(i);
      v[i] = getProp(props, keyPropertyName);
    }
    v = validate(v);
    return new OEntityKey(v);
  }

  /**
   * Creates an entity-key from its standard string representation.
   *
   * @param keyString  a standard key-string
   * @return a newly-created entity-key
   */
  public static OEntityKey parse(String keyString) {

    if (keyString == null)
      throw new IllegalArgumentException("keyString cannot be null");

    keyString = keyString.trim();

    if (keyString.startsWith("(") && keyString.endsWith(")"))
      keyString = keyString.substring(1, keyString.length() - 1);

    keyString = keyString.trim();

    if (keyString.length() == 0)
      throw new IllegalArgumentException("keyString cannot be blank");

    String[] tokens = tokens(keyString, ',');
    List<Object> values = new ArrayList<Object>(tokens.length);
    for (String token : tokens) {
      String[] nv = tokens(token, '=');
      if (nv.length != 1 && nv.length != 2)
        throw new IllegalArgumentException("bad keyString: " + keyString);
      String valueString = nv.length == 1 ? nv[0] : nv[1];
      try {
        CommonExpression expr = ExpressionParser.parse(valueString);
        LiteralExpression literal = (LiteralExpression) expr;
        Object value = Expression.literalValue(literal);
        values.add(nv.length == 1 ? value : NamedValues.create(nv[0], value));
      } catch (Exception e) {
        throw new IllegalArgumentException(String.format("bad valueString [%s] as part of keyString [%s]", valueString, keyString), e);
      }
    }
    return OEntityKey.create(values);
  }

  private static String[] tokens(String ks, char sep) {
    List<String> rt = new ArrayList<String>();
    boolean inString = false;
    int start = 0;
    for (int i = 0; i < ks.length(); i++) {
      char c = ks.charAt(i);
      if (c == sep && !inString) {
        rt.add(ks.substring(start, i));
        start = i + 1;
        continue;
      }
      if (c == '\'') {
        if (!inString) {
          inString = true;
          continue;
        }

        if (i < ks.length() - 1) {
          char next = ks.charAt(i + 1);
          if (next == '\'') {
            i++;
            continue;
          }

        }
        inString = false;
      }
    }
    rt.add(ks.substring(start, ks.length()));
    return rt.toArray(new String[rt.size()]);
  }

  @Override
  public String toString() {
    return toKeyString();
  }

  /**
   * Gets the standard string representation of this entity-key, including parentheses.
   *
   * @return the standard key-string
   */
  public String toKeyString() {
    return keyString;
  }

  /**
   * Gets the standard string representation of this entity-key, excluding parentheses.
   *
   * @return the standard key-string, without parentheses
   */
  public String toKeyStringWithoutParentheses() {
    String keyString = this.keyString;
    if (keyString.startsWith("(") && keyString.endsWith(")"))
      keyString = keyString.substring(1, keyString.length() - 1);
    return keyString;
  }

  @Override
  public int hashCode() {
    return keyString.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof OEntityKey)
        && ((OEntityKey) obj).keyString.equals(keyString);
  }

  /**
   * Gets a the value of a single-valued entity-key.
   *
   * @return the key value
   */
  public Object asSingleValue() {
    if (values.length > 1)
      throw new RuntimeException("Complex key cannot be represented as a single value");
    return values[0];
  }

  /**
   * Gets the values of a complex entity-key.
   *
   * @return the key values as a set of named-values
   */
  @SuppressWarnings("unchecked")
  public Set<NamedValue<?>> asComplexValue() {
    assertComplex();
    return (Set<NamedValue<?>>) (Object) toSortedSet(Enumerable.create(values).cast(NamedValue.class), OComparators.namedValueByNameRaw());
  }

  /**
   * Gets the values of complex entity-key.
   *
   * @return the key values as a set of properties
   */
  public Set<OProperty<?>> asComplexProperties() {
    assertComplex();
    return toSortedSet(Enumerable.create(values).cast(NamedValue.class).select(OFuncs.namedValueToPropertyRaw()), OComparators.propertyByName());
  }

  /**
   * returns the value for a named value in a complex key.
   * @param name
   * @return the value
   */
  public Object getComplexKeyValue(String name) {
    assertComplex();
    for (Object o : this.values) {
      NamedValue<?> nv = (NamedValue<?>) o;
      if (nv.getName().equals(name)) {
        return nv.getValue();
      }
    }
    return null;
  }

  private static <T> SortedSet<T> toSortedSet(Enumerable<T> enumerable, Comparator<T> comparator) {
    TreeSet<T> rt = new TreeSet<T>(comparator);
    rt.addAll(enumerable.toSet());
    return rt;
  }

  private void assertComplex() {
    if (values.length == 1)
      throw new RuntimeException("Single-valued key cannot be represented as a complex value");
  }

  /**
   * Gets the entity-key type: SINGLE or COMPLEX.
   *
   * @return SINGLE or COMPLEX
   */
  public KeyType getKeyType() {
    return values.length == 1 ? KeyType.SINGLE : KeyType.COMPLEX;
  }

  private static OProperty<?> getProp(List<OProperty<?>> props, String name) {
    for (OProperty<?> prop : props)
      if (prop.getName().equals(name))
        return prop;
    throw new IllegalArgumentException(String.format(
        "Property %s not found in %s", name, props));

  }

  private static Object[] validate(Object[] values) {
    if (values == null)
      throw new IllegalArgumentException("Key values cannot be null");
    for (Object value : values)
      if (value == null)
        throw new IllegalArgumentException("Key values cannot be null");
    if (values.length == 0)
      throw new IllegalArgumentException("Key values cannot be empty");

    if (values.length == 1) {
      Object o = values[0];
      if (o instanceof NamedValue)
        o = ((NamedValue<?>) o).getValue();
      assertSimple(o);
      return new Object[] { o };
    }

    Object[] v = new Object[values.length];
    for (int i = 0; i < values.length; i++) {
      Object o = values[i];
      if (!(o instanceof NamedValue<?>))
        throw new IllegalArgumentException(
            "Complex key values must be named");
      NamedValue<?> nv = (NamedValue<?>) o;
      if (nv.getName() == null || nv.getName().length() == 0)
        throw new IllegalArgumentException(
            "Complex key values must be named");
      if (nv.getValue() == null)
        throw new IllegalArgumentException(
            "Complex key values cannot be null");
      assertSimple(nv.getValue());
      v[i] = NamedValues.copy(nv);
    }
    return v;

  }

  private static void assertSimple(Object o) {
    if (!EDM_SIMPLE_JAVA_TYPES.contains(o.getClass()))
      throw new IllegalArgumentException("Key value must be a simple type, found: " + o.getClass().getName());
  }

  private static final Set<Class<?>> EDM_SIMPLE_JAVA_TYPES =
      Enumerable.create(EdmSimpleType.ALL).selectMany(new Func1<EdmSimpleType<?>, Enumerable<Class<?>>>() {
        public Enumerable<Class<?>> apply(EdmSimpleType<?> input) {
          return Enumerable.create(input.getJavaTypes());
        }
      }).toSet();

  private static String keyString(Object[] values) {

    String keyValue;
    if (values.length == 1) {
      keyValue = keyString(values[0], false);
    } else {
      keyValue = Enumerable.create(values)
          .select(new Func1<Object, String>() {
            public String apply(Object input) {
              return keyString(input, true);
            }
          }).orderBy().join(",");
    }

    return "(" + keyValue + ")";
  }

  private static String keyString(Object keyValue, boolean includePropName) {
    if (keyValue instanceof NamedValue<?>) {
      NamedValue<?> namedValue = (NamedValue<?>) keyValue;
      String value = keyString(namedValue.getValue(), false);
      if (includePropName)
        return namedValue.getName() + "=" + value;
      else
        return value;
    }

    LiteralExpression expr = Expression.literal(keyValue);
    return Expression.asFilterString(expr);
  }

}

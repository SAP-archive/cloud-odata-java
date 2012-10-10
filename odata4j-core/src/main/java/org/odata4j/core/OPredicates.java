package org.odata4j.core;

import org.core4j.Predicate1;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmStructuralType;

/**
 * A static factory to create useful generic predicate instances.
 */
public class OPredicates {

  private OPredicates() {}

  public static Predicate1<OEntity> entityPropertyValueEquals(final String propName, final Object value) {
    return new Predicate1<OEntity>() {
      @Override
      public boolean apply(OEntity input) {
        Object pv = input.getProperty(propName).getValue();
        return (value == null) ? pv == null : value.equals(pv);
      }
    };
  }

  public static Predicate1<OLink> linkTitleEquals(final String title) {
    return new Predicate1<OLink>() {
      @Override
      public boolean apply(OLink input) {
        String lt = input.getTitle();
        return (title == null) ? lt == null : title.equals(lt);
      }
    };
  }

  public static Predicate1<OProperty<?>> propertyNameEquals(final String propName) {
    return new Predicate1<OProperty<?>>() {
      public boolean apply(OProperty<?> input) {
        return input.getName().equals(propName);
      }
    };
  }

  public static Predicate1<String> equalsIgnoreCase(final String value) {
    return new Predicate1<String>() {
      public boolean apply(String input) {
        return input.equalsIgnoreCase(value);
      }
    };
  }

  public static Predicate1<EdmProperty> edmPropertyNameEquals(final String name) {
    return new Predicate1<EdmProperty>() {
      public boolean apply(EdmProperty input) {
        return input.getName().equals(name);
      }
    };
  }

  public static <T extends Named> Predicate1<T> nameEquals(Class<T> namedType, final String name) {
    return new Predicate1<T>() {
      public boolean apply(T input) {
        return input.getName().equals(name);
      }
    };
  }

  public static Predicate1<EdmStructuralType> edmSubTypeOf(final EdmStructuralType t) {
    return new Predicate1<EdmStructuralType>() {
      public boolean apply(EdmStructuralType input) {
        return !t.equals(input) && t.equals(input.getBaseType());
      }
    };
  }

}

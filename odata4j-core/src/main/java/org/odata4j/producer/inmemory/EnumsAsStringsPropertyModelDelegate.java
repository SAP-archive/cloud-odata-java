package org.odata4j.producer.inmemory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnumsAsStringsPropertyModelDelegate extends PropertyModelDelegate {

  private final PropertyModel propertyModel;

  public EnumsAsStringsPropertyModelDelegate(PropertyModel propertyModel) {
    this.propertyModel = propertyModel;
  }

  @Override
  public PropertyModel getDelegate() {
    return propertyModel;
  }

  @Override
  public Class<?> getPropertyType(String propertyName) {
    Class<?> rt = super.getPropertyType(propertyName);
    if (rt != null && rt.isEnum())
      return String.class;
    return rt;
  }

  @Override
  public Object getPropertyValue(Object target, String propertyName) {
    Class<?> baseType = super.getPropertyType(propertyName);
    Object rt = super.getPropertyValue(target, propertyName);
    if (baseType != null && baseType.isEnum() && rt != null)
      return ((Enum<?>) rt).name();
    return rt;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void setPropertyValue(Object target, String propertyName, Object value) {
    Class baseType = super.getPropertyType(propertyName);
    if (baseType != null && baseType.isEnum() && value instanceof String) {
      // convert string to enum value
      getDelegate().setPropertyValue(target, propertyName, Enum.valueOf(baseType, (String) value));
    } else {
      getDelegate().setPropertyValue(target, propertyName, value);
    }
  }

  @Override
  public Class<?> getCollectionElementType(String collectionName) {
    Class<?> etype = getDelegate().getCollectionElementType(collectionName);
    if (etype != null && etype.isEnum()) {
      return String.class;
    }
    return etype;
  }

  @Override
  public Iterable<?> getCollectionValue(Object target, String collectionName) {
    Iterable<?> iter = getDelegate().getCollectionValue(target, collectionName);
    Class<?> etype = getDelegate().getCollectionElementType(collectionName);
    if (iter != null && etype != null && etype.isEnum()) {
      Iterator<?> i = iter.iterator();
      List<String> l = new ArrayList<String>();
      while (i.hasNext()) {
        l.add(i.next().toString());
      }
      iter = l;
    }
    return iter;
  }

}

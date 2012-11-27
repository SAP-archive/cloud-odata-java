package com.sap.core.odata.ref.util;


public interface Mapper<T, K> {

  T createKey(String key);

  K createValue(Object value);

}

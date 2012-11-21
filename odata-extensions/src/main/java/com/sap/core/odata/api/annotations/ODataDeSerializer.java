package com.sap.core.odata.api.annotations;


public interface ODataDeSerializer<T> {
  Object deserialize(T is);
}

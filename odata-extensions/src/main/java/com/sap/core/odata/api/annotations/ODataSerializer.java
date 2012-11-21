package com.sap.core.odata.api.annotations;


public interface ODataSerializer<T> {

  T serialize(Object obj);
}

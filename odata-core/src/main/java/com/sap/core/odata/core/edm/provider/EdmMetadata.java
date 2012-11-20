package com.sap.core.odata.core.edm.provider;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.exception.ODataSerializationException;

public class EdmMetadata {

  //TODO Implement + Exception handling
  public static void writeMetadata(DataServices metadata, Writer writer) throws ODataSerializationException {
    try {
      writer.append('<');
      writer.append('x');
      writer.append('m');
      writer.append('l');
      writer.append('>');
      writer.append('M');
      writer.append('e');
      writer.append('t');
      writer.append('a');
      writer.append('d');
      writer.append('a');
      writer.append('t');
      writer.append('a');
      writer.append('<');
      writer.append('/');
      writer.append('x');
      writer.append('m');
      writer.append('l');
      writer.append('>');
    } catch (IOException e) {
      new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  //TODO Implement + Exception handling
  public static DataServices readMetadata(Reader reader) {
    return null;
  }
}
package org.odata4j.internal;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class BOMWorkaroundReader extends FilterReader {

  private boolean first = true;

  public BOMWorkaroundReader(Reader arg0) {
    super(arg0);
  }

  @Override
  public int read() throws IOException {
    return super.read();
  }

  @Override
  public int read(char[] cbuf) throws IOException {
    return super.read(cbuf);
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    if (first) {
      char[] buffer = new char[cbuf.length];
      int rt = super.read(buffer, off, len);
      if (rt > 0) {
        int bufferOff = off;
        int bufferLen = len;
        if (buffer[bufferOff] == '\uFEFF') {
          bufferOff++;
          bufferLen--;
          rt--;
        }
        System.arraycopy(buffer, bufferOff, cbuf, off, bufferLen);
        first = false;
        return rt;
      }
    }

    return super.read(cbuf, off, len);
  }

  @Override
  public int read(CharBuffer target) throws IOException {
    return super.read(target);
  }

}
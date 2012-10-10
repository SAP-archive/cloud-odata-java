package org.odata4j.format.json;

import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Stack;

import org.odata4j.format.json.JsonStreamReaderFactory.JsonParseException;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEndPropertyEvent;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonStartPropertyEvent;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonValueEvent;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamTokenizer;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamTokenizer.JsonToken;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamTokenizer.JsonTokenType;

public class JsonStreamReaderFactory {

  public static class JsonParseException extends RuntimeException {

    private static final long serialVersionUID = 2362481232045271688L;

    public JsonParseException() {}

    public JsonParseException(String message, Throwable cause) {
      super(message, cause);
    }

    public JsonParseException(String message) {
      super(message);
    }

    public JsonParseException(Throwable cause) {
      super(cause);
    }
  }

  public interface JsonStreamReader {

    public static interface JsonEvent {

      boolean isStartObject();

      boolean isEndObject();

      boolean isStartProperty();

      boolean isEndProperty();

      boolean isStartArray();

      boolean isEndArray();

      boolean isValue();

      JsonStartPropertyEvent asStartProperty();

      JsonEndPropertyEvent asEndProperty();

      JsonValueEvent asValue();
    }

    public static interface JsonStartPropertyEvent extends JsonEvent {
      String getName();
    }

    public static interface JsonEndPropertyEvent extends JsonEvent {
      // returns a value if it is a simple property and
      // not an other JsonObject or JsonArray
      String getValue();

      JsonTokenType getValueTokenType();
    }

    public static interface JsonValueEvent extends JsonEvent {
      String getValue();
    }

    boolean hasNext();

    JsonEvent nextEvent();

    /**
     * returns the JsonEvent that the last call to nextEvent() returned.
     *
     * @return the last JsonEvent returned by nextEvent()
     */
    JsonEvent previousEvent();

    void skipNestedEvents();

    void close();
  }

  public interface JsonStreamTokenizer {

    public enum JsonTokenType {
      LEFT_CURLY_BRACKET,
      RIGHT_CURLY_BRACKET,
      LEFT_BRACKET,
      RIGHT_BRACKET,
      COMMA,
      COLON,
      TRUE,
      FALSE,
      NULL,
      NUMBER,
      STRING;
    }

    public class JsonToken {
      public final JsonTokenType type;
      public final String value;

      public JsonToken(JsonTokenType type) {
        this(type, null);
      }

      public JsonToken(JsonTokenType type, String value) {
        this.type = type;
        this.value = value;
      }

      @Override
      public String toString() {
        StringBuilder bld = new StringBuilder();

        bld.append(type);
        if (value != null) {
          bld.append("(").append(value).append(")");
        }

        return bld.toString();
      }
    }

    boolean hasNext();

    JsonToken nextToken();

    void close();
  }

  public static JsonStreamReader createJsonStreamReader(Reader reader) {
    return new JsonStreamReaderImpl(reader);
  }

  public static JsonStreamTokenizer createJsonStreamTokenizer(Reader reader) {
    return new JsonStreamTokenizerImpl(reader);
  }
}

class JsonEventImpl implements JsonEvent {

  @Override
  public boolean isStartObject() {
    return false;
  }

  @Override
  public boolean isEndObject() {
    return false;
  }

  @Override
  public boolean isStartProperty() {
    return false;
  }

  @Override
  public boolean isEndProperty() {
    return false;
  }

  @Override
  public boolean isStartArray() {
    return false;
  }

  @Override
  public boolean isEndArray() {
    return false;
  }

  @Override
  public boolean isValue() {
    return false;
  }

  @Override
  public JsonStartPropertyEvent asStartProperty() {
    return (JsonStartPropertyEvent) this;
  }

  @Override
  public JsonEndPropertyEvent asEndProperty() {
    return (JsonEndPropertyEvent) this;
  }

  @Override
  public JsonValueEvent asValue() {
    return (JsonValueEvent) this;
  }

  public String toString() {
    StringBuilder bld = new StringBuilder();

    if (isStartObject()) {
      bld.append("StartObject('{')");
    } else if (isEndObject()) {
      bld.append("EndObject('}')");
    } else if (isStartArray()) {
      bld.append("StartArray('[')");
    } else if (isEndArray()) {
      bld.append("EndArray(']')");
    } else if (isStartProperty()) {
      bld.append("StartProperty(").append(asStartProperty().getName()).append(")");
    } else if (isEndProperty()) {
      if (asEndProperty().getValue() == null) {
        bld.append("EndProperty(").append("<null>").append(")");
      } else {
        bld.append("EndProperty(").append(asEndProperty().getValue()).append(")");
      }
    } else if (isValue()) {
      if (asValue().getValue() == null) {
        bld.append("Value(").append("<null>").append(")");
      } else {
        bld.append("Value(").append(asValue().getValue()).append(")");
      }
    }

    return bld.toString();
  }

}

class JsonStartPropertyEventImpl extends JsonEventImpl implements JsonStartPropertyEvent {

  @Override
  public boolean isStartProperty() {
    return true;
  }

  @Override
  public String getName() {
    return null;
  }

}

class JsonEndPropertyEventImpl extends JsonEventImpl implements JsonEndPropertyEvent {

  @Override
  public boolean isEndProperty() {
    return true;
  }

  @Override
  public String getValue() {
    return null;
  }

  @Override
  public JsonTokenType getValueTokenType() {
    return null;
  }
}

class JsonValueEventImpl extends JsonEventImpl implements JsonValueEvent {

  @Override
  public boolean isValue() {
    return true;
  }

  @Override
  public String getValue() {
    return null;
  }

}

class JsonStreamTokenizerImpl implements JsonStreamTokenizer {
  private Reader reader;
  private JsonToken token;
  private NumberFormat nf = DecimalFormat.getNumberInstance(Locale.US);
  private int pushedBack = -1;

  JsonStreamTokenizerImpl(Reader reader) {
    if (reader == null)
      throw new NullPointerException();

    this.reader = reader;
    move();
  }

  public boolean hasNext() {
    return token != null;
  }

  public JsonToken nextToken() {
    JsonToken cur = token;
    move();
    return cur;
  }

  public void close() {
    try {
      reader.close();
    } catch (IOException ioe) {
      throw new JsonParseException(ioe);
    }
  }

  enum TokenizerState {
    DEFAULT,
    STRING,
    NUMBER
  }

  private void move() {
    token = null;

    StringBuilder buffer = new StringBuilder();
    boolean quote = false;
    TokenizerState state = TokenizerState.DEFAULT;
    int i;
    while (token == null && (i = next()) != -1) {
      char c = (char) i;
      if (state == TokenizerState.DEFAULT) {
        if ('{' == c) {
          token = new JsonToken(JsonTokenType.LEFT_CURLY_BRACKET);
        } else if ('}' == c) {
          if (hasConstantParsed(buffer)) {
            pushBack(i);
          } else {
            token = new JsonToken(JsonTokenType.RIGHT_CURLY_BRACKET);
          }
        } else if ('[' == c) {
          token = new JsonToken(JsonTokenType.LEFT_BRACKET);
        } else if (']' == c) {
          if (hasConstantParsed(buffer)) {
            pushBack(i);
          } else {
            token = new JsonToken(JsonTokenType.RIGHT_BRACKET);
          }
        } else if (':' == c) {
          token = new JsonToken(JsonTokenType.COLON);
        } else if (',' == c) {
          if (hasConstantParsed(buffer)) {
            pushBack(i);
          } else {
            token = new JsonToken(JsonTokenType.COMMA);
          }
        } else if ('"' == c) {
          if (buffer.length() > 0) {
            throw new JsonParseException("no JSON format");
          }
          state = TokenizerState.STRING;
        } else if ('-' == c || Character.isDigit(c)) {
          buffer.append(c);
          state = TokenizerState.NUMBER;
        } else if (Character.isWhitespace(c)) {
          hasConstantParsed(buffer);
        } else {
          buffer.append(c);
        }
      } else if (state == TokenizerState.STRING) {
        if (quote) {
          if ('b' == c) {
            buffer.append('\b');
          } else if ('f' == c) {
            buffer.append('\f');
          } else if ('n' == c) {
            buffer.append('\n');
          } else if ('r' == c) {
            buffer.append('\r');
          } else if ('t' == c) {
            buffer.append('\t');
          } else if ('/' == c) {
            buffer.append('/');
          } else if ('\\' == c) {
            buffer.append('\\');
          } else if ('"' == c) {
            buffer.append('"');
          } else if ('u' == c) {
            buffer.append((char) Integer.parseInt(new String(next(4)), 16));
          } else {
            throw new JsonParseException("illegal escaped character " + c);
          }
          quote = false;
        } else if ('\\' == c) {
          quote = true;
        } else if ('"' == c) {
          token = new JsonToken(JsonTokenType.STRING, buffer.toString());
        } else {
          buffer.append(c);
          quote = false;
        }
      } else {
        if ('-' == c || Character.isDigit(c)
            || 'E' == c || 'e' == c
            || '+' == c || '.' == c) {
          buffer.append(c); // a valid character in a number
        } else {
          // must be done with the number.
          pushBack(i);
          checkNumberFormat(buffer);
          token = new JsonToken(JsonTokenType.NUMBER, buffer.toString());
        }
      }
    }

    if (state == TokenizerState.NUMBER) {
      checkNumberFormat(buffer);
      token = new JsonToken(JsonTokenType.NUMBER, buffer.toString());
    }
  }

  private Number checkNumberFormat(StringBuilder memory) {
    try {
      return nf.parse(memory.toString());
    } catch (Exception nfe) {
      throw new JsonParseException("", nfe);
    }
  }

  private boolean hasConstantParsed(StringBuilder memory) {
    if (isTrue(memory)) {
      token = new JsonToken(JsonTokenType.TRUE, "true");
    } else if (isFalse(memory)) {
      token = new JsonToken(JsonTokenType.FALSE, "false");
    } else if (isNull(memory)) {
      token = new JsonToken(JsonTokenType.NULL, "null");
    }
    return token != null;
  }

  private void pushBack(int c) {
    if (pushedBack != -1) {
      throw new IllegalStateException("can push back only one character");
    } else {
      pushedBack = c;
    }
  }

  private char[] next(int count) {
    char[] ret = new char[count];
    for (int i = 0; i < count; i++) {
      ret[i] = (char) next();
    }

    return ret;
  }

  private int next() {
    if (pushedBack != -1) {
      int ret = pushedBack;
      pushedBack = -1;
      return ret;
    } else {
      try {
        return reader.read();
      } catch (IOException ioe) {
        throw new JsonParseException(ioe);
      }
    }
  }

  private boolean isTrue(StringBuilder str) {
    return str.length() == 4
        && str.charAt(0) == 't'
        && str.charAt(1) == 'r'
        && str.charAt(2) == 'u'
        && str.charAt(3) == 'e';
  }

  private boolean isFalse(StringBuilder str) {
    return str.length() == 5
        && str.charAt(0) == 'f'
        && str.charAt(1) == 'a'
        && str.charAt(2) == 'l'
        && str.charAt(3) == 's'
        && str.charAt(4) == 'e';
  }

  private boolean isNull(StringBuilder str) {
    return str.length() == 4
        && str.charAt(0) == 'n'
        && str.charAt(1) == 'u'
        && str.charAt(2) == 'l'
        && str.charAt(3) == 'l';
  }

}

enum ReaderState {
  NONE,
  OBJECT,
  ARRAY,
  PROPERTY
}

class JsonStreamReaderImpl implements JsonStreamReader {
  private static final boolean DUMP = false;

  private static void dump(String msg) {
    if (DUMP) System.out.println(msg);
  }

  private JsonStreamTokenizerImpl tokenizer;
  private Stack<ReaderState> state = new Stack<ReaderState>();
  private Stack<Boolean> expectCommaOrEndStack = new Stack<Boolean>();
  private boolean expectCommaOrEnd;
  private boolean fireEndPropertyEvent;
  private JsonEvent previousEvent = null;

  JsonStreamReaderImpl(Reader reader) {
    this.state.push(ReaderState.NONE);
    this.tokenizer = new JsonStreamTokenizerImpl(reader);
  }

  @Override
  public boolean hasNext() {
    return tokenizer.hasNext();
  }

  @Override
  public JsonEvent nextEvent() {

    if (fireEndPropertyEvent) {
      if (state.peek() != ReaderState.PROPERTY) {
        throw new IllegalStateException("State is " + state.peek());
      }
      fireEndPropertyEvent = false;
      return createEndPropertyEvent(null, JsonTokenType.NULL);
    }

    if (hasNext()) {
      JsonToken token = tokenizer.nextToken();

      switch (state.peek()) {
      case NONE:
        if (token.type != JsonTokenType.LEFT_CURLY_BRACKET) {
          throw new JsonParseException("no JSON format must start with {");
        } else {
          return createStartObjectEvent();
        }

      case OBJECT:
        if (expectCommaOrEnd) {
          if (token.type == JsonTokenType.COMMA) {
            if (!tokenizer.hasNext()) {
              throw new JsonParseException("no JSON format premature end");
            }
            token = tokenizer.nextToken();
          } else if (token.type != JsonTokenType.RIGHT_CURLY_BRACKET) {
            throw new JsonParseException("no JSON format expected , or ] got " + token.type);
          }
          expectCommaOrEnd = false;
        }

        switch (token.type) {
        case STRING:
          if (!tokenizer.hasNext() || tokenizer.nextToken().type != JsonTokenType.COLON) {
            throw new JsonParseException("no JSON format : expected afer " + token.value);
          }
          expectCommaOrEnd = true;
          return createStartPropertyEvent(token.value);
        case RIGHT_CURLY_BRACKET:
          return createEndObjectEvent();
        default:
          throw new JsonParseException("no JSON format");
        }

      case PROPERTY:
        switch (token.type) {
        case STRING:
        case NUMBER:
        case TRUE:
        case FALSE:
          return createEndPropertyEvent(token.value, token.type);
        case NULL:
          return createEndPropertyEvent(null, token.type);
        case LEFT_CURLY_BRACKET:
          return createStartObjectEvent();
        case LEFT_BRACKET:
          return createStartArrayEvent();
        default:
          throw new JsonParseException("no JSON format");
        }
      case ARRAY:
        if (expectCommaOrEnd) {
          if (token.type == JsonTokenType.COMMA) {
            if (!tokenizer.hasNext()) {
              throw new JsonParseException("no JSON format premature end");
            }
            token = tokenizer.nextToken();
          } else if (token.type != JsonTokenType.RIGHT_BRACKET) {
            throw new JsonParseException("no JSON format expected , or ]");
          }
          expectCommaOrEnd = false;
        }

        switch (token.type) {
        case STRING:
        case NUMBER:
        case TRUE:
        case FALSE:
          expectCommaOrEnd = true;
          return createValueEvent(token.value);
        case NULL:
          expectCommaOrEnd = true;
          return createValueEvent(null);
        case LEFT_CURLY_BRACKET:
          expectCommaOrEnd = true;
          return createStartObjectEvent();
        case LEFT_BRACKET:
          expectCommaOrEnd = true;
          return createStartArrayEvent();
        case RIGHT_BRACKET:
          return createEndArrayEvent();
        default:
          break;
        }
      }
    }
    this.previousEvent = null;
    throw new RuntimeException("no event");
  }

  private JsonEvent createStartPropertyEvent(final String name) {
    state.push(ReaderState.PROPERTY);
    dump("jsonp start property: " + name);
    this.previousEvent = new JsonStartPropertyEventImpl() {

      @Override
      public String getName() {
        return name;
      }
    };
    return this.previousEvent;
  }

  private JsonEvent createEndPropertyEvent(final String value, final JsonTokenType valueTokenType) {
    state.pop();
    dump("jsonp end property: " + value);

    this.previousEvent = new JsonEndPropertyEventImpl() {
      @Override
      public String getValue() {
        return value;
      }

      @Override
      public JsonTokenType getValueTokenType() {
        return valueTokenType;
      }
    };
    return this.previousEvent;
  }

  private JsonEvent createStartObjectEvent() {
    state.push(ReaderState.OBJECT);
    dump("jsonp start object");

    expectCommaOrEndStack.push(expectCommaOrEnd);
    expectCommaOrEnd = false;
    this.previousEvent = new JsonEventImpl() {
      @Override
      public boolean isStartObject() {
        return true;
      }
    };
    return this.previousEvent;
  }

  private JsonEvent createEndObjectEvent() {
    state.pop();
    dump("jsonp end object");
    expectCommaOrEnd = expectCommaOrEndStack.pop();

    // if the end of the object is also the of
    // a property, we need to fire the
    //  endPropertyEvent before going forward.
    if (state.peek() == ReaderState.PROPERTY) {
      fireEndPropertyEvent = true;
    }

    this.previousEvent = new JsonEventImpl() {
      @Override
      public boolean isEndObject() {
        return true;
      }
    };
    return this.previousEvent;
  }

  private JsonEvent createStartArrayEvent() {
    state.push(ReaderState.ARRAY);
    dump("jsonp start array");
    expectCommaOrEndStack.push(expectCommaOrEnd);
    expectCommaOrEnd = false;
    this.previousEvent = new JsonEventImpl() {
      @Override
      public boolean isStartArray() {
        return true;
      }
    };
    return this.previousEvent;
  }

  private JsonEvent createEndArrayEvent() {
    state.pop();
    dump("jsonp end array");
    expectCommaOrEnd = expectCommaOrEndStack.pop();

    // if the end of the array is also the of
    // a property, we need to fire the
    // endPropertyEvent before going forward.
    if (state.peek() == ReaderState.PROPERTY) {
      fireEndPropertyEvent = true;
    }

    this.previousEvent = new JsonEventImpl() {
      @Override
      public boolean isEndArray() {
        return true;
      }
    };
    return this.previousEvent;
  }

  private JsonEvent createValueEvent(final String value) {
    dump("jsonp value: " + value);
    this.previousEvent = new JsonValueEventImpl() {
      @Override
      public String getValue() {
        return value;
      }
    };
    return this.previousEvent;
  }

  @Override
  public JsonEvent previousEvent() {
    return previousEvent;
  }

  @Override
  public void skipNestedEvents() {
    if (!previousEvent.isStartProperty() && !previousEvent.isStartObject() && !previousEvent.isStartArray())
      return;

    // skip until stack element pushed by a start event has been removed by the corresponding end event
    int stackSize = state.size();
    while (hasNext() && state.size() >= stackSize)
      nextEvent();
  }

  @Override
  public void close() {
    tokenizer.close();
  }

}

package org.odata4j.core;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.core4j.Enumerable;
import org.joda.time.LocalDateTime;

/**
 * A consumer-side function-request builder.  Call {@link #execute()} to issue the request.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 * Enumerable<OObject> e = myConsumer.callFunction("AFunction")
 *     .pBoolean("Parameter1", false)
 *     .pInt32("Parameter2", 55)
 *     .execute();
 * }
 * </pre>
 * <p>Note:
 * OData functions can return single instances or collections of instances.
 * To keep the interface simple, callFunction always returns an {@link Enumerable}.
 *
 * @param <T>  the entity representation as a java type
 */
public interface OFunctionRequest<T> extends OQueryRequest<T> {

  /** Adds a generic parameter. */
  OFunctionRequest<T> parameter(String name, OObject value);

  /** Adds a boolean parameter. */
  OFunctionRequest<T> pBoolean(String name, boolean value);

  /** Adds a byte parameter. */
  OFunctionRequest<T> pByte(String name, UnsignedByte value);

  /** Adds a sbyte parameter. */
  OFunctionRequest<T> pSByte(String name, byte value);

  /** Adds a datetime parameter. */
  OFunctionRequest<T> pDateTime(String name, Calendar value);

  /** Adds a datetime parameter. */
  OFunctionRequest<T> pDateTime(String name, Date value);

  /** Adds a datetime parameter. */
  OFunctionRequest<T> pDateTime(String name, LocalDateTime value);

  /** Adds a decimal  parameter. */
  OFunctionRequest<T> pDecimal(String name, BigDecimal value);

  /** Adds a double parameter. */
  OFunctionRequest<T> pDouble(String name, double value);

  /** Adds a guid parameter. */
  OFunctionRequest<T> pGuid(String name, Guid value);

  /** Adds a 16-bit integer parameter. */
  OFunctionRequest<T> pInt16(String name, short value);

  /** Adds a 32-bit integer parameter. */
  OFunctionRequest<T> pInt32(String name, int value);

  /** Adds a 64-bit integer parameter. */
  OFunctionRequest<T> pInt64(String name, long value);

  /** Adds a single parameter. */
  OFunctionRequest<T> pSingle(String name, float value);

  /** Adds a time parameter. */
  OFunctionRequest<T> pTime(String name, Calendar value);

  /** Adds a time parameter. */
  OFunctionRequest<T> pTime(String name, Date value);

  /** Adds a time parameter. */
  OFunctionRequest<T> pTime(String name, LocalDateTime value);

  /** Adds a string parameter. */
  OFunctionRequest<T> pString(String name, String value);

}

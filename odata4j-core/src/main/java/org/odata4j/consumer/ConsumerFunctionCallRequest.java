package org.odata4j.consumer;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.ReadOnlyIterator;
import org.joda.time.LocalDateTime;
import org.odata4j.core.Guid;
import org.odata4j.core.OCollection;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataVersion;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OFunctionParameters;
import org.odata4j.core.OFunctionRequest;
import org.odata4j.core.OObject;
import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.core.UnsignedByte;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.expression.Expression;
import org.odata4j.expression.LiteralExpression;
import org.odata4j.format.FormatParser;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.Settings;
import org.odata4j.internal.InternalUtil;

/**
 * Function-call-request implementation.
 */
public class ConsumerFunctionCallRequest<T extends OObject>
    extends AbstractConsumerQueryRequestBase<T>
    implements OFunctionRequest<T> {

  private final List<OFunctionParameter> params = new LinkedList<OFunctionParameter>();
  private final EdmFunctionImport function;

  public ConsumerFunctionCallRequest(ODataClient client, String serviceRootUri,
      EdmDataServices metadata, String lastSegment) {
    super(client, serviceRootUri, metadata, lastSegment);
    // lastSegment is the function call name.
    function = metadata.findEdmFunctionImport(lastSegment);
    if (function == null)
      throw new IllegalArgumentException("Function Import " + lastSegment + " not defined");
  }

  @SuppressWarnings("unchecked")
  @Override
  public Enumerable<T> execute() throws ODataProducerException {
    // turn each param into a custom query option
    for (OFunctionParameter p : params)
      custom(p.getName(), toUriString(p));

    final ODataClientRequest request = buildRequest(null);
    Enumerable<OObject> results;
    if (function.getReturnType() == null) {
      results = Enumerable.empty(null);
    } else if (function.getReturnType() instanceof EdmCollectionType) {
      final OCollection<OObject> collection = (OCollection<OObject>) doRequest(request);
      results = Enumerable.createFromIterator(
          new Func<Iterator<OObject>>() {
            @Override
            public Iterator<OObject> apply() {
              return new FunctionResultsIterator(request, collection);
            }
          });
    } else {
      results = Enumerable.create(doRequest(request));
    }
    return (Enumerable<T>) results;
  }

  private static String toUriString(OFunctionParameter p) {
    OObject obj = p.getValue();
    if (obj instanceof OSimpleObject) {
      OSimpleObject<?> simple = (OSimpleObject<?>) obj;
      LiteralExpression le = Expression.literal(simple.getType(), simple.getValue());
      return Expression.asFilterString(le);

    }
    throw new UnsupportedOperationException("type not supported: " + obj.getType().getFullyQualifiedTypeName());
  }

  // set parameters to the function call
  @Override
  public ConsumerFunctionCallRequest<T> parameter(String name, OObject value) {
    params.add(OFunctionParameters.create(name, value));
    return this;
  }

  @Override
  public OFunctionRequest<T> pBoolean(String name, boolean value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.BOOLEAN, value));
  }

  @Override
  public OFunctionRequest<T> pByte(String name, UnsignedByte value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.BYTE, value));
  }

  @Override
  public OFunctionRequest<T> pSByte(String name, byte value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.SBYTE, value));
  }

  @Override
  public OFunctionRequest<T> pDateTime(String name, Calendar value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.DATETIME, value));
  }

  @Override
  public OFunctionRequest<T> pDateTime(String name, Date value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.DATETIME, value));
  }

  @Override
  public OFunctionRequest<T> pDateTime(String name, LocalDateTime value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.DATETIME, value));
  }

  @Override
  public OFunctionRequest<T> pDecimal(String name, BigDecimal value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.DECIMAL, value));
  }

  @Override
  public OFunctionRequest<T> pDouble(String name, double value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.DOUBLE, value));
  }

  @Override
  public OFunctionRequest<T> pGuid(String name, Guid value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.GUID, value));
  }

  @Override
  public OFunctionRequest<T> pInt16(String name, short value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.INT16, value));
  }

  @Override
  public OFunctionRequest<T> pInt32(String name, int value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.INT32, value));
  }

  @Override
  public OFunctionRequest<T> pInt64(String name, long value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.INT64, value));
  }

  @Override
  public OFunctionRequest<T> pSingle(String name, float value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.SINGLE, value));
  }

  @Override
  public OFunctionRequest<T> pTime(String name, Calendar value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.TIME, value));
  }

  @Override
  public OFunctionRequest<T> pTime(String name, Date value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.TIME, value));
  }

  @Override
  public OFunctionRequest<T> pTime(String name, LocalDateTime value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.TIME, value));
  }

  @Override
  public OFunctionRequest<T> pString(String name, String value) {
    return parameter(name, OSimpleObjects.create(EdmSimpleType.STRING, value));
  }

  private OObject doRequest(ODataClientRequest request) throws ODataProducerException {
    ODataClientResponse response = getClient().callFunction(request);

    ODataVersion version = InternalUtil.getDataServiceVersion(response.getHeaders().getFirst(ODataConstants.Headers.DATA_SERVICE_VERSION));

    FormatParser<? extends OObject> parser = FormatParserFactory.getParser(
        function.getReturnType().isSimple() ? OSimpleObject.class : EdmType.getInstanceType(function.getReturnType()),
        getClient().getFormatType(),
        new Settings(
            version,
            getMetadata(),
            function.getName(),
            null, // entitykey
            null, // fcMapping
            true, // isResponse
            function.getReturnType()));

    OObject object = parser.parse(getClient().getFeedReader(response));
    response.close();
    return object;
  }

  private class FunctionResultsIterator extends ReadOnlyIterator<OObject> {

    private ODataClientRequest request;
    private OCollection<OObject> current = null;
    private Iterator<OObject> iter = null;
    private int count = 0;

    public FunctionResultsIterator(ODataClientRequest request, OCollection<OObject> current) {
      this.request = request;
      this.current = current;
      this.iter = current.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected IterationResult<OObject> advance() throws Exception {

      if (current == null) {
        current = (OCollection<OObject>) doRequest(this.request);
        iter = current.iterator();
        count = 0;
      }

      if (iter != null && iter.hasNext()) {
        count++;
        return IterationResult.next(iter.next());
      } else {
        return IterationResult.done();
      }

      /* TODO support paging */
    }
  }

}

package org.odata4j.expression;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;
import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.core.UnsignedByte;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.expression.ExpressionParser.AggregateFunction;
import org.odata4j.expression.OrderByExpression.Direction;

public class Expression {

  private Expression() {}

  public static CommonExpression parse(String value) {
    return ExpressionParser.parse(value);
  }

  public static String asPrintString(CommonExpression expr) {
    PrintExpressionVisitor v = new PrintExpressionVisitor();
    expr.visit(v);
    return v.toString();
  }

  public static String asFilterString(CommonExpression expr) {
    FilterExpressionVisitor v = new FilterExpressionVisitor();
    expr.visit(v);
    return v.toString();
  }

  public static NullLiteral null_() {
    return NullLiteralImpl.INSTANCE;
  }

  public static IntegralLiteral integral(int value) {
    return new IntegralLiteralImpl(value);
  }

  public static BooleanLiteral boolean_(boolean value) {
    return value ? BooleanLiteralImpl.TRUE : BooleanLiteralImpl.FALSE;
  }

  public static DateTimeLiteral dateTime(LocalDateTime value) {
    return new DateTimeLiteralImpl(value);
  }

  public static DateTimeOffsetLiteral dateTimeOffset(DateTime value) {
    return new DateTimeOffsetLiteralImpl(value);
  }

  public static TimeLiteral time(LocalTime value) {
    return new TimeLiteralImpl(value);
  }

  public static StringLiteral string(String value) {
    return new StringLiteralImpl(value);
  }

  public static GuidLiteral guid(Guid value) {
    return new GuidLiteralImpl(value);
  }

  public static DecimalLiteral decimal(BigDecimal value) {
    return new DecimalLiteralImpl(value);
  }

  public static BinaryLiteral binary(byte[] value) {
    return new BinaryLiteralImpl(value);
  }

  public static ByteLiteral byte_(UnsignedByte value) {
    return new ByteLiteralImpl(value);
  }

  public static SByteLiteral sbyte_(byte value) {
    return new SByteLiteralImpl(value);
  }

  public static SingleLiteral single(float value) {
    return new SingleLiteralImpl(value);
  }

  public static DoubleLiteral double_(double value) {
    return new DoubleLiteralImpl(value);
  }

  public static Int64Literal int64(long value) {
    return new Int64LiteralImpl(value);
  }

  public static EntitySimpleProperty simpleProperty(String propertyName) {
    return new EntitySimplePropertyImpl(propertyName);
  }

  public static EqExpression eq(CommonExpression lhs, CommonExpression rhs) {
    return new EqExpressionImpl(lhs, rhs);
  }

  public static NeExpression ne(CommonExpression lhs, CommonExpression rhs) {
    return new NeExpressionImpl(lhs, rhs);
  }

  public static AndExpression and(BoolCommonExpression lhs, BoolCommonExpression rhs) {
    return new AndExpressionImpl(lhs, rhs);
  }

  public static OrExpression or(final BoolCommonExpression lhs, final BoolCommonExpression rhs) {
    return new OrExpressionImpl(lhs, rhs);
  }

  public static LtExpression lt(final CommonExpression lhs, final CommonExpression rhs) {
    return new LtExpressionImpl(lhs, rhs);
  }

  public static GtExpression gt(final CommonExpression lhs, final CommonExpression rhs) {
    return new GtExpressionImpl(lhs, rhs);
  }

  public static LeExpression le(final CommonExpression lhs, final CommonExpression rhs) {
    return new LeExpressionImpl(lhs, rhs);
  }

  public static GeExpression ge(final CommonExpression lhs, final CommonExpression rhs) {
    return new GeExpressionImpl(lhs, rhs);
  }

  public static AddExpression add(final CommonExpression lhs, final CommonExpression rhs) {
    return new AddExpressionImpl(lhs, rhs);
  }

  public static SubExpression sub(final CommonExpression lhs, final CommonExpression rhs) {
    return new SubExpressionImpl(lhs, rhs);
  }

  public static MulExpression mul(final CommonExpression lhs, final CommonExpression rhs) {
    return new MulExpressionImpl(lhs, rhs);
  }

  public static DivExpression div(final CommonExpression lhs, final CommonExpression rhs) {
    return new DivExpressionImpl(lhs, rhs);
  }

  public static ModExpression mod(final CommonExpression lhs, final CommonExpression rhs) {
    return new ModExpressionImpl(lhs, rhs);
  }

  public static ParenExpression paren(final CommonExpression expression) {
    return new ParenExpressionImpl(expression);
  }

  public static BoolParenExpression boolParen(final CommonExpression expression) {
    return new BoolParenExpressionImpl(expression);
  }

  public static NotExpression not(final CommonExpression expression) {
    return new NotExpressionImpl(expression);
  }

  public static NegateExpression negate(final CommonExpression expression) {
    return new NegateExpressionImpl(expression);
  }

  public static CastExpression cast(String type) {
    return cast(null, type);
  }

  public static CastExpression cast(final CommonExpression expression, final String type) {
    return new CastExpressionImpl(expression, type);
  }

  public static IsofExpression isof(String type) {
    return isof(null, type);
  }

  public static IsofExpression isof(final CommonExpression expression, final String type) {
    return new IsofExpressionImpl(expression, type);
  }

  public static EndsWithMethodCallExpression endsWith(final CommonExpression target, final CommonExpression value) {
    return new EndsWithMethodCallExpressionImpl(target, value);
  }

  public static StartsWithMethodCallExpression startsWith(final CommonExpression target, final CommonExpression value) {
    return new StartsWithMethodCallExpressionImpl(target, value);
  }

  public static SubstringOfMethodCallExpression substringOf(CommonExpression value) {
    return substringOf(value, null);
  }

  public static SubstringOfMethodCallExpression substringOf(final CommonExpression value, final CommonExpression target) {
    return new SubstringOfMethodCallExpressionImpl(target, value);
  }

  public static IndexOfMethodCallExpression indexOf(final CommonExpression target, final CommonExpression value) {
    return new IndexOfMethodCallExpressionImpl(target, value);
  }

  public static ReplaceMethodCallExpression replace(final CommonExpression target, final CommonExpression find, final CommonExpression replace) {
    return new ReplaceMethodCallExpressionImpl(target, find, replace);
  }

  public static ToLowerMethodCallExpression toLower(final CommonExpression target) {
    return new ToLowerMethodCallExpressionImpl(target);
  }

  public static ToUpperMethodCallExpression toUpper(final CommonExpression target) {
    return new ToUpperMethodCallExpressionImpl(target);
  }

  public static TrimMethodCallExpression trim(final CommonExpression target) {
    return new TrimMethodCallExpressionImpl(target);
  }

  public static SubstringMethodCallExpression substring(final CommonExpression target, final CommonExpression start) {
    return substring(target, start, null);
  }

  public static SubstringMethodCallExpression substring(final CommonExpression target, final CommonExpression start, final CommonExpression length) {
    return new SubstringMethodCallExpressionImpl(target, start, length);
  }

  public static ConcatMethodCallExpression concat(final CommonExpression lhs, final CommonExpression rhs) {
    return new ConcatMethodCallExpressionImpl(lhs, rhs);
  }

  public static LengthMethodCallExpression length(final CommonExpression target) {
    return new LengthMethodCallExpressionImpl(target);
  }

  public static YearMethodCallExpression year(final CommonExpression target) {
    return new YearMethodCallExpressionImpl(target);
  }

  public static MonthMethodCallExpression month(final CommonExpression target) {
    return new MonthMethodCallExpressionImpl(target);
  }

  public static DayMethodCallExpression day(final CommonExpression target) {
    return new DayMethodCallExpressionImpl(target);
  }

  public static HourMethodCallExpression hour(final CommonExpression target) {
    return new HourMethodCallExpressionImpl(target);
  }

  public static MinuteMethodCallExpression minute(final CommonExpression target) {
    return new MinuteMethodCallExpressionImpl(target);
  }

  public static SecondMethodCallExpression second(final CommonExpression target) {
    return new SecondMethodCallExpressionImpl(target);
  }

  public static RoundMethodCallExpression round(final CommonExpression target) {
    return new RoundMethodCallExpressionImpl(target);
  }

  public static CeilingMethodCallExpression ceiling(final CommonExpression target) {
    return new CeilingMethodCallExpressionImpl(target);
  }

  public static FloorMethodCallExpression floor(final CommonExpression target) {
    return new FloorMethodCallExpressionImpl(target);
  }

  public static OrderByExpression orderBy(final CommonExpression expression, final Direction direction) {
    return new OrderByExpressionImpl(expression, direction);
  }

  public static LiteralExpression literal(Object value) {
    return literal(null, value);
  }

  public static LiteralExpression literal(EdmSimpleType<?> edmType, Object value) {
    if (edmType == null) {
      if (value == null)
        throw new IllegalArgumentException("Cannot infer literal expression type for a null value");

      edmType = EdmSimpleType.forJavaType(value.getClass());
      if (edmType == null)
        throw new IllegalArgumentException("Cannot infer literal expression type for java type: " + value.getClass().getName());
    }

    // use OSimpleObject for java type normalization
    OSimpleObject<?> prop = OSimpleObjects.create(edmType, value);

    if (edmType.equals(EdmSimpleType.BINARY))
      return binary((byte[]) prop.getValue());
    if (edmType.equals(EdmSimpleType.BOOLEAN))
      return boolean_((Boolean) prop.getValue());
    if (edmType.equals(EdmSimpleType.DATETIME))
      return dateTime((LocalDateTime) prop.getValue());
    if (edmType.equals(EdmSimpleType.DATETIMEOFFSET))
      return dateTimeOffset((DateTime) prop.getValue());
    if (edmType.equals(EdmSimpleType.DECIMAL))
      return decimal((BigDecimal) prop.getValue());
    if (edmType.equals(EdmSimpleType.DOUBLE))
      return double_((Double) prop.getValue());
    if (edmType.equals(EdmSimpleType.STRING))
      return string((String) prop.getValue());
    if (edmType.equals(EdmSimpleType.GUID))
      return guid((Guid) prop.getValue());
    if (edmType.equals(EdmSimpleType.INT64))
      return int64((Long) prop.getValue());
    if (edmType.equals(EdmSimpleType.INT32) || edmType.equals(EdmSimpleType.INT16))
      return integral(Integer.parseInt(prop.getValue().toString()));
    if (edmType.equals(EdmSimpleType.SINGLE))
      return single((Float) prop.getValue());
    if (edmType.equals(EdmSimpleType.TIME))
      return time((LocalTime) prop.getValue());
    if (edmType.equals(EdmSimpleType.BYTE))
      return byte_((UnsignedByte) prop.getValue());
    if (edmType.equals(EdmSimpleType.SBYTE))
      return sbyte_((Byte) prop.getValue());
    throw new UnsupportedOperationException("Cannot infer literal expression type for edm type: " + edmType);
  }

  public static Object literalValue(LiteralExpression expression) {
    if (expression instanceof BinaryLiteral)
      return ((BinaryLiteral) expression).getValue();
    if (expression instanceof ByteLiteral)
      return ((ByteLiteral) expression).getValue();
    if (expression instanceof SByteLiteral)
      return ((SByteLiteral) expression).getValue();
    if (expression instanceof BooleanLiteral)
      return ((BooleanLiteral) expression).getValue();
    if (expression instanceof DateTimeLiteral)
      return ((DateTimeLiteral) expression).getValue();
    if (expression instanceof DateTimeOffsetLiteral)
      return ((DateTimeOffsetLiteral) expression).getValue();
    if (expression instanceof DecimalLiteral)
      return ((DecimalLiteral) expression).getValue();
    if (expression instanceof DoubleLiteral)
      return ((DoubleLiteral) expression).getValue();
    if (expression instanceof StringLiteral)
      return ((StringLiteral) expression).getValue();
    if (expression instanceof GuidLiteral)
      return ((GuidLiteral) expression).getValue();
    if (expression instanceof Int64Literal)
      return ((Int64Literal) expression).getValue();
    if (expression instanceof IntegralLiteral)
      return ((IntegralLiteral) expression).getValue();
    if (expression instanceof NullLiteral)
      return null;
    if (expression instanceof SingleLiteral)
      return ((SingleLiteral) expression).getValue();
    if (expression instanceof TimeLiteral)
      return ((TimeLiteral) expression).getValue();

    throw new UnsupportedOperationException("Implement " + expression);
  }

  public static AggregateAnyFunction any(CommonExpression source) {
    return new AggregateAnyFunctionImpl(source, null, null);
  }

  public static AggregateAnyFunction any(CommonExpression source, String var, BoolCommonExpression predicate) {
    return new AggregateAnyFunctionImpl(source, var, predicate);
  }

  public static AggregateAllFunction all(CommonExpression source, String var, BoolCommonExpression predicate) {
    return new AggregateAllFunctionImpl(source, var, predicate);
  }

  public static AggregateBoolFunction aggregate(AggregateFunction function, CommonExpression source, String var, BoolCommonExpression predicate) {
    switch (function) {
    case all:
      return all(source, var, predicate);
    case any:
      return any(source, var, predicate);
    case none:
      return null;
    default:
      throw new RuntimeException("unexpected AggregateFunction: " + function);
    }
  }

  private abstract static class ExpressionImpl implements CommonExpression {
    private final Class<?> interfaceType;

    protected ExpressionImpl(Class<?> interfaceType) {
      this.interfaceType = interfaceType;
    }

    @Override
    public String toString() {
      return interfaceType.getSimpleName();
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
    }

    abstract void visitThis(ExpressionVisitor visitor);
  }

  private static class NullLiteralImpl extends ExpressionImpl implements NullLiteral {
    static NullLiteral INSTANCE = new NullLiteralImpl();

    private NullLiteralImpl() {
      super(NullLiteral.class);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class BooleanLiteralImpl extends ExpressionImpl implements BooleanLiteral {
    static BooleanLiteral TRUE = new BooleanLiteralImpl(true);
    static BooleanLiteral FALSE = new BooleanLiteralImpl(false);
    private final boolean value;

    private BooleanLiteralImpl(boolean value) {
      super(BooleanLiteral.class);
      this.value = value;
    }

    @Override
    public boolean getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class IntegralLiteralImpl extends ExpressionImpl implements IntegralLiteral {
    private final int value;

    public IntegralLiteralImpl(int value) {
      super(IntegralLiteral.class);
      this.value = value;
    }

    @Override
    public int getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class ByteLiteralImpl extends ExpressionImpl implements ByteLiteral {
    private final UnsignedByte value;

    public ByteLiteralImpl(UnsignedByte value) {
      super(ByteLiteral.class);
      this.value = value;
    }

    @Override
    public UnsignedByte getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class SByteLiteralImpl extends ExpressionImpl implements SByteLiteral {
    private final byte value;

    public SByteLiteralImpl(byte value) {
      super(SByteLiteral.class);
      this.value = value;
    }

    @Override
    public byte getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class SingleLiteralImpl extends ExpressionImpl implements SingleLiteral {
    private final float value;

    public SingleLiteralImpl(float value) {
      super(SingleLiteral.class);
      this.value = value;
    }

    @Override
    public float getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class DoubleLiteralImpl extends ExpressionImpl implements DoubleLiteral {
    private final double value;

    public DoubleLiteralImpl(double value) {
      super(DoubleLiteral.class);
      this.value = value;
    }

    @Override
    public double getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class Int64LiteralImpl extends ExpressionImpl implements Int64Literal {
    private final long value;

    public Int64LiteralImpl(long value) {
      super(Int64Literal.class);
      this.value = value;
    }

    @Override
    public long getValue() {
      return value;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class ObjectLiteralImpl<T> extends ExpressionImpl {
    private final T value;

    public ObjectLiteralImpl(Class<?> interfaceType, T value) {
      super(interfaceType);
      this.value = value;
    }

    public T getValue() {
      return value;
    }
  }

  private static class DateTimeLiteralImpl extends ObjectLiteralImpl<LocalDateTime> implements DateTimeLiteral {
    public DateTimeLiteralImpl(LocalDateTime value) {
      super(DateTimeLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class DateTimeOffsetLiteralImpl extends ObjectLiteralImpl<DateTime> implements DateTimeOffsetLiteral {
    public DateTimeOffsetLiteralImpl(DateTime value) {
      super(DateTimeOffsetLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class TimeLiteralImpl extends ObjectLiteralImpl<LocalTime> implements TimeLiteral {
    public TimeLiteralImpl(LocalTime value) {
      super(TimeLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class StringLiteralImpl extends ObjectLiteralImpl<String> implements StringLiteral {
    public StringLiteralImpl(String value) {
      super(StringLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class GuidLiteralImpl extends ObjectLiteralImpl<Guid> implements GuidLiteral {
    public GuidLiteralImpl(Guid value) {
      super(GuidLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class DecimalLiteralImpl extends ObjectLiteralImpl<BigDecimal> implements DecimalLiteral {
    public DecimalLiteralImpl(BigDecimal value) {
      super(DecimalLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class BinaryLiteralImpl extends ObjectLiteralImpl<byte[]> implements BinaryLiteral {
    public BinaryLiteralImpl(byte[] value) {
      super(BinaryLiteral.class, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class EntitySimplePropertyImpl extends ExpressionImpl implements EntitySimpleProperty {
    private final String propertyName;

    protected EntitySimplePropertyImpl(String propertyName) {
      super(EntitySimpleProperty.class);
      this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
      return propertyName;
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class BinaryCommonExpressionImpl extends ExpressionImpl implements BinaryCommonExpression {
    private final CommonExpression lhs;
    private final CommonExpression rhs;

    public BinaryCommonExpressionImpl(Class<?> interfaceType, CommonExpression lhs, CommonExpression rhs) {
      super(interfaceType);
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public CommonExpression getLHS() {
      return lhs;
    }

    @Override
    public CommonExpression getRHS() {
      return rhs;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getLHS().visit(visitor);
      visitor.betweenDescend();
      getRHS().visit(visitor);
      visitor.afterDescend();
    }
  }

  private static class EqExpressionImpl extends BinaryCommonExpressionImpl implements EqExpression {
    public EqExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(EqExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class NeExpressionImpl extends BinaryCommonExpressionImpl implements NeExpression {
    public NeExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(NeExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class BinaryBoolCommonExpressionImpl extends BinaryCommonExpressionImpl implements BinaryBoolCommonExpression {
    private final BoolCommonExpression lhs;
    private final BoolCommonExpression rhs;

    public BinaryBoolCommonExpressionImpl(Class<?> interfaceType, BoolCommonExpression lhs, BoolCommonExpression rhs) {
      super(interfaceType, lhs, rhs);
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public BoolCommonExpression getLHS() {
      return lhs;
    }

    @Override
    public BoolCommonExpression getRHS() {
      return rhs;
    }
  }

  private static class AndExpressionImpl extends BinaryBoolCommonExpressionImpl implements AndExpression {
    public AndExpressionImpl(BoolCommonExpression lhs, BoolCommonExpression rhs) {
      super(AndExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class OrExpressionImpl extends BinaryBoolCommonExpressionImpl implements OrExpression {
    public OrExpressionImpl(BoolCommonExpression lhs, BoolCommonExpression rhs) {
      super(OrExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class LtExpressionImpl extends BinaryCommonExpressionImpl implements LtExpression {
    public LtExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(LtExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class GtExpressionImpl extends BinaryCommonExpressionImpl implements GtExpression {
    public GtExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(GtExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class LeExpressionImpl extends BinaryCommonExpressionImpl implements LeExpression {
    public LeExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(LeExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class GeExpressionImpl extends BinaryCommonExpressionImpl implements GeExpression {
    public GeExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(GeExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class AddExpressionImpl extends BinaryCommonExpressionImpl implements AddExpression {
    public AddExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(AddExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class SubExpressionImpl extends BinaryCommonExpressionImpl implements SubExpression {
    public SubExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(SubExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class MulExpressionImpl extends BinaryCommonExpressionImpl implements MulExpression {
    public MulExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(MulExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class DivExpressionImpl extends BinaryCommonExpressionImpl implements DivExpression {
    public DivExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(DivExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class ModExpressionImpl extends BinaryCommonExpressionImpl implements ModExpression {
    public ModExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(ModExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class ConcatMethodCallExpressionImpl extends BinaryCommonExpressionImpl implements ConcatMethodCallExpression {
    public ConcatMethodCallExpressionImpl(CommonExpression lhs, CommonExpression rhs) {
      super(ConcatMethodCallExpression.class, lhs, rhs);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class OneExpressionImpl extends ExpressionImpl {
    private final CommonExpression expression;

    protected OneExpressionImpl(Class<?> interfaceType, CommonExpression expression) {
      super(interfaceType);
      this.expression = expression;
    }

    public CommonExpression getExpression() {
      return expression;
    }
  }

  private abstract static class UnaryExpressionImpl extends OneExpressionImpl {
    protected UnaryExpressionImpl(Class<?> interfaceType, CommonExpression expression) {
      super(interfaceType, expression);
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getExpression().visit(visitor);
      visitor.afterDescend();
    }
  }

  private static class ParenExpressionImpl extends UnaryExpressionImpl implements ParenExpression {
    protected ParenExpressionImpl(CommonExpression expression) {
      super(ParenExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class BoolParenExpressionImpl extends UnaryExpressionImpl implements BoolParenExpression {
    protected BoolParenExpressionImpl(CommonExpression expression) {
      super(BoolParenExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class NotExpressionImpl extends UnaryExpressionImpl implements NotExpression {
    protected NotExpressionImpl(CommonExpression expression) {
      super(NotExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class NegateExpressionImpl extends UnaryExpressionImpl implements NegateExpression {
    protected NegateExpressionImpl(CommonExpression expression) {
      super(NegateExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class ExpressionAndTypeImpl extends OneExpressionImpl {
    private final String type;

    protected ExpressionAndTypeImpl(Class<?> interfaceType, CommonExpression expression, String type) {
      super(interfaceType, expression);
      this.type = type;
    }

    public String getType() {
      return type;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      if (getExpression() != null) {
        getExpression().visit(visitor);
        visitor.betweenDescend();
      }
      visitor.visit(getType());
      visitor.afterDescend();
    }
  }

  private static class CastExpressionImpl extends ExpressionAndTypeImpl implements CastExpression {
    protected CastExpressionImpl(CommonExpression expression, String type) {
      super(CastExpression.class, expression, type);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class IsofExpressionImpl extends ExpressionAndTypeImpl implements IsofExpression {
    protected IsofExpressionImpl(CommonExpression expression, String type) {
      super(IsofExpression.class, expression, type);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class EndsWithMethodCallExpressionImpl extends TargetValueExpressionImpl implements EndsWithMethodCallExpression {
    protected EndsWithMethodCallExpressionImpl(CommonExpression target, CommonExpression value) {
      super(EndsWithMethodCallExpression.class, target, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class StartsWithMethodCallExpressionImpl extends TargetValueExpressionImpl implements StartsWithMethodCallExpression {
    protected StartsWithMethodCallExpressionImpl(CommonExpression target, CommonExpression value) {
      super(StartsWithMethodCallExpression.class, target, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class SubstringOfMethodCallExpressionImpl extends TargetValueExpressionImpl implements SubstringOfMethodCallExpression {
    protected SubstringOfMethodCallExpressionImpl(CommonExpression target, CommonExpression value) {
      super(SubstringOfMethodCallExpression.class, target, value);
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getValue().visit(visitor);
      if (getTarget() != null) {
        visitor.betweenDescend();
        getTarget().visit(visitor);
      }
      visitor.afterDescend();
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class ReplaceMethodCallExpressionImpl extends TargetExpressionImpl implements ReplaceMethodCallExpression {
    private final CommonExpression find;
    private final CommonExpression replace;

    protected ReplaceMethodCallExpressionImpl(CommonExpression target, CommonExpression find, CommonExpression replace) {
      super(ReplaceMethodCallExpression.class, target);
      this.find = find;
      this.replace = replace;
    }

    @Override
    public CommonExpression getFind() {
      return find;
    }

    @Override
    public CommonExpression getReplace() {
      return replace;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getTarget().visit(visitor);
      visitor.betweenDescend();
      getFind().visit(visitor);
      visitor.betweenDescend();
      getReplace().visit(visitor);
      visitor.afterDescend();
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class TargetValueExpressionImpl extends TargetExpressionImpl {
    private final CommonExpression value;

    protected TargetValueExpressionImpl(Class<?> interfaceType, CommonExpression target, CommonExpression value) {
      super(interfaceType, target);
      this.value = value;
    }

    public CommonExpression getValue() {
      return value;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getTarget().visit(visitor);
      visitor.betweenDescend();
      getValue().visit(visitor);
      visitor.afterDescend();
    }
  }

  private static class IndexOfMethodCallExpressionImpl extends TargetValueExpressionImpl implements IndexOfMethodCallExpression {
    protected IndexOfMethodCallExpressionImpl(CommonExpression target, CommonExpression value) {
      super(IndexOfMethodCallExpression.class, target, value);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class TargetExpressionImpl extends ExpressionImpl {
    private final CommonExpression target;

    protected TargetExpressionImpl(Class<?> interfaceType, CommonExpression target) {
      super(interfaceType);
      this.target = target;
    }

    public CommonExpression getTarget() {
      return target;
    }
  }

  private static class SubstringMethodCallExpressionImpl extends TargetExpressionImpl implements SubstringMethodCallExpression {
    private final CommonExpression start;
    private final CommonExpression length;

    protected SubstringMethodCallExpressionImpl(CommonExpression target, CommonExpression start, CommonExpression length) {
      super(SubstringMethodCallExpression.class, target);
      this.start = start;
      this.length = length;
    }

    @Override
    public CommonExpression getStart() {
      return start;
    }

    @Override
    public CommonExpression getLength() {
      return length;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getTarget().visit(visitor);
      visitor.betweenDescend();
      getStart().visit(visitor);
      if (getLength() != null) {
        visitor.betweenDescend();
        getLength().visit(visitor);
      }
      visitor.afterDescend();
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class OneTargetExpressionImpl extends OneExpressionImpl {
    protected OneTargetExpressionImpl(Class<?> interfaceType, CommonExpression expression) {
      super(interfaceType, expression);
    }

    public CommonExpression getTarget() {
      return getExpression();
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getTarget().visit(visitor);
      visitor.afterDescend();
    }
  }

  private static class LengthMethodCallExpressionImpl extends OneTargetExpressionImpl implements LengthMethodCallExpression {
    protected LengthMethodCallExpressionImpl(CommonExpression expression) {
      super(LengthMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class FloorMethodCallExpressionImpl extends OneTargetExpressionImpl implements FloorMethodCallExpression {
    protected FloorMethodCallExpressionImpl(CommonExpression expression) {
      super(FloorMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class CeilingMethodCallExpressionImpl extends OneTargetExpressionImpl implements CeilingMethodCallExpression {
    protected CeilingMethodCallExpressionImpl(CommonExpression expression) {
      super(CeilingMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class RoundMethodCallExpressionImpl extends OneTargetExpressionImpl implements RoundMethodCallExpression {
    protected RoundMethodCallExpressionImpl(CommonExpression expression) {
      super(RoundMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class SecondMethodCallExpressionImpl extends OneTargetExpressionImpl implements SecondMethodCallExpression {
    protected SecondMethodCallExpressionImpl(CommonExpression expression) {
      super(SecondMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class MinuteMethodCallExpressionImpl extends OneTargetExpressionImpl implements MinuteMethodCallExpression {
    protected MinuteMethodCallExpressionImpl(CommonExpression expression) {
      super(MinuteMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class HourMethodCallExpressionImpl extends OneTargetExpressionImpl implements HourMethodCallExpression {
    protected HourMethodCallExpressionImpl(CommonExpression expression) {
      super(HourMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class DayMethodCallExpressionImpl extends OneTargetExpressionImpl implements DayMethodCallExpression {
    protected DayMethodCallExpressionImpl(CommonExpression expression) {
      super(DayMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class YearMethodCallExpressionImpl extends OneTargetExpressionImpl implements YearMethodCallExpression {
    protected YearMethodCallExpressionImpl(CommonExpression expression) {
      super(YearMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class MonthMethodCallExpressionImpl extends OneTargetExpressionImpl implements MonthMethodCallExpression {
    protected MonthMethodCallExpressionImpl(CommonExpression expression) {
      super(MonthMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class ToLowerMethodCallExpressionImpl extends OneTargetExpressionImpl implements ToLowerMethodCallExpression {
    protected ToLowerMethodCallExpressionImpl(CommonExpression expression) {
      super(ToLowerMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class ToUpperMethodCallExpressionImpl extends OneTargetExpressionImpl implements ToUpperMethodCallExpression {
    protected ToUpperMethodCallExpressionImpl(CommonExpression expression) {
      super(ToUpperMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class TrimMethodCallExpressionImpl extends OneTargetExpressionImpl implements TrimMethodCallExpression {
    protected TrimMethodCallExpressionImpl(CommonExpression expression) {
      super(LengthMethodCallExpression.class, expression);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private static class OrderByExpressionImpl extends OneExpressionImpl implements OrderByExpression {
    private final Direction direction;

    protected OrderByExpressionImpl(CommonExpression expression, Direction direction) {
      super(OrderByExpression.class, expression);
      this.direction = direction;
    }

    @Override
    public Direction getDirection() {
      return direction;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getExpression().visit(visitor);
      visitor.betweenDescend();
      visitor.visit(getDirection());
      visitor.afterDescend();
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }
  }

  private abstract static class AggregateBoolFunctionImpl extends ExpressionImpl implements AggregateBoolFunction {
    private final CommonExpression source;
    private final String variable;
    private final BoolCommonExpression predicate;

    public AggregateBoolFunctionImpl(CommonExpression source, String variable, BoolCommonExpression predicate) {
      super(AggregateAnyFunction.class);
      this.source = source;
      this.variable = variable;
      this.predicate = predicate;
    }

    @Override
    public CommonExpression getSource() {
      return source;
    }

    @Override
    public BoolCommonExpression getPredicate() {
      return predicate;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
      visitThis(visitor);
      visitor.beforeDescend();
      getSource().visit(visitor);
      visitor.betweenDescend();
      if (getPredicate() != null) {
        getPredicate().visit(visitor);
      }
      visitor.afterDescend();
    }

    @Override
    public String getVariable() {
      return variable;
    }
  }

  private static class AggregateAnyFunctionImpl extends AggregateBoolFunctionImpl implements AggregateAnyFunction {
    public AggregateAnyFunctionImpl(CommonExpression source, String variable, BoolCommonExpression predicate) {
      super(source, variable, predicate);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }

    @Override
    public ExpressionParser.AggregateFunction getFunctionType() {
      return ExpressionParser.AggregateFunction.any;
    }
  }

  private static class AggregateAllFunctionImpl extends AggregateBoolFunctionImpl implements AggregateAllFunction {
    public AggregateAllFunctionImpl(CommonExpression source, String variable, BoolCommonExpression predicate) {
      super(source, variable, predicate);
    }

    @Override
    void visitThis(ExpressionVisitor visitor) {
      visitor.visit(this);
    }

    @Override
    public ExpressionParser.AggregateFunction getFunctionType() {
      return ExpressionParser.AggregateFunction.all;
    }
  }
}

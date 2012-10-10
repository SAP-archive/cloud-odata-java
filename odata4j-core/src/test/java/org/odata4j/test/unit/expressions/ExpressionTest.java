package org.odata4j.test.unit.expressions;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.odata4j.core.Guid;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.expression.AddExpression;
import org.odata4j.expression.AggregateAllFunction;
import org.odata4j.expression.AggregateAnyFunction;
import org.odata4j.expression.AndExpression;
import org.odata4j.expression.BinaryLiteral;
import org.odata4j.expression.BooleanLiteral;
import org.odata4j.expression.CastExpression;
import org.odata4j.expression.CeilingMethodCallExpression;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ConcatMethodCallExpression;
import org.odata4j.expression.DateTimeLiteral;
import org.odata4j.expression.DateTimeOffsetLiteral;
import org.odata4j.expression.DayMethodCallExpression;
import org.odata4j.expression.DecimalLiteral;
import org.odata4j.expression.DivExpression;
import org.odata4j.expression.DoubleLiteral;
import org.odata4j.expression.EndsWithMethodCallExpression;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.EqExpression;
import org.odata4j.expression.Expression;
import org.odata4j.expression.ExpressionParser;
import org.odata4j.expression.FloorMethodCallExpression;
import org.odata4j.expression.GeExpression;
import org.odata4j.expression.GtExpression;
import org.odata4j.expression.GuidLiteral;
import org.odata4j.expression.HourMethodCallExpression;
import org.odata4j.expression.IndexOfMethodCallExpression;
import org.odata4j.expression.Int64Literal;
import org.odata4j.expression.IntegralLiteral;
import org.odata4j.expression.IsofExpression;
import org.odata4j.expression.LeExpression;
import org.odata4j.expression.LengthMethodCallExpression;
import org.odata4j.expression.LtExpression;
import org.odata4j.expression.MinuteMethodCallExpression;
import org.odata4j.expression.ModExpression;
import org.odata4j.expression.MonthMethodCallExpression;
import org.odata4j.expression.MulExpression;
import org.odata4j.expression.NeExpression;
import org.odata4j.expression.NegateExpression;
import org.odata4j.expression.NotExpression;
import org.odata4j.expression.NullLiteral;
import org.odata4j.expression.OrExpression;
import org.odata4j.expression.OrderByExpression;
import org.odata4j.expression.OrderByExpression.Direction;
import org.odata4j.expression.ParenExpression;
import org.odata4j.expression.ReplaceMethodCallExpression;
import org.odata4j.expression.RoundMethodCallExpression;
import org.odata4j.expression.SecondMethodCallExpression;
import org.odata4j.expression.SingleLiteral;
import org.odata4j.expression.StartsWithMethodCallExpression;
import org.odata4j.expression.StringLiteral;
import org.odata4j.expression.SubExpression;
import org.odata4j.expression.SubstringMethodCallExpression;
import org.odata4j.expression.SubstringOfMethodCallExpression;
import org.odata4j.expression.TimeLiteral;
import org.odata4j.expression.ToLowerMethodCallExpression;
import org.odata4j.expression.ToUpperMethodCallExpression;
import org.odata4j.expression.TrimMethodCallExpression;
import org.odata4j.expression.YearMethodCallExpression;

public class ExpressionTest {

  @Test
  public void testExpressionParsing() {

    ExpressionParser.DUMP_EXPRESSION_INFO = true;

    t(Expression.null_(), "null");
    t(Expression.boolean_(true), "true");
    t(Expression.boolean_(false), "false");

    t(Expression.string(""), "''");
    t(Expression.string(""), "  ''    ");
    t(Expression.string("foo"), "'foo'");
    t(Expression.string("foo"), "   'foo' \n");
    t(Expression.string(" foo "), "' foo '");
    t(Expression.string("fo'o"), "'fo''o'");

    t(Expression.integral(0), "0");
    t(Expression.integral(2), "2");
    t(Expression.integral(-2), "-2");
    t(Expression.integral(222222222), "222222222");
    t(Expression.integral(-222222222), "-222222222");
    t(Expression.int64(-2), "-2l");
    t(Expression.int64(-2), "-2L");
    t(Expression.single(-2f), "-2f");
    t(Expression.single(-2f), "-2F");
    t(Expression.single(-2.34f), "-2.34f");
    t(Expression.single(-2.34f), "-2.34F");
    t(Expression.double_(-2d), "-2d");
    t(Expression.double_(-2d), "-2D");
    t(Expression.double_(-2.34d), "-2.34d");
    t(Expression.double_(-2.34d), "-2.34D");
    t(Expression.double_(-2E+1), "-2e+1");
    t(Expression.double_(-2E+1), "-2E+1");
    t(Expression.double_(2E-1), "2e-1");
    t(Expression.double_(2E-1), "2E-1");
    t(Expression.double_(-2.1E+1), "-2.1e+1");
    t(Expression.double_(-2.1E-1), "-2.1E-1");
    t(Expression.decimal(new BigDecimal("2")), "2M");
    t(Expression.decimal(new BigDecimal("2.34")), "2.34M");
    t(Expression.decimal(new BigDecimal("2")), "2m");
    t(Expression.decimal(new BigDecimal("2.34")), "2.34m");
    t(Expression.decimal(new BigDecimal("-2")), "-2M");
    t(Expression.decimal(new BigDecimal("-2.34")), "-2.34M");
    t(Expression.decimal(new BigDecimal("-2")), "-2m");
    t(Expression.decimal(new BigDecimal("-2.34")), "-2.34m");
    t(Expression.dateTime(new LocalDateTime("2008-10-13")), "datetime'2008-10-13T00:00:00'");

    // new DateTime(<string>) does *not* preserve the timezone!
    DateTime dto = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ").withOffsetParsed().parseDateTime("2008-10-13T00:00:00-04:00");
    t(Expression.dateTimeOffset(dto), "datetimeoffset'2008-10-13T00:00:00-04:00'");
    t(Expression.time(new LocalTime("13:20:00")), "time'PT13H20M'");
    t(Expression.guid(Guid.fromString("12345678-aaaa-bbbb-cccc-ddddeeeeffff")), "guid'12345678-aaaa-bbbb-cccc-ddddeeeeffff'");
    t(Expression.guid(Guid.fromString("bf4eeb4d-2ded-4aa6-a167-0571e1057e3b")), "guid'bf4eeb4d-2ded-4aa6-a167-0571e1057e3b'");

    t(Expression.decimal(new BigDecimal("2.345")), "decimal'2.345'");
    t(Expression.binary(new byte[] { (byte) 0xff }), "X'FF'");
    t(Expression.binary(new byte[] { (byte) 0x00, (byte) 0xaa, (byte) 0xff }), "binary'00aaff'");

    t(Expression.simpleProperty("LastName"), "LastName");
    t(Expression.simpleProperty("LastName2"), "   LastName2  ");

    t(Expression.eq(Expression.simpleProperty("LastName"), Expression.string("foo")), "LastName eq 'foo'");
    t(Expression.eq(Expression.simpleProperty("LastName"), Expression.string("foo")), "    LastName    eq     'foo'   ");
    t(Expression.eq(Expression.string("foo"), Expression.simpleProperty("LastName")), "'foo' eq LastName");

    t(Expression.ne(Expression.simpleProperty("LastName"), Expression.string("foo")), "LastName ne 'foo'");

    EqExpression exp = Expression.eq(Expression.simpleProperty("a"), Expression.integral(1));
    t(Expression.and(exp, exp), "a eq 1 and a eq 1");
    t(Expression.or(exp, exp), "a eq 1 or a eq 1");
    t(Expression.or(exp, Expression.and(exp, exp)), "a eq 1 or a eq 1 and a eq 1");
    t(Expression.or(Expression.and(exp, exp), exp), "a eq 1 and a eq 1 or a eq 1");
    t(Expression.and(Expression.boolean_(true), Expression.boolean_(false)), "true and false");

    t(Expression.lt(Expression.simpleProperty("a"), Expression.integral(1)), "a lt 1");
    t(Expression.gt(Expression.simpleProperty("a"), Expression.integral(1)), "a gt 1");
    t(Expression.le(Expression.simpleProperty("a"), Expression.integral(1)), "a le 1");
    t(Expression.ge(Expression.simpleProperty("a"), Expression.integral(1)), "a ge 1");

    t(Expression.add(Expression.integral(1), Expression.integral(2)), "1 add 2");
    t(Expression.sub(Expression.integral(1), Expression.integral(2)), "1 sub 2");
    t(Expression.mul(Expression.integral(1), Expression.integral(2)), "1 mul 2");
    t(Expression.div(Expression.integral(1), Expression.integral(2)), "1 div 2");
    t(Expression.mod(Expression.integral(1), Expression.integral(2)), "1 mod 2");

    t(Expression.paren(Expression.null_()), "(null)");
    t(Expression.paren(Expression.null_()), " (  null )  ");
    t(Expression.paren(Expression.paren(Expression.null_())), "((null))");
    t(Expression.add(Expression.paren(Expression.integral(1)), Expression.paren(Expression.integral(2))), "(1) add (2)");

    t(Expression.not(Expression.null_()), "not null");
    t(Expression.negate(Expression.simpleProperty("a")), "-a");
    t(Expression.negate(Expression.simpleProperty("a")), "- a");
    t(Expression.cast(EdmSimpleType.STRING.getFullyQualifiedTypeName()), "cast('Edm.String')");
    t(Expression.cast(EdmSimpleType.STRING.getFullyQualifiedTypeName()), "cast    ( 'Edm.String'  ) ");
    t(Expression.cast(Expression.null_(), EdmSimpleType.STRING.getFullyQualifiedTypeName()), "cast(null,'Edm.String')");
    t(Expression.cast(Expression.null_(), EdmSimpleType.STRING.getFullyQualifiedTypeName()), "    cast   (  null  ,  'Edm.String'   ) ");
    t(Expression.isof(EdmSimpleType.STRING.getFullyQualifiedTypeName()), "isof('Edm.String')");

    t(Expression.endsWith(Expression.string("aba"), Expression.string("a")), "endswith('aba','a')");
    t(Expression.startsWith(Expression.string("aba"), Expression.string("a")), "startswith('aba','a')");
    t(Expression.substringOf(Expression.string("aba"), Expression.string("a")), "substringof('aba','a')");
    t(Expression.substringOf(Expression.string("aba")), "substringof('aba')");
    t(Expression.indexOf(Expression.string("aba"), Expression.string("a")), "indexof('aba','a')");
    t(Expression.replace(Expression.string("aba"), Expression.string("a"), Expression.string("b")), "replace('aba','a','b')");
    t(Expression.toLower(Expression.string("aba")), "tolower('aba')");
    t(Expression.toUpper(Expression.string("aba")), "toupper('aba')");
    t(Expression.trim(Expression.string("aba")), "trim('aba')");
    t(Expression.substring(Expression.string("aba"), Expression.integral(1)), "substring('aba',1)");
    t(Expression.substring(Expression.string("aba"), Expression.integral(1), Expression.integral(2)), "substring('aba',1,2)");
    t(Expression.concat(Expression.string("a"), Expression.string("b")), "concat('a','b')");
    t(Expression.length(Expression.string("aba")), "length('aba')");

    t(Expression.substringOf(Expression.simpleProperty("Name"), Expression.string("Boris")), "substringof(Name, 'Boris')");

    t(Expression.year(Expression.string("aba")), "year('aba')");
    t(Expression.month(Expression.string("aba")), "month('aba')");
    t(Expression.day(Expression.string("aba")), "day('aba')");
    t(Expression.hour(Expression.string("aba")), "hour('aba')");
    t(Expression.minute(Expression.string("aba")), "minute('aba')");
    t(Expression.second(Expression.string("aba")), "second('aba')");
    t(Expression.round(Expression.string("aba")), "round('aba')");
    t(Expression.ceiling(Expression.string("aba")), "ceiling('aba')");
    t(Expression.floor(Expression.string("aba")), "floor('aba')");

    o("a desc", Expression.orderBy(Expression.simpleProperty("a"), Direction.DESCENDING));
    o("a", Expression.orderBy(Expression.simpleProperty("a"), Direction.ASCENDING));
    o("b desc, a", Expression.orderBy(Expression.simpleProperty("b"), Direction.DESCENDING), Expression.orderBy(Expression.simpleProperty("a"), Direction.ASCENDING));
  }

  private void o(String value, OrderByExpression... expecteds) {
    List<OrderByExpression> actuals = ExpressionParser.parseOrderBy(value);
    Assert.assertEquals(expecteds.length, actuals.size());
    for (int i = 0; i < expecteds.length; i++) {
      OrderByExpression expected = expecteds[i];
      OrderByExpression actual = actuals.get(i);

      Assert.assertEquals(expected.getDirection(), actual.getDirection());

      assertSame(expected.getExpression(), actual.getExpression());

    }

  }

  private void t(CommonExpression expected, String value) {
    CommonExpression actual = ExpressionParser.parse(value);
    assertSame(expected, actual);

  }

  private void assertSame(CommonExpression expected, CommonExpression actual) {
    if (expected == null) {
      Assert.assertNull(actual);
      return;
    } else {
      Assert.assertNotNull(actual);
    }

    if (expected instanceof EqExpression) {
      assertInstanceOf(EqExpression.class, actual);
      assertSame(((EqExpression) actual).getLHS(), ((EqExpression) expected).getLHS());
      assertSame(((EqExpression) actual).getRHS(), ((EqExpression) expected).getRHS());
    } else if (expected instanceof AndExpression) {
      assertInstanceOf(AndExpression.class, actual);
      assertSame(((AndExpression) actual).getLHS(), ((AndExpression) expected).getLHS());
      assertSame(((AndExpression) actual).getRHS(), ((AndExpression) expected).getRHS());
    } else if (expected instanceof OrExpression) {
      assertInstanceOf(OrExpression.class, actual);
      assertSame(((OrExpression) actual).getLHS(), ((OrExpression) expected).getLHS());
      assertSame(((OrExpression) actual).getRHS(), ((OrExpression) expected).getRHS());
    } else if (expected instanceof NeExpression) {
      assertInstanceOf(NeExpression.class, actual);
      assertSame(((NeExpression) actual).getLHS(), ((NeExpression) expected).getLHS());
      assertSame(((NeExpression) actual).getRHS(), ((NeExpression) expected).getRHS());
    } else if (expected instanceof LtExpression) {
      assertInstanceOf(LtExpression.class, actual);
      assertSame(((LtExpression) actual).getLHS(), ((LtExpression) expected).getLHS());
      assertSame(((LtExpression) actual).getRHS(), ((LtExpression) expected).getRHS());
    } else if (expected instanceof GtExpression) {
      assertInstanceOf(GtExpression.class, actual);
      assertSame(((GtExpression) actual).getLHS(), ((GtExpression) expected).getLHS());
      assertSame(((GtExpression) actual).getRHS(), ((GtExpression) expected).getRHS());
    } else if (expected instanceof LeExpression) {
      assertInstanceOf(LeExpression.class, actual);
      assertSame(((LeExpression) actual).getLHS(), ((LeExpression) expected).getLHS());
      assertSame(((LeExpression) actual).getRHS(), ((LeExpression) expected).getRHS());
    } else if (expected instanceof GeExpression) {
      assertInstanceOf(GeExpression.class, actual);
      assertSame(((GeExpression) actual).getLHS(), ((GeExpression) expected).getLHS());
      assertSame(((GeExpression) actual).getRHS(), ((GeExpression) expected).getRHS());
    } else if (expected instanceof AddExpression) {
      assertInstanceOf(AddExpression.class, actual);
      assertSame(((AddExpression) actual).getLHS(), ((AddExpression) expected).getLHS());
      assertSame(((AddExpression) actual).getRHS(), ((AddExpression) expected).getRHS());
    } else if (expected instanceof SubExpression) {
      assertInstanceOf(SubExpression.class, actual);
      assertSame(((SubExpression) actual).getLHS(), ((SubExpression) expected).getLHS());
      assertSame(((SubExpression) actual).getRHS(), ((SubExpression) expected).getRHS());
    } else if (expected instanceof MulExpression) {
      assertInstanceOf(MulExpression.class, actual);
      assertSame(((MulExpression) actual).getLHS(), ((MulExpression) expected).getLHS());
      assertSame(((MulExpression) actual).getRHS(), ((MulExpression) expected).getRHS());
    } else if (expected instanceof DivExpression) {
      assertInstanceOf(DivExpression.class, actual);
      assertSame(((DivExpression) actual).getLHS(), ((DivExpression) expected).getLHS());
      assertSame(((DivExpression) actual).getRHS(), ((DivExpression) expected).getRHS());
    } else if (expected instanceof ModExpression) {
      assertInstanceOf(ModExpression.class, actual);
      assertSame(((ModExpression) actual).getLHS(), ((ModExpression) expected).getLHS());
      assertSame(((ModExpression) actual).getRHS(), ((ModExpression) expected).getRHS());
    } else if (expected instanceof StringLiteral) {
      assertInstanceOf(StringLiteral.class, actual);
      Assert.assertEquals(((StringLiteral) actual).getValue(), ((StringLiteral) expected).getValue());
    } else if (expected instanceof IntegralLiteral) {
      assertInstanceOf(IntegralLiteral.class, actual);
      Assert.assertEquals(((IntegralLiteral) actual).getValue(), ((IntegralLiteral) expected).getValue());
    } else if (expected instanceof Int64Literal) {
      assertInstanceOf(Int64Literal.class, actual);
      Assert.assertEquals(((Int64Literal) actual).getValue(), ((Int64Literal) expected).getValue());
    } else if (expected instanceof DateTimeLiteral) {
      assertInstanceOf(DateTimeLiteral.class, actual);
      Assert.assertEquals(((DateTimeLiteral) actual).getValue(), ((DateTimeLiteral) expected).getValue());
    } else if (expected instanceof DateTimeOffsetLiteral) {
      assertInstanceOf(DateTimeOffsetLiteral.class, actual);
      Assert.assertEquals(((DateTimeOffsetLiteral) actual).getValue(), ((DateTimeOffsetLiteral) expected).getValue());
    } else if (expected instanceof TimeLiteral) {
      assertInstanceOf(TimeLiteral.class, actual);
      Assert.assertEquals(((TimeLiteral) actual).getValue(), ((TimeLiteral) expected).getValue());
    } else if (expected instanceof BooleanLiteral) {
      assertInstanceOf(BooleanLiteral.class, actual);
      Assert.assertEquals(((BooleanLiteral) actual).getValue(), ((BooleanLiteral) expected).getValue());
    } else if (expected instanceof BinaryLiteral) {
      assertInstanceOf(BinaryLiteral.class, actual);
      assertArrayEqual(((BinaryLiteral) expected).getValue(), ((BinaryLiteral) actual).getValue());

    } else if (expected instanceof DecimalLiteral) {
      assertInstanceOf(DecimalLiteral.class, actual);
      Assert.assertEquals(((DecimalLiteral) actual).getValue(), ((DecimalLiteral) expected).getValue());
    } else if (expected instanceof SingleLiteral) {
      assertInstanceOf(SingleLiteral.class, actual);
      Assert.assertEquals(((SingleLiteral) actual).getValue(), ((SingleLiteral) expected).getValue());
    } else if (expected instanceof DoubleLiteral) {
      assertInstanceOf(DoubleLiteral.class, actual);
      Assert.assertEquals(((DoubleLiteral) actual).getValue(), ((DoubleLiteral) expected).getValue());
    } else if (expected instanceof GuidLiteral) {
      assertInstanceOf(GuidLiteral.class, actual);
      Assert.assertEquals(((GuidLiteral) actual).getValue(), ((GuidLiteral) expected).getValue());
    } else if (expected instanceof EntitySimpleProperty) {
      assertInstanceOf(EntitySimpleProperty.class, actual);
      Assert.assertEquals(((EntitySimpleProperty) actual).getPropertyName(), ((EntitySimpleProperty) expected).getPropertyName());
    } else if (expected instanceof NullLiteral) {
      assertInstanceOf(NullLiteral.class, actual);
    } else if (expected instanceof ParenExpression) {
      assertInstanceOf(ParenExpression.class, actual);
      assertSame(((ParenExpression) actual).getExpression(), ((ParenExpression) expected).getExpression());
    } else if (expected instanceof NotExpression) {
      assertInstanceOf(NotExpression.class, actual);
      assertSame(((NotExpression) actual).getExpression(), ((NotExpression) expected).getExpression());
    } else if (expected instanceof NegateExpression) {
      assertInstanceOf(NegateExpression.class, actual);
      assertSame(((NegateExpression) actual).getExpression(), ((NegateExpression) expected).getExpression());
    } else if (expected instanceof CastExpression) {
      assertInstanceOf(CastExpression.class, actual);
      assertSame(((CastExpression) actual).getExpression(), ((CastExpression) expected).getExpression());
      Assert.assertEquals(((CastExpression) actual).getType(), ((CastExpression) expected).getType());
    } else if (expected instanceof IsofExpression) {
      assertInstanceOf(IsofExpression.class, actual);
      assertSame(((IsofExpression) actual).getExpression(), ((IsofExpression) expected).getExpression());
      Assert.assertEquals(((IsofExpression) actual).getType(), ((IsofExpression) expected).getType());
    } else if (expected instanceof EndsWithMethodCallExpression) {
      assertInstanceOf(EndsWithMethodCallExpression.class, actual);
      assertSame(((EndsWithMethodCallExpression) actual).getTarget(), ((EndsWithMethodCallExpression) expected).getTarget());
      assertSame(((EndsWithMethodCallExpression) actual).getValue(), ((EndsWithMethodCallExpression) expected).getValue());
    } else if (expected instanceof StartsWithMethodCallExpression) {
      assertInstanceOf(StartsWithMethodCallExpression.class, actual);
      assertSame(((StartsWithMethodCallExpression) actual).getTarget(), ((StartsWithMethodCallExpression) expected).getTarget());
      assertSame(((StartsWithMethodCallExpression) actual).getValue(), ((StartsWithMethodCallExpression) expected).getValue());
    } else if (expected instanceof SubstringOfMethodCallExpression) {
      assertInstanceOf(SubstringOfMethodCallExpression.class, actual);
      assertSame(((SubstringOfMethodCallExpression) actual).getTarget(), ((SubstringOfMethodCallExpression) expected).getTarget());
      assertSame(((SubstringOfMethodCallExpression) actual).getValue(), ((SubstringOfMethodCallExpression) expected).getValue());
    } else if (expected instanceof IndexOfMethodCallExpression) {
      assertInstanceOf(IndexOfMethodCallExpression.class, actual);
      assertSame(((IndexOfMethodCallExpression) actual).getTarget(), ((IndexOfMethodCallExpression) expected).getTarget());
      assertSame(((IndexOfMethodCallExpression) actual).getValue(), ((IndexOfMethodCallExpression) expected).getValue());
    } else if (expected instanceof ReplaceMethodCallExpression) {
      assertInstanceOf(ReplaceMethodCallExpression.class, actual);
      assertSame(((ReplaceMethodCallExpression) actual).getTarget(), ((ReplaceMethodCallExpression) expected).getTarget());
      assertSame(((ReplaceMethodCallExpression) actual).getFind(), ((ReplaceMethodCallExpression) expected).getFind());
      assertSame(((ReplaceMethodCallExpression) actual).getReplace(), ((ReplaceMethodCallExpression) expected).getReplace());
    } else if (expected instanceof ToLowerMethodCallExpression) {
      assertInstanceOf(ToLowerMethodCallExpression.class, actual);
      assertSame(((ToLowerMethodCallExpression) actual).getTarget(), ((ToLowerMethodCallExpression) expected).getTarget());
    } else if (expected instanceof ToUpperMethodCallExpression) {
      assertInstanceOf(ToUpperMethodCallExpression.class, actual);
      assertSame(((ToUpperMethodCallExpression) actual).getTarget(), ((ToUpperMethodCallExpression) expected).getTarget());
    } else if (expected instanceof TrimMethodCallExpression) {
      assertInstanceOf(TrimMethodCallExpression.class, actual);
      assertSame(((TrimMethodCallExpression) actual).getTarget(), ((TrimMethodCallExpression) expected).getTarget());
    } else if (expected instanceof SubstringMethodCallExpression) {
      assertInstanceOf(SubstringMethodCallExpression.class, actual);
      assertSame(((SubstringMethodCallExpression) actual).getTarget(), ((SubstringMethodCallExpression) expected).getTarget());
      assertSame(((SubstringMethodCallExpression) actual).getStart(), ((SubstringMethodCallExpression) expected).getStart());
      assertSame(((SubstringMethodCallExpression) actual).getLength(), ((SubstringMethodCallExpression) expected).getLength());
    } else if (expected instanceof ConcatMethodCallExpression) {
      assertInstanceOf(ConcatMethodCallExpression.class, actual);
      assertSame(((ConcatMethodCallExpression) actual).getLHS(), ((ConcatMethodCallExpression) expected).getLHS());
      assertSame(((ConcatMethodCallExpression) actual).getRHS(), ((ConcatMethodCallExpression) expected).getRHS());
    } else if (expected instanceof LengthMethodCallExpression) {
      assertInstanceOf(LengthMethodCallExpression.class, actual);
      assertSame(((LengthMethodCallExpression) actual).getTarget(), ((LengthMethodCallExpression) expected).getTarget());
    } else if (expected instanceof YearMethodCallExpression) {
      assertInstanceOf(YearMethodCallExpression.class, actual);
      assertSame(((YearMethodCallExpression) actual).getTarget(), ((YearMethodCallExpression) expected).getTarget());
    } else if (expected instanceof MonthMethodCallExpression) {
      assertInstanceOf(MonthMethodCallExpression.class, actual);
      assertSame(((MonthMethodCallExpression) actual).getTarget(), ((MonthMethodCallExpression) expected).getTarget());
    } else if (expected instanceof DayMethodCallExpression) {
      assertInstanceOf(DayMethodCallExpression.class, actual);
      assertSame(((DayMethodCallExpression) actual).getTarget(), ((DayMethodCallExpression) expected).getTarget());
    } else if (expected instanceof HourMethodCallExpression) {
      assertInstanceOf(HourMethodCallExpression.class, actual);
      assertSame(((HourMethodCallExpression) actual).getTarget(), ((HourMethodCallExpression) expected).getTarget());
    } else if (expected instanceof MinuteMethodCallExpression) {
      assertInstanceOf(MinuteMethodCallExpression.class, actual);
      assertSame(((MinuteMethodCallExpression) actual).getTarget(), ((MinuteMethodCallExpression) expected).getTarget());
    } else if (expected instanceof SecondMethodCallExpression) {
      assertInstanceOf(SecondMethodCallExpression.class, actual);
      assertSame(((SecondMethodCallExpression) actual).getTarget(), ((SecondMethodCallExpression) expected).getTarget());
    } else if (expected instanceof RoundMethodCallExpression) {
      assertInstanceOf(RoundMethodCallExpression.class, actual);
      assertSame(((RoundMethodCallExpression) actual).getTarget(), ((RoundMethodCallExpression) expected).getTarget());
    } else if (expected instanceof FloorMethodCallExpression) {
      assertInstanceOf(FloorMethodCallExpression.class, actual);
      assertSame(((FloorMethodCallExpression) actual).getTarget(), ((FloorMethodCallExpression) expected).getTarget());
    } else if (expected instanceof CeilingMethodCallExpression) {
      assertInstanceOf(CeilingMethodCallExpression.class, actual);
      assertSame(((CeilingMethodCallExpression) actual).getTarget(), ((CeilingMethodCallExpression) expected).getTarget());
    } else if (expected instanceof AggregateAnyFunction) {
      assertInstanceOf(AggregateAnyFunction.class, actual);
      assertSame(((AggregateAnyFunction) expected).getSource(), ((AggregateAnyFunction) actual).getSource());
      assertSame(((AggregateAnyFunction) expected).getVariable(), ((AggregateAnyFunction) actual).getVariable());
      assertSame(((AggregateAnyFunction) expected).getPredicate(), ((AggregateAnyFunction) actual).getPredicate());
    } else if (expected instanceof AggregateAllFunction) {
      assertInstanceOf(AggregateAllFunction.class, actual);
      assertSame(((AggregateAllFunction) expected).getSource(), ((AggregateAllFunction) actual).getSource());
      assertSame(((AggregateAllFunction) expected).getVariable(), ((AggregateAllFunction) actual).getVariable());
      assertSame(((AggregateAllFunction) expected).getPredicate(), ((AggregateAllFunction) actual).getPredicate());
    } else {
      Assert.fail("Unsupported: " + expected);
    }
  }

  private void assertSame(String expected, String actual) {
    if (expected == null) {
      Assert.assertNull(actual);
    } else {
      Assert.assertNotNull(actual);
    }
  }

  private <T> void assertArrayEqual(byte[] expected, byte[] actual) {
    Assert.assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals(expected[i], actual[i]);
    }
  }

  private <T> void assertInstanceOf(Class<T> expected, Object actual) {
    Assert.assertTrue("e:" + expected.getSimpleName() + " a:" + actual.getClass().getSimpleName(), expected.isAssignableFrom(actual.getClass()));
  }

  @Test
  public void testAny() {
    t(Expression.any(Expression.simpleProperty("Actors")), "Actors/any()");
  }

  @Test
  public void testAnyPredicate() {
    t(Expression.any(
        Expression.simpleProperty("Actors"),
        "a",
        Expression.eq(
            Expression.simpleProperty("a/FirstName"),
            Expression.string("Charlize"))),
        "Actors/any(a:a/FirstName eq 'Charlize')");
  }

  @Test
  public void testAnyNestedPredicate() {
    t(Expression.any(
        Expression.simpleProperty("Actors"),
        "a",
        Expression.any(
            Expression.simpleProperty("a/Awards"),
            "w",
            Expression.eq(
                Expression.simpleProperty("w/Name"),
                Expression.string("Oscar")))),
        "Actors/any(a:a/Awards/any(w:w/Name eq 'Oscar'))");
  }

  @Test
  public void testAllPredicate() {
    // it could happen...
    t(Expression.all(
        Expression.simpleProperty("Actors"),
        "a",
        Expression.eq(
            Expression.simpleProperty("a/FirstName"),
            Expression.string("Charlize"))),
        "Actors/all(a:a/FirstName eq 'Charlize')");
  }

  @Test
  public void testAllNestedPredicate() {
    // now that is a cast..
    t(Expression.all(
        Expression.simpleProperty("Actors"),
        "a",
        Expression.all(
            Expression.simpleProperty("a/Awards"),
            "w",
            Expression.eq(
                Expression.simpleProperty("w/Name"),
                Expression.string("Oscar")))),
        "Actors/all(a:a/Awards/all(w:w/Name eq 'Oscar'))");
  }

  @Test
  public void testAnyAllNestedPredicate() {
    t(Expression.any(
        Expression.simpleProperty("Actors"),
        "a",
        Expression.all(
            Expression.simpleProperty("a/Awards"),
            "w",
            Expression.eq(
                Expression.simpleProperty("w/Name"),
                Expression.string("Oscar")))),
        "Actors/any(a:a/Awards/all(w:w/Name eq 'Oscar'))");
  }

  @Test
  public void testAnyAllNestedPredicate2() {
    t(Expression.any(
        Expression.simpleProperty("Actors"),
        "a",
        Expression.or(
            Expression.all(
                Expression.simpleProperty("a/Awards"),
                "w",
                Expression.eq(
                    Expression.simpleProperty("w/Name"),
                    Expression.string("Oscar"))),
            Expression.any(
                Expression.simpleProperty("a/Houses"),
                "h",
                Expression.eq(
                    Expression.simpleProperty("h/City"),
                    Expression.string("Malibu"))))),
        "Actors/any(a:a/Awards/all(w:w/Name eq 'Oscar') or a/Houses/any(h:h/City eq 'Malibu'))");
  }

  @Test
  public void testAnyPredicateOnCollectionProperty() {
    t(Expression.any(
        Expression.simpleProperty("Tags"),
        "t",
        Expression.eq(
            Expression.simpleProperty("t"),
            Expression.string("Beautiful"))),
        "Tags/any(t:t eq 'Beautiful')");
  }
}

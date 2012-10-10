package org.odata4j.producer.jdbc;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmProperty;
import org.odata4j.expression.AddExpression;
import org.odata4j.expression.AggregateAllFunction;
import org.odata4j.expression.AggregateAnyFunction;
import org.odata4j.expression.AndExpression;
import org.odata4j.expression.BinaryLiteral;
import org.odata4j.expression.BoolParenExpression;
import org.odata4j.expression.BooleanLiteral;
import org.odata4j.expression.ByteLiteral;
import org.odata4j.expression.CastExpression;
import org.odata4j.expression.CeilingMethodCallExpression;
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
import org.odata4j.expression.ExpressionVisitor;
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
import org.odata4j.expression.SByteLiteral;
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
import org.odata4j.producer.jdbc.JdbcModel.JdbcColumn;
import org.odata4j.producer.jdbc.SqlStatement.SqlParameter;

public class GenerateWhereClause implements ExpressionVisitor {

  private final StringBuilder sb = new StringBuilder();
  private final List<SqlParameter> params = new ArrayList<SqlParameter>();

  private final EdmEntitySet entitySet;
  private final JdbcMetadataMapping mapping;

  private Stack<String> nextBetween = new Stack<String>();

  public GenerateWhereClause(EdmEntitySet entitySet, JdbcMetadataMapping mapping) {
    this.entitySet = entitySet;
    this.mapping = mapping;
  }

  public void append(StringBuilder sql, List<SqlParameter> params) {
    sql.append(" WHERE ");
    sql.append(sb);
    params.addAll(this.params);
  }

  @Override
  public void beforeDescend() {

  }

  @Override
  public void afterDescend() {

  }

  @Override
  public void betweenDescend() {
    if (!nextBetween.isEmpty()) {
      sb.append(nextBetween.pop());
    }
  }

  @Override
  public void visit(String type) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(OrderByExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(Direction direction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(AddExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(AndExpression expr) {
    nextBetween.push(" AND ");
  }

  @Override
  public void visit(BooleanLiteral expr) {
    sb.append(expr.getValue() ? "TRUE" : "FALSE");
  }

  @Override
  public void visit(CastExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(ConcatMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(DateTimeLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(DateTimeOffsetLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(DecimalLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(DivExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(EndsWithMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(EntitySimpleProperty expr) {
    EdmProperty edmProp = entitySet.getType().findProperty(expr.getPropertyName());
    JdbcColumn column = mapping.getMappedColumn(edmProp);
    sb.append(column.columnName);
  }

  @Override
  public void visit(EqExpression expr) {
    nextBetween.push(" = ");
  }

  @Override
  public void visit(GeExpression expr) {
    nextBetween.push(" >= ");
  }

  @Override
  public void visit(GtExpression expr) {
    nextBetween.push(" > ");
  }

  @Override
  public void visit(GuidLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(BinaryLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(ByteLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(SByteLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(IndexOfMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(SingleLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(DoubleLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(IntegralLiteral expr) {
    sb.append("?");
    params.add(new SqlParameter(expr.getValue(), Types.INTEGER));
  }

  @Override
  public void visit(Int64Literal expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(IsofExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(LeExpression expr) {
    nextBetween.push(" <= ");
  }

  @Override
  public void visit(LengthMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(LtExpression expr) {
    nextBetween.push(" < ");
  }

  @Override
  public void visit(ModExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(MulExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(NeExpression expr) {
    nextBetween.push(" <> ");
  }

  @Override
  public void visit(NegateExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(NotExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(NullLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(OrExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(ParenExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(BoolParenExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(ReplaceMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(StartsWithMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(StringLiteral expr) {
    sb.append("?");
    params.add(new SqlParameter(expr.getValue(), Types.VARCHAR));
  }

  @Override
  public void visit(SubExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(SubstringMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(SubstringOfMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(TimeLiteral expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(ToLowerMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(ToUpperMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(TrimMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(YearMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(MonthMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(DayMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(HourMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(MinuteMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(SecondMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(RoundMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(FloorMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(CeilingMethodCallExpression expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(AggregateAnyFunction expr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(AggregateAllFunction expr) {
    throw new UnsupportedOperationException();
  }

}
package org.odata4j.expression;

public interface ExpressionVisitor {

  void beforeDescend();

  void afterDescend();

  void betweenDescend();

  void visit(String type);

  void visit(OrderByExpression expr);

  void visit(OrderByExpression.Direction direction);

  void visit(AddExpression expr);

  void visit(AndExpression expr);

  void visit(BooleanLiteral expr);

  void visit(CastExpression expr);

  void visit(ConcatMethodCallExpression expr);

  void visit(DateTimeLiteral expr);

  void visit(DateTimeOffsetLiteral expr);

  void visit(DecimalLiteral expr);

  void visit(DivExpression expr);

  void visit(EndsWithMethodCallExpression expr);

  void visit(EntitySimpleProperty expr);

  void visit(EqExpression expr);

  void visit(GeExpression expr);

  void visit(GtExpression expr);

  void visit(GuidLiteral expr);

  void visit(BinaryLiteral expr);

  void visit(ByteLiteral expr);

  void visit(SByteLiteral expr);

  void visit(IndexOfMethodCallExpression expr);

  void visit(SingleLiteral expr);

  void visit(DoubleLiteral expr);

  void visit(IntegralLiteral expr);

  void visit(Int64Literal expr);

  void visit(IsofExpression expr);

  void visit(LeExpression expr);

  void visit(LengthMethodCallExpression expr);

  void visit(LtExpression expr);

  void visit(ModExpression expr);

  void visit(MulExpression expr);

  void visit(NeExpression expr);

  void visit(NegateExpression expr);

  void visit(NotExpression expr);

  void visit(NullLiteral expr);

  void visit(OrExpression expr);

  void visit(ParenExpression expr);

  void visit(BoolParenExpression expr);

  void visit(ReplaceMethodCallExpression expr);

  void visit(StartsWithMethodCallExpression expr);

  void visit(StringLiteral expr);

  void visit(SubExpression expr);

  void visit(SubstringMethodCallExpression expr);

  void visit(SubstringOfMethodCallExpression expr);

  void visit(TimeLiteral expr);

  void visit(ToLowerMethodCallExpression expr);

  void visit(ToUpperMethodCallExpression expr);

  void visit(TrimMethodCallExpression expr);

  void visit(YearMethodCallExpression expr);

  void visit(MonthMethodCallExpression expr);

  void visit(DayMethodCallExpression expr);

  void visit(HourMethodCallExpression expr);

  void visit(MinuteMethodCallExpression expr);

  void visit(SecondMethodCallExpression expr);

  void visit(RoundMethodCallExpression expr);

  void visit(FloorMethodCallExpression expr);

  void visit(CeilingMethodCallExpression expr);

  void visit(AggregateAnyFunction expr);

  void visit(AggregateAllFunction expr);

}

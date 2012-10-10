package org.odata4j.producer.jpa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.core4j.Enumerable;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OEntityKey.KeyType;
import org.odata4j.core.OProperty;
import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.Expression;
import org.odata4j.expression.LiteralExpression;
import org.odata4j.expression.OrderByExpression;
import org.odata4j.expression.OrderByExpression.Direction;

public class JPASkipToken {

  public static String create(List<OrderByExpression> orderByList, OEntity lastEntity) {

    // skip token = <orderbyvalue1,orderbyvalue2,>keystringwithoutparens

    List<String> values = new LinkedList<String>();
    if (orderByList != null) {
      for (OrderByExpression orderBy : orderByList) {
        String orderByPropName = ((EntitySimpleProperty) orderBy.getExpression()).getPropertyName();
        Object orderByPropValue = lastEntity.getProperty(orderByPropName).getValue();
        String valueFilterString = Expression.asFilterString(Expression.literal(orderByPropValue));
        values.add(valueFilterString);
      }
    }

    values.add(lastEntity.getEntityKey().toKeyStringWithoutParentheses());
    return Enumerable.create(values).join(",");
  }

  public static BoolCommonExpression parse(String primaryKeyName, List<OrderByExpression> orderByList, String skipToken) {
    if (skipToken == null)
      return null;

    // kvalue
    // k > kvalue

    // avalue, kvalue
    // (a > avalue) or (a = avalue and k > kvalue)

    // avalue, bvalue, kvalue
    // (a > avalue) or (a = avalue and b > bvalue) or (a = avalue and b = bvalue and k > kvalue)

    // etc.

    List<BoolCommonExpression> predicates = new ArrayList<BoolCommonExpression>();
    List<LiteralExpression> orderByValues = new ArrayList<LiteralExpression>();

    // PASS1  (a > avalue), (b > bvalue), ... (k > kvalue)
    int start = 0;
    int end = 0;
    if (orderByList != null) {
      for (int i = 0; i < orderByList.size(); i++) {
        OrderByExpression orderBy = orderByList.get(i);
        end = skipToken.indexOf(',', start);
        String orderByValueString = skipToken.substring(start, end);
        LiteralExpression orderByValue = (LiteralExpression) Expression.parse(orderByValueString);
        orderByValues.add(orderByValue);
        // a > avalue
        BoolCommonExpression ordExp = orderBy.getDirection() == Direction.ASCENDING
            ? Expression.gt(orderBy.getExpression(), orderByValue)
            : Expression.lt(orderBy.getExpression(), orderByValue);

        predicates.add(ordExp);
        start = end + 1;
      }
    }

    //  k > kvalue
    OEntityKey entityKey = OEntityKey.parse(skipToken.substring(start));
    if (entityKey.getKeyType() == KeyType.SINGLE) {

      LiteralExpression entityKeyValue = Expression.literal(entityKey.asSingleValue());

      BoolCommonExpression keyPredicate = Expression.gt(
          Expression.simpleProperty(primaryKeyName),
          entityKeyValue);

      predicates.add(keyPredicate);
    } else {
      // complex key predicate

      // k > keyvalue actually means (k.a > kvalue.a) or (k.a = kvalue.a and k.b > kvalue.b) ...
      List<OProperty<?>> keyProperties = new ArrayList<OProperty<?>>(entityKey.asComplexProperties());
      BoolCommonExpression keyPredicate = null;
      for (int i = 0; i < keyProperties.size(); i++) {
        OProperty<?> keyProperty = keyProperties.get(i);
        // k.x > kvalue.x
        BoolCommonExpression subPredicate = Expression.gt(
            Expression.simpleProperty(primaryKeyName + "." + keyProperty.getName()),
            Expression.literal(keyProperty.getValue()));

        for (int j = 0; j < i; j++) {
          OProperty<?> earlierKeyProperty = keyProperties.get(j);
          // k.x = kvalue.x
          BoolCommonExpression eq = Expression.eq(
              Expression.simpleProperty(primaryKeyName + "." + earlierKeyProperty.getName()),
              Expression.literal(earlierKeyProperty.getValue()));
          subPredicate = Expression.and(eq, subPredicate);
        }
        if (keyPredicate == null)
          keyPredicate = subPredicate;
        else
          keyPredicate = Expression.or(keyPredicate, subPredicate);
      }

      predicates.add(keyPredicate);
    }

    // PASS2  (a > avalue), (a = avalue and b > bvalue), ... (a = avalue and b = bvalue ... and k > kvalue)
    for (int i = 1; i < predicates.size(); i++) {
      BoolCommonExpression predicate = predicates.get(i);
      for (int j = 0; j < i; j++) {
        OrderByExpression orderBy = orderByList.get(j);
        BoolCommonExpression eq = Expression.eq(orderBy.getExpression(), orderByValues.get(j));
        predicate = Expression.and(eq, predicate);
      }
      predicates.set(i, Expression.boolParen(predicate));
    }

    // return all predicates OR'ed together
    BoolCommonExpression rt = predicates.get(0);
    for (int i = 1; i < predicates.size(); i++)
      rt = Expression.or(rt, predicates.get(i));
    return Expression.boolParen(rt);
  }

}

package org.odata4j.producer.jdbc;

import org.core4j.Enumerable;
import org.odata4j.core.Action1;
import org.odata4j.producer.jdbc.JdbcModel.JdbcSchema;

public class LimitJdbcModelToDefaultSchema implements Action1<JdbcModel> {

  @Override
  public void apply(JdbcModel model) {
    for (JdbcSchema schema : Enumerable.create(model.schemas).toList()) {
      if (!schema.isDefault)
        model.schemas.remove(schema);
    }
  }

}

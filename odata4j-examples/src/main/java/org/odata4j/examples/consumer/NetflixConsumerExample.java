package org.odata4j.examples.consumer;

import java.util.List;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperty;
import org.odata4j.examples.AbstractExample;

public class NetflixConsumerExample extends AbstractExample {

  public static void main(String[] args) {
    NetflixConsumerExample example = new NetflixConsumerExample();
    example.run(args);
  }

  private void run(String[] args) {

    ODataConsumer c = ODataConsumers.create(ODataEndpoints.NETFLIX);

    // locate the netflix id for Morgan Spurlock
    int morganSpurlockId = c.getEntities("People").filter("substringof('Spurlock',Name)").execute().first().getProperty("Id", Integer.class).getValue();

    // lookup and print all titles he's acted in
    List<OEntity> titlesActedIn = c.getEntities("People").nav(morganSpurlockId, "TitlesActedIn").execute().toList();
    for (OEntity title : titlesActedIn) {
      for (OProperty<?> p : title.getProperties()) {
        report("%s: %s", p.getName(), p.getValue());
      }
      report("\n");
    }
    report("count: " + titlesActedIn.size());
  }

}

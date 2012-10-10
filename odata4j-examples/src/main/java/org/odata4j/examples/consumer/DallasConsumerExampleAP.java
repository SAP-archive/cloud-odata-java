package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.examples.AbstractExample;

public class DallasConsumerExampleAP extends AbstractExample {

  public static void main(String[] args) {
    DallasConsumerExampleAP example = new DallasConsumerExampleAP();
    example.run(args);
  }

  private void run(String[] args) {

    String[] dallasCreds = args.length > 0 ? args : System.getenv("DALLAS").split(":");
    OClientBehavior basicAuth = OClientBehaviors.basicAuth("accountKey", dallasCreds[0]);

    ODataConsumer c = ODataConsumers.newBuilder(ODataEndpoints.DALLAS_CTP3_AP).setClientBehaviors(basicAuth).build();

    // all breaking news categories
    reportEntities(c, "GetBreakingNewsCategories", 1000);

    // stories by category: top 5 tech stories
    int topTechCategoryId = 31992;
    String mediaOptionNoPictures = "0";
    //    String mediaOptionPictures = "1";
    String contentOptionLinksOnly = "0";
    //    String contentOptionFullStoryContent = "2";
    reportEntities("Tech", c.getEntities("GetBreakingNewsContentByCategory")
        .custom("CategoryId", "" + topTechCategoryId)
        .custom("MediaOption", mediaOptionNoPictures)
        .custom("ContentOption", contentOptionLinksOnly)
        .custom("Count", "5")
        .execute());

    // stories by keyword: first story for "obama"
    reportEntities("Search", c.getEntities("SearchNewsByKeyword")
        .custom("MediaOption", mediaOptionNoPictures)
        .custom("SearchTerms", "'obama'")
        .execute()
        .take(1));
  }

}

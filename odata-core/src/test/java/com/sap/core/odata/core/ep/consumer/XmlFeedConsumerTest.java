/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.feed.FeedMetadata;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlFeedConsumerTest extends AbstractConsumerTest {

  @Test
  public void readEmployeesFeedWithInlineCountValid() throws Exception {
    // prepare
    String content = readFile("feed_employees_full.xml");
    assertNotNull(content);

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream reqContent = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    EntityProviderReadProperties consumerProperties = EntityProviderReadProperties.init()
        .mergeSemantic(false).build();

    ODataFeed feed = xec.readFeed(entitySet, reqContent, consumerProperties);
    assertNotNull(feed);

    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);

    int inlineCount = feedMetadata.getInlineCount();
    //Null means no inlineCount found
    assertNotNull(inlineCount);

    assertEquals(6, inlineCount);
  }

  @Test(expected = EntityProviderException.class)
  public void readEmployeesFeedWithInlineCountNegative() throws Exception {
    // prepare
    String content = readFile("feed_employees_full.xml").replace("<m:count>6</m:count>", "<m:count>-1</m:count>");
    ;
    assertNotNull(content);

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream reqContent = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    EntityProviderReadProperties consumerProperties = EntityProviderReadProperties.init()
        .mergeSemantic(false).build();

    try {
      xec.readFeed(entitySet, reqContent, consumerProperties);
    } catch (EntityProviderException e) {
      assertEquals(EntityProviderException.INLINECOUNT_INVALID, e.getMessageReference());
      throw e;
    }

    Assert.fail("Exception expected");
  }

  @Test(expected = EntityProviderException.class)
  public void readEmployeesFeedWithInlineCountLetters() throws Exception {
    // prepare
    String content = readFile("feed_employees_full.xml").replace("<m:count>6</m:count>", "<m:count>AAA</m:count>");
    ;
    assertNotNull(content);

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream reqContent = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    EntityProviderReadProperties consumerProperties = EntityProviderReadProperties.init()
        .mergeSemantic(false).build();

    try {
      xec.readFeed(entitySet, reqContent, consumerProperties);
    } catch (EntityProviderException e) {
      assertEquals(EntityProviderException.INLINECOUNT_INVALID, e.getMessageReference());
      throw e;
    }

    Assert.fail("Exception expected");
  }
}

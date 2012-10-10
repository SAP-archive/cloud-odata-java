package org.odata4j.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.format.xml.XmlFormatWriter;
import org.odata4j.internal.InternalUtil;

/**
 * Shared consumer request implementation for operations with an entity as the request payload.
 */
public abstract class AbstractConsumerEntityPayloadRequest {

  protected final List<OProperty<?>> props = new ArrayList<OProperty<?>>();
  protected final List<OLink> links = new ArrayList<OLink>();

  protected final String entitySetName;
  protected final String serviceRootUri;
  protected final EdmDataServices metadata;

  protected AbstractConsumerEntityPayloadRequest(String entitySetName, String serviceRootUri, EdmDataServices metadata) {
    this.entitySetName = entitySetName;
    this.serviceRootUri = serviceRootUri;
    this.metadata = metadata;
  }

  protected <T> T properties(T rt, OProperty<?>... props) {
    for (OProperty<?> prop : props)
      this.props.add(prop);
    return rt;
  }

  protected <T> T properties(T rt, Iterable<OProperty<?>> props) {
    for (OProperty<?> prop : props)
      this.props.add(prop);
    return rt;
  }

  protected <T> T link(T rt, String navProperty, OEntity target) {
    return link(rt, navProperty, target.getEntitySet(), target.getEntityKey());
  }

  protected <T> T link(T rt, String navProperty, OEntityKey targetKey) {
    return link(rt, navProperty, null, targetKey);
  }

  private <T> T link(T rt, String navProperty, EdmEntitySet targetEntitySet, OEntityKey targetKey) {
    EdmEntitySet entitySet = metadata.getEdmEntitySet(entitySetName);
    EdmNavigationProperty navProp = entitySet.getType().findNavigationProperty(navProperty);
    if (navProp == null)
      throw new IllegalArgumentException("unknown navigation property " + navProperty);

    if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY)
      throw new IllegalArgumentException("many associations are not supported");

    StringBuilder href = new StringBuilder(serviceRootUri);
    if (!serviceRootUri.endsWith("/"))
      href.append("/");

    if (targetEntitySet == null)
      targetEntitySet = metadata.getEdmEntitySet(navProp.getToRole().getType());

    href.append(InternalUtil.getEntityRelId(targetEntitySet, targetKey));

    // TODO get rid of XmlFormatWriter
    // We may need to rethink the rel property on a link
    // since it adds no new information. The title is
    // already there and rel has only a fixed prefix valid for
    // the atom format.
    String rel = XmlFormatWriter.related + navProperty;

    this.links.add(OLinks.relatedEntity(rel, navProperty, href.toString()));
    return rt;
  }

  protected <T> T inline(T rt, String navProperty, OEntity... entities) {
    EdmEntitySet entitySet = metadata.getEdmEntitySet(entitySetName);
    EdmNavigationProperty navProp = entitySet.getType().findNavigationProperty(navProperty);
    if (navProp == null)
      throw new IllegalArgumentException("unknown navigation property " + navProperty);

    // TODO get rid of XmlFormatWriter
    String rel = XmlFormatWriter.related + navProperty;
    String href = entitySetName + "/" + navProperty;
    if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
      links.add(OLinks.relatedEntitiesInline(rel, navProperty, href, Arrays.asList(entities)));
    } else {
      if (entities.length > 1)
        throw new IllegalArgumentException("only one entity is allowed for this navigation property " + navProperty);

      links.add(OLinks.relatedEntityInline(rel, navProperty, href,
          entities.length > 0 ? entities[0] : null));
    }

    return rt;
  }

}

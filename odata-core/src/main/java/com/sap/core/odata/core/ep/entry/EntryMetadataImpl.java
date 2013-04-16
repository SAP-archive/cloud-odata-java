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
package com.sap.core.odata.core.ep.entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ep.entry.EntryMetadata;

/**
 * @author SAP AG
 */
public class EntryMetadataImpl implements EntryMetadata {
  private String id;
  private String etag;
  private String uri;
  private Map<String, List<String>> associationUris = new HashMap<String, List<String>>();

  @Override
  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  @Override
  public String getEtag() {
    return etag;
  }

  public void setEtag(final String etag) {
    this.etag = etag;
  }

  @Override
  public String getUri() {
    return uri;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  @Override
  public List<String> getAssociationUris(final String navigationPropertyName) {
    final List<String> uris = associationUris.get(navigationPropertyName);
    if (uris == null) {
      return Collections.emptyList();
    } else {
      return Collections.unmodifiableList(uris);
    }
  }

  public void putAssociationUri(final String navigationPropertyName, final String uri) {
    List<String> uris = associationUris.get(navigationPropertyName);
    if (uris == null) {
      uris = new ArrayList<String>();
    }
    uris.add(uri);
    associationUris.put(navigationPropertyName, uris);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EntryMetadataImpl [id=" + id + ", etag=" + etag + ", uri=" + uri + ", associationUris=" + associationUris + "]";
  }
}

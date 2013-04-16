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

import com.sap.core.odata.api.ep.entry.MediaMetadata;

public class MediaMetadataImpl implements MediaMetadata {

  private String sourceLink;
  private String etag;
  private String contentType;
  private String editLink;

  @Override
  public String getSourceLink() {
    return sourceLink;
  }

  @Override
  public String getEtag() {
    return etag;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public String getEditLink() {
    return editLink;
  }

  public void setSourceLink(final String sourceLink) {
    this.sourceLink = sourceLink;
  }

  public void setEtag(final String etag) {
    this.etag = etag;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public void setEditLink(final String editLink) {
    this.editLink = editLink;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MediaMetadataImpl [sourceLink=" + sourceLink + ", etag=" + etag + ", contentType=" + contentType + ", editLink=" + editLink + "]";
  }
}

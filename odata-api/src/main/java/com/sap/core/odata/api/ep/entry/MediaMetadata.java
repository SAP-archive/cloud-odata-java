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
package com.sap.core.odata.api.ep.entry;

/**
 * {@link MediaMetadata} contains all metadata for media related entries.
 */
public interface MediaMetadata {

  /**
   * Get <code>edit link</code>.
   * 
   * @return <code>edit link</code>.
   */
  public abstract String getEditLink();

  /**
   * Get <code>content type</code> in as specified in 
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">RFC 2616 Section 14</a>.
   * 
   * @return <code>content type</code>.
   */
  public abstract String getContentType();

  /**
   * Get <code>etag</code>.
   * 
   * @return <code>etag</code>.
   */
  public abstract String getEtag();

  /**
   * Get <code>source link</code>.
   * 
   * @return <code>source link</code>.
   */
  public abstract String getSourceLink();
}

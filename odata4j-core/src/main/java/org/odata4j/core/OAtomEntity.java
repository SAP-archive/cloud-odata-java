package org.odata4j.core;

import org.joda.time.LocalDateTime;

/**
 * Interface providing information for ATOM serialization.
 *
 * <p>Enables feed customization of ATOM entries for certain properties instead of using &lt;m:Properties&gt;
 */
public interface OAtomEntity extends OExtension<OEntity> {

  /**
   * @return Atom Entity title or null
   */
  String getAtomEntityTitle();

  /**
   * If null returned, there will be no &lt;summary&gt; element in the response
   * @return Atom Entity title or null
   */
  String getAtomEntitySummary();

  /**
   * @return Atom Entity author value or null
   */
  String getAtomEntityAuthor();

  /**
   * @return Atom Entity updated value or null to use current date
   */
  LocalDateTime getAtomEntityUpdated();

}

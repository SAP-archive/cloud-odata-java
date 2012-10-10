package org.odata4j.core;

/**
 * Additional Atom information.
 */
public interface AtomInfo extends Titled {

  /**
   * Gets the Atom title.
   *
   * @return the Atom title
   */
  String getTitle();

  /**
   * Gets the Atom category term.
   *
   * @return the Atom category term
   */
  String getCategoryTerm();
}

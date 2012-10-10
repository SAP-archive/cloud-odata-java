package org.odata4j.format;

/**
 * <code>Feed</code> and <code>Entry</code> are buildings block for the ODATA payload.
 * There are differences between the Atom and Json format. These interfaces are used
 * where we can handle both formats the same way.
 */
public interface Feed {

  String getNext();

  Iterable<Entry> getEntries();
}

package com.sap.core.odata.api.processor;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public interface PathSegments {

  public boolean contains(Object o);

  public boolean containsAll(Collection<?> c);

  public PathSegment get(int index);

  public int indexOf(Object o);

  public boolean isEmpty();

  public Iterator<PathSegment> iterator();

  public int lastIndexOf(Object o);

  public ListIterator<PathSegment> listIterator();

  public ListIterator<PathSegment> listIterator(int index);

  public int size();

  public PathSegments subList(int fromIndex, int toIndex);

  public Object[] toArray();

  public <T> T[] toArray(T[] a);
}

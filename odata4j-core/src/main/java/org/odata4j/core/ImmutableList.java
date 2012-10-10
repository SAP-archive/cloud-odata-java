package org.odata4j.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * A List implementation whose structure is immutable after construction.
 * <p>Useful for apis that assume a returned list remains unmodified.
 * <p>All mutation methods throw <code>UnsupportedOperationException</code>
 *
 * @param <T>  the list item type
 */
public class ImmutableList<T> implements List<T>, RandomAccess {

  private final List<T> values;

  private static final ImmutableList<Object> EMPTY = new ImmutableList<Object>(Collections.emptyList());

  private ImmutableList(List<T> values) {
    this.values = values;
  }

  @SuppressWarnings("unchecked")
  public static <T> ImmutableList<T> create(T... values) {
    if (values.length == 0)
      return (ImmutableList<T>) EMPTY;
    return new ImmutableList<T>(Arrays.asList(values));
  }

  @SuppressWarnings("unchecked")
  public static <T> ImmutableList<T> copyOf(List<T> values) {
    if (values == null)
      return (ImmutableList<T>) EMPTY;
    if (values instanceof ImmutableList)
      return (ImmutableList<T>) values;
    return (ImmutableList<T>) create(values.toArray());
  }

  @Override
  public String toString() {
    return values.toString();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof ImmutableList) && ((ImmutableList<?>) obj).values.equals(values);
  }

  @Override
  public int hashCode() {
    return values.hashCode();
  }

  @Override
  public int size() {
    return values.size();
  }

  @Override
  public boolean isEmpty() {
    return values.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return values.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return values.iterator();
  }

  @Override
  public Object[] toArray() {
    return values.toArray();
  }

  @Override
  public <TArray> TArray[] toArray(TArray[] a) {
    return values.toArray(a);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return values.containsAll(c);
  }

  @Override
  public T get(int index) {
    return values.get(index);
  }

  @Override
  public int indexOf(Object o) {
    return values.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return values.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return values.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return values.listIterator(index);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return values.subList(fromIndex, toIndex);
  }

  private static UnsupportedOperationException newModificationUnsupported() {
    return new UnsupportedOperationException(ImmutableList.class.getSimpleName() + " cannot be modified");
  }

  @Override
  public void clear() {
    throw newModificationUnsupported();
  }

  @Override
  public T set(int paramInt, T paramE) {
    throw newModificationUnsupported();
  }

  @Override
  public void add(int paramInt, T paramE) {
    throw newModificationUnsupported();
  }

  @Override
  public T remove(int paramInt) {
    throw newModificationUnsupported();
  }

  @Override
  public boolean add(T paramE) {
    throw newModificationUnsupported();
  }

  @Override
  public boolean remove(Object paramObject) {
    throw newModificationUnsupported();
  }

  @Override
  public boolean addAll(Collection<? extends T> paramCollection) {
    throw newModificationUnsupported();
  }

  @Override
  public boolean addAll(int paramInt, Collection<? extends T> paramCollection) {
    throw newModificationUnsupported();
  }

  @Override
  public boolean removeAll(Collection<?> paramCollection) {
    throw newModificationUnsupported();
  }

  @Override
  public boolean retainAll(Collection<?> paramCollection) {
    throw newModificationUnsupported();
  }

}

package org.odata4j.producer;

import java.util.Arrays;

/**
 * A path in an object graph made up of property names.
 */
public class PropertyPath {

  private String[] pathComponents;
  private String pathString;

  public PropertyPath(String path) {
    this.pathString = path;
    this.pathComponents = path.isEmpty() ? null : path.split("/");
  }

  public PropertyPath(PropertyPath path) {
    this.pathString = path.pathString;
    this.pathComponents = path.isEmpty() ? null : Arrays.<String> copyOf(path.pathComponents, path.pathComponents.length);
  }

  public int getNComponents() {
    return isEmpty() ? 0 : pathComponents.length;
  }

  public boolean isEmpty() {
    return pathComponents == null;
  }

  // components numbered from 0
  public String getNthComponent(int n) {
    if (n < 0 || n > (getNComponents() - 1)) {
      throw new java.lang.IndexOutOfBoundsException();
    }
    return pathComponents[n];
  }

  public String getPath() {
    return pathString;
  }

  public PropertyPath addComponent(String component) {
    return new PropertyPath(pathString.isEmpty() ? component : (pathString + "/" + component));
  }

  public PropertyPath removeLastComponent() {
    if (isEmpty()) {
      return this;
    } else if (this.getNComponents() == 1) {
      return new PropertyPath("");
    } else {
      StringBuilder sb = new StringBuilder(pathComponents[0]);
      for (int i = 1; i < pathComponents.length - 1; i++) {
        sb.append("/").append(pathComponents[i]);
      }
      return new PropertyPath(sb.toString());
    }
  }

  public PropertyPath removeFirstComponent() {
    if (isEmpty()) {
      return this;
    } else if (this.getNComponents() == 1) {
      return new PropertyPath("");
    } else {
      StringBuilder sb = new StringBuilder(pathComponents[1]);
      for (int i = 2; i < pathComponents.length; i++) {
        sb.append("/").append(pathComponents[i]);
      }
      return new PropertyPath(sb.toString());
    }
  }

  public String getFirstComponent() {
    return isEmpty() ? null : pathComponents[0];
  }

  public String getLastComponent() {
    return isEmpty() ? null : pathComponents[pathComponents.length - 1];
  }

  public boolean isWild() {
    // roar!
    return !isEmpty() && getLastComponent().equals("*");
  }

  @Override
  public boolean equals(Object rhso) {
    if (rhso == null || !(rhso instanceof PropertyPath)) {
      return false;
    }
    PropertyPath rhs = (PropertyPath) rhso;

    return pathString.equals(rhs.pathString);
  }

  @Override
  public int hashCode() {
    return pathString.hashCode();
  }

  @Override
  public String toString() {
    return pathString;
  }

  public boolean startsWith(PropertyPath p) {
    if (this.getNComponents() < p.getNComponents()) {
      return false;
    }

    for (int i = 0; i < p.getNComponents(); i++) {
      if (!this.pathComponents[i].equals(p.getNthComponent(i))) {
        return false;
      }
    }
    return true;
  }

}

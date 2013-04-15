/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.testutil.helper;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author SAP AG
 */
public class ClassHelper {
  public static final FileFilter JAVA_FILE_FILTER = new FileFilter() {
    @Override
    public boolean accept(final File path) {
      return path.isFile() && path.getName().toLowerCase(Locale.ROOT).endsWith("class");
    }
  };
  public static final String CLASS_FILE_ENDING = ".class";
  private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  /**
   * 
   * @param exClasses
   */
  public static <T> List<T> getClassInstances(final List<Class<T>> exClasses) {
    return getClassInstances(exClasses, EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
  }

  /**
   * 
   * @param exClasses
   * @param ctorParameters
   */
  public static <T> List<T> getClassInstances(final List<Class<T>> exClasses, final Object... ctorParameters) {
    final List<Class<?>> ctorParameterClasses = new ArrayList<Class<?>>();
    for (final Object object : ctorParameters) {
      ctorParameterClasses.add(object.getClass());
    }

    return getClassInstances(exClasses, ctorParameterClasses.toArray(new Class<?>[0]), ctorParameters);
  }

  /**
   * @param exClasses
   * @param ctorParameterClasses
   * @param ctorParameters
   */
  public static <T> List<T> getClassInstances(final List<Class<T>> exClasses, final Class<?>[] ctorParameterClasses, final Object[] ctorParameters) {

    final List<T> toTestExceptions = new ArrayList<T>();
    for (final Class<T> clazz : exClasses) {
      if (isNotAbstractOrInterface(clazz)) {
        Constructor<T> ctor;
        try {
          ctor = clazz.getConstructor(ctorParameterClasses);
          final T ex = ctor.newInstance(ctorParameters);
          toTestExceptions.add(ex);
        } catch (final SecurityException e) {
          continue;
        } catch (final NoSuchMethodException e) {
          continue;
        } catch (final IllegalArgumentException e) {
          continue;
        } catch (final InstantiationException e) {
          continue;
        } catch (final IllegalAccessException e) {
          continue;
        } catch (final InvocationTargetException e) {
          continue;
        }
      }
    }
    return toTestExceptions;
  }

  /**
   * @param clazz
   */
  public static boolean isNotAbstractOrInterface(final Class<?> clazz) {
    return !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers());
  }

  /**
   * @param packageName
   * @param assignableToClass
   */
  public static <T> List<Class<T>> getAssignableClasses(final String packageName, final Class<T> assignableToClass) {
    final List<Class<T>> foundClasses = new ArrayList<Class<T>>();
    final URL url = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

    final File path = new File(url.getPath());
    if (path.exists()) {
      final File[] javaFiles = path.listFiles(JAVA_FILE_FILTER);
      for (final File file : javaFiles) {
        final Class<T> clazz = getClass(file, packageName, assignableToClass);
        if (clazz != null) {
          foundClasses.add(clazz);
        }
      }
    }

    return foundClasses;
  }

  /**
   * @param file
   * @param packageName
   * @param clazz
   */
  public static <T> Class<T> getClass(final File file, final String packageName, final Class<T> clazz) {
    String className = file.getName();
    if (className.endsWith(CLASS_FILE_ENDING)) {
      className = className.substring(0, className.length() - CLASS_FILE_ENDING.length());
    }

    return getClass(packageName + "." + className, clazz);
  }

  /**
   * @param className
   * @param clazz
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getClass(final String className, final Class<T> clazz) {
    try {
      final Class<?> clazzForName = Class.forName(className);
      if (clazz.isAssignableFrom(clazzForName)) {
        return (Class<T>) clazzForName;
      }
    } catch (final ClassNotFoundException e) {
      return null;
    }
    return null;
  }
}

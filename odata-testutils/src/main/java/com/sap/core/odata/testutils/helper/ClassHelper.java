package com.sap.core.odata.testutils.helper;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SAP AG
 */
public class ClassHelper {
  public static final FileFilter JAVA_FILE_FILTER = new FileFilter() {
    @Override
    public boolean accept(File path) {
      return path.isFile() && path.getName().toLowerCase().endsWith("class");
    }
  };
  public static final String CLASS_FILE_ENDING = ".class";
  private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  /**
   * 
   * @param exClasses
   * @return
   */
  public static <T> List<T> getClassInstances(List<Class<T>> exClasses) {
    return getClassInstances(exClasses, EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
  }

  /**
   * 
   * @param exClasses
   * @param ctorParameters
   * @return
   */
  public static <T> List<T> getClassInstances(List<Class<T>> exClasses, Object... ctorParameters) {
    List<Class<?>> ctorParameterClasses = new ArrayList<Class<?>>();
    for (Object object : ctorParameters) {
      ctorParameterClasses.add(object.getClass());
    }

    return getClassInstances(exClasses, ctorParameterClasses.toArray(new Class<?>[0]), ctorParameters);
  }

  /**
   * @param exClasses
   * @param ctorParameterClasses
   * @param ctorParameters
   * @return
   */
  public static <T> List<T> getClassInstances(List<Class<T>> exClasses, Class<?>[] ctorParameterClasses, Object[] ctorParameters) {

    List<T> toTestExceptions = new ArrayList<T>();
    for (Class<T> clazz : exClasses) {
      if (isNotAbstractOrInterface(clazz)) {
        Constructor<T> ctor;
        try {
          ctor = clazz.getConstructor(ctorParameterClasses);
          T ex = ctor.newInstance(ctorParameters);
          toTestExceptions.add(ex);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {}
      }
    }
    return toTestExceptions;
  }

  /**
   * @param clazz
   * @return
   */
  public static boolean isNotAbstractOrInterface(Class<?> clazz) {
    return !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers());
  }

  /**
   * @param packageName
   * @param assignableToClass
   * @return
   */
  public static <T> List<Class<T>> getAssignableClasses(String packageName, Class<T> assignableToClass) {
    List<Class<T>> foundClasses = new ArrayList<Class<T>>();
    URL url = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

    File path = new File(url.getPath());
    if (path.exists()) {
      File[] javaFiles = path.listFiles(JAVA_FILE_FILTER);
      for (File file : javaFiles) {
        Class<T> clazz = getClass(file, packageName, assignableToClass);
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
   * @return
   */
  public static <T> Class<T> getClass(File file, String packageName, Class<T> clazz) {
    String className = file.getName();
    if (className.endsWith(CLASS_FILE_ENDING)) {
      className = className.substring(0, className.length() - CLASS_FILE_ENDING.length());
    }

    return getClass(packageName + "." + className, clazz);
  }

  /**
   * @param className
   * @param clazz
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getClass(String className, Class<T> clazz) {
    try {
      Class<?> clazzForName = Class.forName(className);
      if (clazz.isAssignableFrom(clazzForName)) {
        return (Class<T>) clazzForName;
      }
    } catch (ClassNotFoundException e) {
      return null;
    }
    return null;
  }
}

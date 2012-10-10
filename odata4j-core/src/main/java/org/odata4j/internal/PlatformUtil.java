package org.odata4j.internal;

public class PlatformUtil {

  private static boolean RUNNING_ON_ANDROID;

  static {
    try {
      Class.forName("android.app.Activity");
      RUNNING_ON_ANDROID = true;
    } catch (Exception e) {
      RUNNING_ON_ANDROID = false;
    }

    if (runningOnAndroid()) {
      androidInit();
    }
  }

  private static void androidInit() {

  }

  public static boolean runningOnAndroid() {
    return RUNNING_ON_ANDROID;
  }

}

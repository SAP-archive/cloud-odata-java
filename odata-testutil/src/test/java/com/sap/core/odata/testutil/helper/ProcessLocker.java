package com.sap.core.odata.testutil.helper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interprocess synchronization to enable parallel test executions.
 * 
 * @author SAP AG
 */
public class ProcessLocker {

  @SuppressWarnings("unused")
  private static final Logger log = LoggerFactory.getLogger(ProcessLocker.class);

  //Acquire
  public static void crossProcessLockAcquire(final Class<?> c, final long waitMS) {
    RandomAccessFile randomAccessFile = null;
    if ((fileLock == null) && (c != null) && (waitMS > 0)) {
      try {
        final long dropDeadTime = System.currentTimeMillis() + waitMS;
        final File file = new File(lockTempDir, c.getName() + ".lock");
        randomAccessFile = new RandomAccessFile(file, "rw");
        final FileChannel fileChannel = randomAccessFile.getChannel();
        while (System.currentTimeMillis() < dropDeadTime) {
          fileLock = fileChannel.tryLock();
          if (fileLock != null) {
            break;
          }
          Thread.sleep(250); // 4 attempts/sec
        }
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }
    if (fileLock == null) {
      throw new RuntimeException("timeout after " + waitMS);
    }
  }

  // Release
  public static void crossProcessLockRelease() {
    if (fileLock != null) {
      try {
        fileLock.release();
        fileLock = null;
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static File lockTempDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "locks");
  static {
    lockTempDir.mkdirs();
  }
  private static FileLock fileLock = null;

  static {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        crossProcessLockRelease();
      }
    });
  }
}

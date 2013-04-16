/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.testutil.helper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.testutil.TestUtilRuntimeException;

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
        throw new TestUtilRuntimeException(e);
      }
    }
    if (fileLock == null) {
      throw new TestUtilRuntimeException("timeout after " + waitMS);
    }
  }

  // Release
  public static void crossProcessLockRelease() {
    if (fileLock != null) {
      try {
        fileLock.release();
        fileLock = null;
      } catch (final IOException e) {
        throw new TestUtilRuntimeException(e);
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

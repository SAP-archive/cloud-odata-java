/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.testutil.tool.core;

import java.util.Set;

public interface TestResultFilter {

  /**
   * If <code>true</code> <b>use</b> this {@link TestResult} set,
   * otherwise if <code>false</code> <b>ignore</b> this {@link TestResult} set.
   * 
   * @param results to be filtered/checked {@link TestResult}
   * @return <code>true</code> to <b>use</b> or <code>false</code> to <b>ignore</b> this {@link TestResult}.
   */
  boolean filterResults(Set<TestResult> results);
}

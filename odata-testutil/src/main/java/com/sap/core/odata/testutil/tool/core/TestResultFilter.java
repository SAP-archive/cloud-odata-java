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

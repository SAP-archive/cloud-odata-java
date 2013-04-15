/**
 * (c) 2013 by SAP AG
 */
/**
 * Entity Provider Callbacks<p>
 * These callbacks will be used to support the $expand query option. Callbacks have to implement the {@link com.sap.core.odata.api.ODataCallback} as a marker. 
 * <br>To support an expanded entry the {@link com.sap.core.odata.api.ep.callback.OnWriteEntryContent} interface has to be implemented.
 * <br>To support an expanded feed the {@link com.sap.core.odata.api.ep.callback.OnWriteFeedContent} interface has to be implemented.
 * 
 * <p>All callbacks are registered for a navigation property in a HashMap<String as navigation property name, callback for this navigation property> and will only be called if a matching $expand clause is found.
 */
package com.sap.core.odata.api.ep.callback;
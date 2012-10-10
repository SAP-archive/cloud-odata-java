package org.odata4j.producer;

/**
 * quote from an email from John Spurlock:
 * 
 * I agree accessing request context inside a producer is something we should address,
 * been thinking about this for a while.  My preference would be to keep the changes 
 * minimal to the ODataProducer interface, and maintain the principle of keeping it 
 * free of jax-rs apis.  That is, keep the layer separation intact instead of 
 * starting to mix them together at this point.
 * 
 * In the interest of minimal changes, I'd rather not radically change each producer 
 * method to remove the individual args and take a single method-specific request object arg.  
 * Implementations, however are free to do so under the hood if they choose.  
 * In fact, that's what I'm doing with the new jdbc producer.
 * 
 * My current thinking is that we should add a new single common arg as the 
 * first arg of every producer method:  ODataContext context.
 * e.g. callFunction(ODataContext context, EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo
 * 
 * Now what does the ODataContext interface look like?  It should make the common context 
 * scenarios simple (like security, which is the main scenarios folks are wanting), 
 * leave advanced scenarios possible, and stay jax-rs free in and of itself.  
 * Instead of having a large surface area, I think a simple service interface would 
 * be a better fit.   context.get(OSecurityContext.class) etc.  This would allow 
 * us to have common services provided in the framework (e.g. for security) and 
 * leave it possible for accessing implementation-specific objects as a workaround.  
 * e.g. context.get(HttpHeaders.class)  etc.  This would obviously be discouraged and 
 * common use cases moved into the framework.  
 * But we should make it possible, which is better than where we are now.
 * 
 */
public interface ODataContext {

  /**
   * Get an aspect of the overall context in which the OData request is being
   * executed.
   * @param <T> - the type of the context aspect
   * @param contextClass
   * @return the object representing the requested context aspect.
   */
  <T> T getContextAspect(Class<T> contextClass);

  /**
   * Get the request headers of the current request. 
   * @return 
   */
  ODataHeadersContext getRequestHeadersContext();
}

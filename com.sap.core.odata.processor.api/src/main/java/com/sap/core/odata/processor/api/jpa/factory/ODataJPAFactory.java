package com.sap.core.odata.processor.api.jpa.factory;

/**
 * The class is an abstract factory for creating default ODataJPAFactory. The
 * class's actual implementation is responsible for creating other factory
 * implementations.The class creates factories implementing interfaces
 * <ul>
 * <li>{@link com.sap.core.odata.processor.api.jpa.factory.JPAAccessFactory}</li>
 * <li>{@link com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory}</li>
 * <li>{@link com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory}</li>
 * </ul>
 * 
 * <b>Note: </b>Extend this class only if you don't require library's default
 * factory implementation.
 * <p>
 * 
 * @author SAP AG
 * 
 * 
 * 
 */
public abstract class ODataJPAFactory {

	private static final String IMPLEMENTATION = "com.sap.core.odata.processor.core.jpa.factory.ODataJPAFactoryImpl";
	private static ODataJPAFactory factoryImpl;

	/**
	 * Method creates a factory instance. The instance returned is singleton.
	 * The instance of this factory can be used for creating other factory
	 * implementations.
	 * 
	 * @return instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory}
	 *         .
	 */
	public static ODataJPAFactory createFactory() {

		if (factoryImpl != null)
			return factoryImpl;
		else {
			try {
				Class<?> clazz = Class.forName(ODataJPAFactory.IMPLEMENTATION);

				Object object = clazz.newInstance();
				factoryImpl = (ODataJPAFactory) object;

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return factoryImpl;
		}
	}

	/**
	 * The method returns a null reference to JPQL Builder Factory. Override
	 * this method to return an implementation of JPQLBuilderFactory if default
	 * implementation from library is not required.
	 * 
	 * @return instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory}
	 */
	public JPQLBuilderFactory getJPQLBuilderFactory() {
		return null;
	};

	/**
	 * The method returns a null reference to JPA Access Factory. Override this
	 * method to return an implementation of JPAAccessFactory if default
	 * implementation from library is not required.
	 * 
	 * @return instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory}
	 */
	public JPAAccessFactory getJPAAccessFactory() {
		return null;
	};

	/**
	 * The method returns a null reference to OData JPA Access Factory. Override
	 * this method to return an implementation of ODataJPAAccessFactory if
	 * default implementation from library is not required.
	 * 
	 * @return instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.factory.ODataJPAAccessFactory}
	 */
	public ODataJPAAccessFactory getODataJPAAccessFactory() {
		return null;
	};

}

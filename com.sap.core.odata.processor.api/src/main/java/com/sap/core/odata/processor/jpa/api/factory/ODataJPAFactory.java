package com.sap.core.odata.processor.jpa.api.factory;

/**
 * <b>
 * <p>
 * ------------------------------ DO NOT EXTEND THIS CLASS
 * ------------------------------
 * </p>
 * </b>
 * 
 * The class is an abstract factory for creating factory implementations. The
 * class's actual implementation is responsible for creating concrete factory
 * implementations.
 * 
 * The class creates factories implementing interfaces
 * <p>
 * </p>
 * <li>{@link JPAAccessFactory}</li> <li>{@link JPQLBuilderFactory}</li> <li>
 * {@link ODataJPAAccessFactory}</li>
 * 
 * @author SAP AG
 * 
 */
public abstract class ODataJPAFactory {

	private static final String IMPLEMENTATION = "com.sap.core.odata.processor.jpa.factory.ODataJPAFactoryImpl";
	private static ODataJPAFactory factoryImpl;

	/**
	 * Method creates a factory instance. The instance returned is singleton.
	 * 
	 * @return instance of type {@link ODataJPAFactory}.
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
				// TODO
			}

			return factoryImpl;
		}
	}

	/**
	 * Method returns a JPQL Builder Factory.
	 * 
	 * @return instance of type {@link JPQLBuilderFactory}
	 */
	public JPQLBuilderFactory getJPQLBuilderFactory() {
		return null;
	};

	public JPAAccessFactory getJPAAccessFactory() {
		return null;
	};

	public ODataJPAAccessFactory getODataJPAAccessFactory() {
		return null;
	};

}

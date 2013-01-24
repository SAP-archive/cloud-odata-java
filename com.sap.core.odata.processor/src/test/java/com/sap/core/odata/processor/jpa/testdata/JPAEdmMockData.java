package com.sap.core.odata.processor.jpa.testdata;

public interface JPAEdmMockData {
	/*
	 * Edm Complex Type Mock Data
	 */
	public interface ComplexType{
		
		public interface ComplexTypeA{
			public static final String name = "ComplexTypeA";
			public static final Class<ComplexTypeA> clazz = ComplexTypeA.class;
			public interface Property{
				public static final String PROPERTY_A = "A";
				public static final String PROPERTY_B = "B";
				public static final String PROPERTY_C = "C";
			}
			
		}
		
		public interface ComplexTypeB{
			public static final String name = "ComplexTypeB";
			public interface Property{
				public static final String PROPERTY_D = "D";
				public static final String PROPERTY_E = "E";
			}
			
		}
	}
}

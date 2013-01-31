package com.sap.core.odata.processor.jpa.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {

	/**
	 * @param args
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		
		
		
		String name = "x.y.z";
		String[] nameParts = name.split("\\.");
		Test t = new Test();
		A a = t.new A();
		A b = t.new A( );
		b = a;
		b.b.name = a.b.name;
		
		b.b.name = "X";
		
		System.out.println(a.b.name);

//		for (int i = 0; i < nameParts.length; i++) {
//			Method m = a.getClass().getMethod(nameParts[i], (Class<?>[]) null);
//			a = (Object) m.invoke(a);
//		}

		

	}

	public class A {
		public B b = new B( );
		public B x() {
			return new B();
		}

		public class B {
			String name = "Anirban";
			public C y() {
				System.out.println("Hello B");
				return new C();
			}
			
			public class C{
				public void z(){
					System.out.println("Hello World");
				}
			}
		}
	}

}

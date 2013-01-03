package com.sap.core.odata.processor.jpa.util;

public class MockJPA {
	
	
	
	/*public static EntityManagerFactory mockEntityManagerFactory()
	{
		EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.expect(emf.getMetamodel()).andReturn(mockMetaModel());
		return emf;
		
	}
	public static Metamodel mockMetaModel()
	{
		Metamodel metaModel = EasyMock.createMock(Metamodel.class);
		EasyMock.expect(metaModel.getEntities()).andReturn(mockEntities());
		return metaModel;
	}
	public static Set<EntityType<?>> mockEntities()
	{
		Set<EntityType<?>> entities = new HashSet<EntityType<?>>();
		EntityType<?> entity = EasyMock.createMock(EntityType.class);
		EasyMock.expect(entity.getName()).andReturn(MockData.ENTITY_NAME_1);
		//Class attributeClass = SalesOrderHeader.class;
		
		//Set<Attribute<ManagedType<SalesOrderHeader>,java.lang.Long>> attributes = new HashSet<Attribute<ManagedType<SalesOrderHeader>,java.lang.Long>>();
		Set<Attribute<? super ManagedType<?>, ?>> attr = mockAttributeSet();
//		EasyMock.expect(entity.getAttributes()).andReturn((Set<?>)attr);
		
		//	EasyMock.expect(entity.getId(java.lang.Long.class)).andReturn(mockSingularAttribute(MockData.ENTITY_ID_NAME,MockData.ENTITY_ID_TYPE));
		return entities;
	}
	private static LinkedHashSet<Attribute<? super ManagedType<?>, ?>> mockAttributeSet() {
		//Set<Attribute<Type<?>,java.lang.Long>> attributes = new HashSet<Attribute<Type<Integer>,java.lang.Long>>();
		//return attributes;
		return null;
	}
	public static SingularAttribute<?, ?> mockSingularAttribute(String name,Class<?> javaType)
	{
		SingularAttribute<?, ?> attribute = EasyMock.createMock(SingularAttribute.class);
		EasyMock.expect(attribute.getName()).andReturn(name);
	//	EasyMock.expect(attribute.getJavaType()).andReturn((Class<?>) javaType);
		return attribute;
	}
*/
}

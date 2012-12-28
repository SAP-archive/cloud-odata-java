package com.sap.core.odata.processor.jpa.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import org.easymock.EasyMock;

import com.sap.core.odata.processor.jpa.util.MockData;

public class Mock {
	
	
	
	public static EntityManagerFactory mockEntityManagerFactory()
	{
		EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.expect(emf.getMetamodel()).andReturn(mockMetaModel());
		return emf;
		
	}
	public static Metamodel mockMetaModel()
	{
		Metamodel metaModel = EasyMock.createMock(Metamodel.class);
		EasyMock.expect(metaModel.getEntities());
		return metaModel;
	}
	public static Set<EntityType<?>> mockEntities()
	{
		Set<EntityType<?>> entities = new HashSet<EntityType<?>>();
		EntityType<?> entity = EasyMock.createMock(EntityType.class);
		EasyMock.expect(entity.getName()).andReturn(MockData.ENTITY_NAME);
		EasyMock.expect(entity.getAttributes()).andReturn(mockAttributeSet());
		EasyMock.expect(entity.getId(java.lang.Long.class)).andReturn(mockSingularAttribute(MockData.ENTITY_ID_NAME,MockData.ENTITY_ID_TYPE));
	}
	private static Set<Attribute<?, ?>> mockAttributeSet() {
		// TODO Auto-generated method stub
		return null;
	}
	public static SingularAttribute<?, ?> mockSingularAttribute(String name,Class<?> javaType)
	{
		SingularAttribute<?, ?> attribute = EasyMock.createMock(SingularAttribute.class);
		EasyMock.expect(attribute.getName()).andReturn(name);
		EasyMock.expect(attribute.getJavaType()).andReturn((Class<?>) javaType);
	}

}

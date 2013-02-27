package com.sap.core.odata.processor.core.jpa.access.model;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import com.sap.core.odata.processor.core.jpa.mock.ODataJPAContextMock;

public class JPAEdmMappingModelServiceTest extends JPAEdmMappingModelService {

	public JPAEdmMappingModelServiceTest() {
		super(ODataJPAContextMock.mockODataJPAContext());
	}

	// @Test
	// public void testLoadMappingModel() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testIsMappingModelExists() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetJPAEdmMappingModel() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPAPersistenceUnit() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPAEntityType() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPAEntitySet() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPAAttribute() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPARelationship() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPAEmbeddableType() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testMapJPAEmbeddableTypeAttribute() {
	// fail("Not yet implemented");
	// }

	@Test
	public void testLoadMappingModelFile() {
		loadMappingModel();
		System.out.print(this.getJPAEdmMappingModel().getPersistenceUnit()
				.getEDMSchemaNamespace());

	}

	@Override
	protected InputStream loadMappingModelInputStream() {
		return null;
	}
}

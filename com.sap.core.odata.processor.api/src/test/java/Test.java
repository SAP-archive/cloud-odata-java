import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import com.sap.core.odata.processor.api.jpa.model.mapping.JPAEdmMappingModel;

public class Test {

	/**
	 * @param args
	 * @throws SAXException
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws SAXException, JAXBException,
			FileNotFoundException {

		String name = "C:\\Users\\I042713\\git\\com.sap.core.odata.processor\\com.sap.core.odata.processor.api\\src\\main\\resources\\SalesOrderProcessingMappingModel.xml";
		// String sname =
		// "C:\\Users\\I042713\\git\\com.sap.core.odata.processor\\com.sap.core.odata.processor.api\\src\\test\\java\\JPAEDMMappingModel.xsd";
		// SchemaFactory sf = SchemaFactory
		// .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		//
		// Schema s = sf.newSchema(new File(sname));

		Unmarshaller um = JAXBContext.newInstance(JPAEdmMappingModel.class)
				.createUnmarshaller();
		// um.setSchema(s);
		FileInputStream fs = new FileInputStream(name);
		JPAEdmMappingModel model = (JPAEdmMappingModel) um.unmarshal(fs);
		System.out.println(model.getPersistenceUnit().getEDMSchemaNamespace());
		System.out.println(model.getPersistenceUnit().getJPAEmbeddableTypes()
				.getJPAEmbeddableType().get(0).getEDMComplexType());
	}

}

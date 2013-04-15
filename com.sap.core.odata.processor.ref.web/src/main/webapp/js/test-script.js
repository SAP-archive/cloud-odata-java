module("Sales Order");

var request = {
	resourcePath : ""
};
odataTest("read service document", 2, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(data.workspaces[0].collections.length, 5, "5 entity sets found");
});

var request = {
		resourcePath : "$metadata"
};

odataTest("read metadata document", 9, request, function(response, data) {
		equal(response.statusCode, 200, "StatusCode: 200");
		expectedHeaders(response.headers, { "Content-Length" : "7470" }, "Check if there is any change in metadata");
		strictEqual((new RegExp('<FunctionImport Name="getAddress" ReturnType="SalesOrderProcessing.AddressInfo">')).test(response.body), true,"Check function import 'getAddress'");
		strictEqual((new RegExp('<FunctionImport Name="FindAllSalesOrders" ReturnType="Collection\\(SalesOrderProcessing.SalesOrder\\)" EntitySet="SalesOrders">')).test(response.body), true,"Check function import 'FindAllSalesOrders'");
		strictEqual((new RegExp('<Parameter Name="DeliveryStatusCode" Type="Edm.String" Mode="IN" Nullable="false" MaxLength="2"\\/>')).test(response.body), true,"Check the param in function import 'FindAllSalesOrders'");
		strictEqual((new RegExp('<FunctionImport Name="CheckATP" ReturnType="Edm.Boolean">')).test(response.body), true,"Check the function import 'CheckATP'");
		strictEqual((new RegExp('<Parameter Name="SoID" Type="Edm.Int64" Mode="IN" Nullable="false"\\/>')).test(response.body), true,"Check the param in function import 'CheckATP'");
		strictEqual((new RegExp('<Parameter Name="LiId" Type="Edm.Int64" Mode="IN" Nullable="false"\\/>')).test(response.body), true,"Check the param in function import 'CheckATP'");
		strictEqual((new RegExp('<FunctionImport Name="calculateNetAmount" ReturnType="SalesOrderProcessing.SalesOrder" EntitySet="SalesOrders">')).test(response.body), true,"Check the function import 'calculateNetAmount'");		
});

//---------------------------------------------------------------
module("Sales Order - Query");
//---------------------------------------------------------------

var request = {
	resourcePath : "SalesOrders?&$format=xml"
};
odataTest("GET SalesOrders", 3, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	expectedHeaders(response.headers, { "Content-Type" : "application/xml" },
		"content-type application/xml");
	equal(data.results.length, 11, "result length = 11");
});

var request = {
	resourcePath : "SalesOrders?$filter=DeliveryStatus eq '01'&$inlinecount=allpages"	
};

odataTest("$filter with $inlinecount", 4, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(response.data.__count, 6, "Response to $inlinecount: total count=6");
	equal(response.data.results[0].BuyerId, 2, "Checking the data for first record: BuyerID");
	equal(response.data.results[0].BuyerAddressInfo.City, "Test_City_2", "Checking the complex type for first record: City");
});

var request = {
		resourcePath : "SalesOrders?&$orderby=BuyerId%20desc"	
	};

odataTest("$orderby check", 7, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(response.data.results.length, 11, "Total number of records: 11");
	equal(response.data.results[0].BuyerId, 11, "Checking buyer ID of first record: 11");
	equal(response.data.results[1].BuyerId, 10, "Checking buyer ID of first record: 10");
	equal(response.data.results[2].BuyerId, 9, "Checking buyer ID of first record: 9");
	equal(response.data.results[3].BuyerId, 8, "Checking buyer ID of first record: 8");
	equal(response.data.results[4].BuyerId, 7, "Checking buyer ID of first record: 7");
});

var request = {
		resourcePath : "SalesOrders?&$orderby=BuyerId%20desc&$top=3"	
	};
odataTest("$top check along with $orderby", 5, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(response.data.results.length, 3, "Total number of records: 3");
	equal(response.data.results[0].BuyerId, 11, "Checking buyer ID of first record: 11");
	equal(response.data.results[1].BuyerId, 10, "Checking buyer ID of first record: 10");
	equal(response.data.results[2].BuyerId, 9, "Checking buyer ID of first record: 9");
});

var request = {
		resourcePath : "SalesOrders?&$orderby=BuyerId%20desc&$top=3&$skip=3"	
	};
odataTest("$skip test", 5, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(response.data.results.length, 3, "Total number of records: 3");
	equal(response.data.results[0].BuyerId, 8, "Checking buyer ID of first record: 8");
	equal(response.data.results[1].BuyerId, 7, "Checking buyer ID of first record: 7");
	equal(response.data.results[2].BuyerId, 6, "Checking buyer ID of first record: 6");
});

var request = {
		resourcePath : "SalesOrders?&$orderby=BuyerId%20desc&$top=3&$skip=13&$filter=DeliveryStatus eq '01'"	
	};
odataTest("$skip test:Skip more entries than existing", 2, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(response.data.results.length, 0, "Total number of records: 0");
});

var request = {
		resourcePath : "SalesOrders?&$orderby=BuyerId%20desc&$top=3&$skip=3&$filter=DeliveryStatus%20eq%20'01'"	
	};
odataTest("Combo of $top,$skip,$orderby and $filter", 5, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	equal(response.data.results.length, 3, "Total number of records: 3");
	equal(response.data.results[0].BuyerId, 4, "Checking buyer ID of first record: 4");
	equal(response.data.results[1].BuyerId, 2, "Checking buyer ID of first record: 2");
	equal(response.data.results[2].BuyerId, 1, "Checking buyer ID of first record: 1");
});

var request = {
		resourcePath : "SalesOrders?&$orderby=BuyerId%20desc&$top=3&$skip=3&$filter=ABCDEF%20eq%20'01'"	
	};

odataTest("Wrong parameter given in $filter", 2, request, function(response, data) {
	equal(response.statusCode, 400, "StatusCode: 400 Bad Request");
	equal((new RegExp("Invalid filter expression: 'ABCDEF eq '01'")).test(data.error.message.value), true, "Validating the error message");
});
//---------------------------------------------------------------
module("Sales Order - Read");
//---------------------------------------------------------------
var request = {
		resourcePath : "SalesOrders(2L)",
		headers : {
			Accept : "application/xml"
		}
	};
	odataTest("READ SalesOrder with ID 2L", 4, request, function(response, data) {
		equal(response.statusCode, 200, "StatusCode: 200");
		expectedHeaders(response.headers, { "Content-Type" : "application/xml" },
			"content-type application/xml");
		equal(data.CurrencyCode, "Code_2", "Curency code value validation");
		equal(data.BuyerAddressInfo.Street, "Test_Street_Name_2", "Complex Type value validation");
	});
	

	var request = {
		resourcePath : "SalesOrders(1000L)",
		headers : {
			Accept : "application/atom+xml"
		}
	};
	odataTest("Business Exception", 2, request, function(response, data) {
		equal(response.statusCode, 404, "StatusCode: 500-Server Error");
		expectedHeaders(response.headers, { "Content-Type" : "application/xml" },
			"content-type application/xml");
	});

//----------------------------------------------------------------	
module("$count cases");
//----------------------------------------------------------------

var request = {
		resourcePath : "SalesOrders/$count",
		headers : {
			Content  : "application/atom+xml",
			Accept : "application/atom+xml"
		}
};
odataTest("$count test: parent entity", 3, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code for the request: 200 OK");
	equal(response.data, 11, "Respose of $count for SalesOrders: 11 entries");
	expectedHeaders(response.headers, {"Content-Type" : "text/plain;charset=utf-8"},
			"Content type is plain text");
});
//Test $count for child entity
var request = {
		resourcePath : "SalesOrders(2L)/SalesOrderLineItemDetails/$count",
		headers : {
			Content  : "application/atom+xml",
			Accept : "application/atom+xml"
		}
};

odataTest("$count test: child entity", 1, request, function(response, data) {
	equal(response.data, 1, "number of child nodes: 2");	
});

//Negative case: the root entity does not exist
var request = {
		resourcePath : "SalesOrders(1000L)/SalesOrderLineItemDetails/$count",
		headers : {
			Content  : "application/atom+xml",
			Accept : "application/atom+xml"
		}
};

odataTest("$count test: child entity doesn't exist", 2, request, function(response, data) {
	equal(response.statusCode, 200, "You don't get error response if the resource doesn't exist");
	equal(response.data, 0, "Zero count is returned if the entity doesn't exist");
});

//----------------------------------------------------------------	
module("$select cases");
//----------------------------------------------------------------
var request = {
		resourcePath : "SalesOrders?$select=ID",
};

odataTest("$select test: display only ID for SO", 3, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal(data.results.length, 11, "Total number of results returned: 11");
	equal(data.results[0].ID, 2, "Value of lone field ID: 2");
});

var request = {
		resourcePath : "SalesOrders?$select=BuyerAddressInfo",
};

odataTest("$select test: complex type", 6, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal(data.results.length, 11, "Total number of results returned: 11");
	equal(data.results[0].BuyerAddressInfo.Street, "Test_Street_Name_2", "Field 1 val for complex type");
	equal(data.results[0].BuyerAddressInfo.Number, 2, "Field 2 val for complex type");
	equal(data.results[0].BuyerAddressInfo.Country, "Test_Country_2", "Field 3 val for complex type");
	equal(data.results[0].BuyerAddressInfo.City, "Test_City_2", "Field 4 val for complex type");
});

var request = {
		resourcePath : "SalesOrders(2L)/SalesOrderLineItemDetails?$select=MatId",
};

odataTest("$select test: Child item with navigation", 3, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal(data.results.length, 1, "Total number of results returned: 1");
	equal(data.results[0].MatId, 112, "Value of lone field MatId: 112");
});

var request = {
		resourcePath : "SalesOrders?$skip=3&$top=3&$filter=DeliveryStatus%20eq%20%20'01'&$inlinecount=allpages&$select=BuyerId,BuyerName",
};

odataTest("combination of $filter,top,skip,select,inlinecount", 4, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal(data.results.length, 3, "Total number of results returned: 3");
	equal(data.results[0].BuyerId, 8, "Field verification: Buyer ID=112");
	equal(data.results[0].BuyerName, "buyerName_8", "Field verification: Buyer Name: buyerName_8");
});

//----------------------------------------------------------------	
module("Function Import");
//----------------------------------------------------------------
var request = {
		resourcePath : "getAddress?&SoID=2"
};
odataTest("Function Import for GetAddress", 5, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal((new RegExp("Test_Street_Name_2")).test(response.body), true, "Check Street Name");
	equal((new RegExp("2")).test(response.body), true, "Check number");
	equal((new RegExp("Test_Country_2")).test(response.body), true, "Check country");
	equal((new RegExp("Test_City_2")).test(response.body), true, "Check city");
});

var request = {
		resourcePath : "FindAllSalesOrders?&DeliveryStatusCode='01'"
};
odataTest("Function Import for Finding All Sales Order for a Delivery Status", 5, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal(data.results.length, 6, "Total number of entries with status 01: 6");
	equal(data.results[0].BuyerId, 2, "Check Buyer Id: 2");
	equal(data.results[0].BuyerAddressInfo.City, "Test_City_2", "Check Complex Type Value");
	equal(data.results[0].DeliveryStatus, "01", "Delivery Status: 01");
});

var request = {
		resourcePath : "CheckATP?&SoID=2&LiId=10"
};
odataTest("Function Import CheckATP", 2, request, function(response, data) {
	equal(response.statusCode, 200, "HTTP Status Code: 200 OK");
	equal((new RegExp("false").test(response.body)), true, "Check if the response is false");
});

var request = {
		resourcePath : "CheckATP?&SoID=2"
};
odataTest("Not Passing All the Parameters in a Function Import", 2, request, function(response, data) {
	equal(response.statusCode, 400, "HTTP Status Code: 400 Bad Request");
	equal((new RegExp("Missing parameter.").test(response.body)), true, "Missing Parameter Error");
});

var request = {
		resourcePath : "CheckATP?&SoID='abcd'&LiId=10"
};
odataTest("Function Import: Passing wrong parameter type", 2, request, function(response, data) {
	equal(response.statusCode, 400, "HTTP Status Code: 400 Bad Request");
	equal((new RegExp("Format of ''abcd'' is not compatible with 'Edm.Int64'.").test(response.body)), true, "Wrong format error");
});
////-----------------------------------------------------------------------
//module("CUD Scenarios")
////-----------------------------------------------------------------------
//var request_payload = "<?xml version='1.0' encoding='UTF-8'?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/\"><id>http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/SalesOrders(1L)</id><title type=\"text\">SalesOrders</title><updated>2013-03-20T18:36:32.392+05:30</updated><category term=\"SalesOrderProcessing.SalesOrder\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/><link href=\"SalesOrders(1L)\" rel=\"edit\" title=\"SalesOrder\"/><link href=\"SalesOrders(1L)/SalesOrderLineItemDetails\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/SalesOrderLineItemDetails\" type=\"application/atom+xml; type=feed\" title=\"SalesOrderLineItemDetails\"/><link href=\"SalesOrders(1L)/NotesDetails\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/NotesDetails\" type=\"application/atom+xml; type=feed\" title=\"NotesDetails\"/><content type=\"application/xml\"><m:properties><d:ID>1000</d:ID><d:CreationDate /><d:CurrencyCode>Code_1000</d:CurrencyCode><d:BuyerAddressInfo m:type=\"SalesOrderProcessing.AddressInfo\"><d:Street>Test_Street_Name_1000</d:Street><d:Number>1000</d:Number><d:Country>Test_Country_1000</d:Country><d:City>Test_City_1000</d:City></d:BuyerAddressInfo><d:GrossAmount>0.0</d:GrossAmount><d:BuyerId>10001</d:BuyerId><d:DeliveryStatus>false</d:DeliveryStatus><d:BuyerName>buyerName_1000</d:BuyerName><d:NetAmount>0.0</d:NetAmount></m:properties></content></entry>";
//var request = {
//	method : "POST",
//	resourcePath : "SalesOrders",
//    data : request_payload ,
//	headers : {
//		Content  : "application/atom+xml",
//		Accept : "application/atom+xml"
//	}
//};
//
//odataTest("$count test: child entity", 2, request, function(response, data) {
//	equal(response.statusCode, 200, "Creation Successful!");
//	equal(response.data, 0, "Zero count is returned if the entity doesn't exist");
//});
////----------------------------------
////Update
////----------------------------------
//
//var request = {
//		method : "PUT",
//		resourcePath : "SalesOrders",
//	    data : request_payload ,
//		headers : {
//			Content  : "application/atom+xml",
//			Accept : "application/atom+xml"
//		}
//	};


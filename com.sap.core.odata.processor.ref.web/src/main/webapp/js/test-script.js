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

odataTest("read metadata document", 3, request, function(response, data) {
		equal(response.statusCode, 200, "StatusCode: 200");
		expectedHeaders(response.headers, { "Content-Length" : "6520" }, "Check if there is any change in metadata");
		strictEqual((new RegExp('Complex')).test(response.body), true,"check for some substring in metadata");
});

//---------------------------------------------------------------
module("Sales Order - GET");
//---------------------------------------------------------------

var request = {
	resourcePath : "SalesOrders?&$format=xml"
};
odataTest("GET salesorderheader qp xml", 3, request, function(response, data) {
	equal(response.statusCode, 200, "StatusCode: 200");
	expectedHeaders(response.headers, { "Content-Type" : "application/xml" },
		"content-type application/xml");
	equal(data.results.length, 11, "result length = 11");
});

var request = {
		resourcePath : "SalesOrders(1L)",
		headers : {
			Accept : "application/xml"
		}
	};
	odataTest("GET employee entry hp atom", 4, request, function(response, data) {
		equal(response.statusCode, 200, "StatusCode: 200");
		expectedHeaders(response.headers, { "Content-Type" : "application/xml" },
			"content-type application/xml");
		equal(data.CurrencyCode, "Code_1", "Curency code value validation");
		equal(data.BuyerAddressInfo.Street, "Test_Street_Name_1", "Complex Type value validation");
	});
	
//---------------------------------------------------------------
module("Error Cases - BEP");
//---------------------------------------------------------------

	var request = {
		resourcePath : "SalesOrders(1000L)",
		headers : {
			Accept : "application/atom+xml"
		}
	};
	odataTest("Business Exception", 2, request, function(response, data) {
		equal(response.statusCode, 500, "StatusCode: 500-Server Error");
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
		resourcePath : "SalesOrders(1L)/SalesOrderLineItemDetails/$count",
		headers : {
			Content  : "application/atom+xml",
			Accept : "application/atom+xml"
		}
};

odataTest("$count test: child entity", 1, request, function(response, data) {
	equal(response.data, 2, "number of child nodes: 2");	
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

//-----------------------------------------------------------------------
module("CUD Scenarios")
//-----------------------------------------------------------------------
var request_payload = "<?xml version='1.0' encoding='UTF-8'?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/\"><id>http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/SalesOrders(1L)</id><title type=\"text\">SalesOrders</title><updated>2013-03-20T18:36:32.392+05:30</updated><category term=\"SalesOrderProcessing.SalesOrder\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/><link href=\"SalesOrders(1L)\" rel=\"edit\" title=\"SalesOrder\"/><link href=\"SalesOrders(1L)/SalesOrderLineItemDetails\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/SalesOrderLineItemDetails\" type=\"application/atom+xml; type=feed\" title=\"SalesOrderLineItemDetails\"/><link href=\"SalesOrders(1L)/NotesDetails\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/NotesDetails\" type=\"application/atom+xml; type=feed\" title=\"NotesDetails\"/><content type=\"application/xml\"><m:properties><d:ID>1000</d:ID><d:CreationDate /><d:CurrencyCode>Code_1000</d:CurrencyCode><d:BuyerAddressInfo m:type=\"SalesOrderProcessing.AddressInfo\"><d:Street>Test_Street_Name_1000</d:Street><d:Number>1000</d:Number><d:Country>Test_Country_1000</d:Country><d:City>Test_City_1000</d:City></d:BuyerAddressInfo><d:GrossAmount>0.0</d:GrossAmount><d:BuyerId>10001</d:BuyerId><d:DeliveryStatus>false</d:DeliveryStatus><d:BuyerName>buyerName_1000</d:BuyerName><d:NetAmount>0.0</d:NetAmount></m:properties></content></entry>";
var request = {
	method : "POST",
	resourcePath : "SalesOrders",
    data : request_payload ,
	headers : {
		Content  : "application/atom+xml",
		Accept : "application/atom+xml"
	}
};

odataTest("$count test: child entity", 2, request, function(response, data) {
	equal(response.statusCode, 200, "Creation Successful!");
	equal(response.data, 0, "Zero count is returned if the entity doesn't exist");
});
//----------------------------------
//Update
//----------------------------------

var request = {
		method : "PUT",
		resourcePath : "SalesOrders",
	    data : request_payload ,
		headers : {
			Content  : "application/atom+xml",
			Accept : "application/atom+xml"
		}
	};


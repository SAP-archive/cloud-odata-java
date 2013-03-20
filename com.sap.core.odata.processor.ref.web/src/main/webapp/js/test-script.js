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
odataTest("read metadata document", 1, request, function(response, data) {
		equal(response.statusCode, 200, "StatusCode: 200");
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
//	ok(data.__next, "__next link exists");
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

odataTest("$count test: child entity", 2, request, function(response, data) {
	equal(response.statusCode, 200, "You don't get error response if the resource doesn't exist");
	equal(response.data, 0, "Zero count is returned if the entity doesn't exist");
});
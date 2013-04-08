module("Service",{ foo : "bar" } );   

/* Test 1.1 */		
var request = {
  resourcePath: ".", 
  headers: {DataServiceVersion: "999.0"}
};
odataTest("Read with invalid DataServiceVersion", 1, request, function (response, data) {
  equal(response.statusCode, 400, "StatusCode: 400");
});

module("Employee");    

/* Test 2.1 */
request = "Employees('1')";
odataTest("Read Entity '1'", 4, request , function (response, data) {
  equal(response.statusCode, 200, "StatusCode: 200");
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.EmployeeName, 'Walter Winter', "EmployeeName");
  deepEqual(data.EntryDate, new Date("1 Jan 1999 GMT"), "EntryDate: 1999-01-01");
});

request = "Buildings('1')";
odataTest("Read Building '1'", 3, request , function (response, data) {
  equal(response.statusCode, 200, "StatusCode: 200");
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  ok(/odata=verbose/.test(response.headers["Content-Type"]), "JSON");
});

/* Test 2.2 */
request = {
  method: 'GET',
  resourcePath: "Employees('2')/EntryDate",
  recognizeDates: true     
};      
odataTest("Read Property of '2'", 2, request, function (response, data) {
  equal(response.headers["DataServiceVersion"], "1.0", "DataServiceVersion: 1.0");
  deepEqual(data, { EntryDate : new Date("01 Jul 2003 GMT") }, "EntryDate: 2003-07-01");
});   

/* Test 2.3 */
request = {
  method: 'PUT',
  resourcePath: "Employees('2')/EntryDate",
  data: {EntryDate: new Date("28 Apr 2012 GMT")}
};    
odataTest("Update Property of '2'", 2, request, function (response, data) {
  equal(response.statusCode, 204, "StatusCode: 204");  
  equal(response.statusText, "No Content", "StatusText: No Content");  
}); 

module("Employees");    

/* Test 3.1 */
request = "Employees?$inlinecount=allpages";
odataTest("Read Entities", 4, request , function (response, data) {
  equal(response.statusCode, 200, "StatusCode: 200");
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.results.length, 6, "Number of entries");
  equal(data.__count, 6, "Inlinecount");
});

module("batch");    

/* Test 4.1 */
request = {
  method: 'POST',
  resourcePath: "$batch",
  data: { __batchRequests: [
    { requestUri: "Employees('1')/EmployeeName", method: "GET" },
    { requestUri: "http://localhost/sap/bc/odata/Employees('2')/EmployeeName", method: "GET" }
  ]}  
};  

odataTest("batch", 4, request , function (response, data) {
  equal(response.statusCode, 202, "StatusCode: 202");
  equal(data.__batchResponses.length, 2, "Number of Responses: 2");
  equal(data.__batchResponses[0].data.EmployeeName, "Walter Winter", "First EmployeeName: Walter Winter");
  equal(data.__batchResponses[1].data.EmployeeName, "Frederic Fall", "Second EmployeeName: Frederic Fall");
});

/* Test 4.1 */
request2 = {
  method: 'POST',
  resourcePath: "$batch",
  data: { __batchRequests:  [ { requestUri: "http://localhost/sap/bc/odata/Employees('2')/EmployeeName", method: "GET" } ,
                              {__changeRequests: [ 
                                    { requestUri: "Employees('2')/EmployeeName", method: "PUT", data: {EmployeeName: "Frederic Fall MODIFIED"}, headers: { "Content-Length" : "100000"} }        
                                                 ] 
                              },
                              { requestUri: "http://localhost/sap/bc/odata/Employees('2')/EmployeeName", method: "GET" } ,
                            ] 
        } 
};  

odataTest("batch with post", 5, request2 , function (response, data) {
  equal(response.statusCode, 202, "StatusCode: 202");
  equal(data.__batchResponses.length, 3, "Number of Responses: 3");
  equal(data.__batchResponses[0].data.EmployeeName, "Frederic Fall", "Second EmployeeName: Frederic Fall");
  equal(data.__batchResponses[1].__changeResponses.length,1,"Expected 1 __changeResponse");
  //SK why is data.__batchResponses[1].__changeResponses[0].message = "no handler for data" ?? and 
  //       data.__batchResponses[1].__changeResponses[0].statusCode is not defined
  equal(data.__batchResponses[2].data.EmployeeName, "Frederic Fall MODIFIED", "Second EmployeeName: Frederic Fall MODIFIED");
});
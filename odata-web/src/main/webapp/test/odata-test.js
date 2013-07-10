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
module("GET");

/* Test 3.1 ComplexProperty*/
request = {
  method: 'GET',
  resourcePath: "/Employees('2')/Location/City/CityName",
  recognizeDates: true,
  headers: {Accept: "application/json", MaxDataServiceVersion:"2.0"},
};      
odataTest("ComplexProperty of Employees('2')", 2, request, function (response, data) {
  equal(response.headers["DataServiceVersion"], "1.0", "DataServiceVersion: 1.0");
  equal(data.CityName,"Walldorf","Country:"+ data.CityName);
});

  /* Test 3.2 NavigationProperty*/
request = {
  method: 'GET',
  resourcePath: "/Employees('1')/ne_Team/nt_Employees",
  recognizeDates: true     
};      
odataTest("NavigationProperty of Employees('1')", 3, request, function (response, data) {
  var team=data.results;
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.results.length,3,"Count of teammember: "+data.results.length);
  ok(team[0].TeamId==team[1].TeamId==team[2].TeamId, "Team:"+team[0].EmployeeName+", "+team[1].EmployeeName+", "+team[2].EmployeeName);

});

/* Test 3.3 NavigationProperty*/
request = {
  method: 'GET',
  resourcePath: "/Employees('1')/ne_Team/nt_Employees/$count",
  recognizeDates: true     
};      
odataTest("Navigate to teammember of Employees('1')", 2, request, function (response, data) {
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data,3,"Count of teammember: "+data);
});

/* Test 3.4 FunctionImport*/
request = {
  method: 'GET',
  resourcePath: "/EmployeeSearch?q='Walter Winter','1'",
  recognizeDates: true     
};      
odataTest("FunctionImport EmployeeSearch with q='Walter Winter', id=1", 2, request, function (response, data) {
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.results.length,0);
});

/* Test 3.5 FunctionImport*/
request = {
  method: 'GET',
  resourcePath: "EmployeeSearch?q='Jo'&$orderby=Age asc&$select=EmployeeId",
  recognizeDates: true     
};      
odataTest("FunctionImport with system query options", 2, request, function (response, data) {
  if(data.results.length>1){
  var employee1 = data.results[0];
  var employee2 = data.results[1];
    equal(employee1.EmployeeId, 5, "John Field");
    equal(employee2.EmployeeId, 3, "Jonathan Smith");
  }
});

/* Test 3.6 FunctionImport*/
request = {
  method: 'GET',
  resourcePath: "/AllUsedRoomIds/$count",
  recognizeDates: true     
};      
odataTest("FunctionImport with count", 1, request, function (response, data) {
 equal(response.statusCode, 400, "StatusCode: 400");
});

/* Test 3.7 Count*/
request = {
  method: 'GET',
  resourcePath: "Employees/$count?$filter=startswith(EmployeeName,'Wal')",
  recognizeDates: true     
};      
odataTest("$count", 2, request, function (response, data) {
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data, 1, "Count: 1");
 
}); 
module("System Query Options");  

/* Test 4.1 */
request = "Employees?$inlinecount=allpages";
odataTest("Read Entities", 4, request , function (response, data) {
  equal(response.statusCode, 200, "StatusCode: 200");
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.results.length, 6, "Number of entries");
  equal(data.__count, 6, "Inlinecount");
});

/* Test 4.2 Inlinecount and filter*/
request = "Employees?$inlinecount=allpages&$filter=EmployeeId gt '3'";
odataTest("Read Entities(inlinecount after $filter have been applied)", 4, request , function (response, data) {
  equal(response.statusCode, 200, "StatusCode: 200");
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.results.length, 3, "Number of entries: "+data.results.length);
  equal(data.__count, 3, "Inlinecount");
});

/* Test 4.3 Select */
request = {
  method: 'GET',
  resourcePath: "Employees/?$select=EmployeeId,Location", 
  recognizeDates: true     
};      
odataTest("Select", 4, request, function (response, data) {
  var employee1 = data.results[0];
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(employee1.EmployeeId, "1", "EmployeeId");
  equal(employee1.Location.City.CityName, "Heidelberg", "Location")
  equal(employee1.EmployeeName,undefined, "EmployeeName is undefined");
}); 

/* Test 4.4 Expand */
request = {
  method: 'GET',
  resourcePath: "Employees('2')/?$expand=ne_Manager", 
  recognizeDates: true     
};      
odataTest("Expand manager of Employees('2')", 2, request, function (response, data) {
  
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.ne_Manager.EmployeeName, 'Walter Winter', "ManagerName: Walter Winter");
}); 


/* Test 4.5 Expand and select */
request = {
  method: 'GET',
  resourcePath: "Employees('5')/?$expand=ne_Team/nt_Employees/ne_Room/nr_Building&$select=ne_Team/nt_Employees/EmployeeName,ne_Team/nt_Employees/ne_Room/nr_Building/Name",
  recognizeDates: true     
};      
odataTest("Complex expand with select", 3, request, function (response, data) {
  var team = data.ne_Team.nt_Employees.results;
  var building = team[0].ne_Room.nr_Building;
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(team.length, 2, "Count of teammember: 2");
  equal(building.Name, 'Building 2', team[0].EmployeeName+" is located in "+building.Name);
});

 /* Test 4.6 Filter and OrderBy*/
request = {
  method: 'GET',
  resourcePath: "Employees/?$filter=startswith(EmployeeName,'Jo') or (TeamId gt '2')&$orderby=Age",
  recognizeDates: true     
};      
odataTest("Filter and orderby", 3, request, function (response, data) {
  equal(response.headers["DataServiceVersion"], "2.0", "DataServiceVersion: 2.0");
  equal(data.results.length, 3, "Count : 3");
  ok(data.results[0].Age<data.results[1].Age, "correct order");
});


module("batch");    

/* Test 5.1 */
request = {
  method: 'POST',
  resourcePath: "$batch",
  data: { __batchRequests: [
    { requestUri: "Employees('1')/EmployeeName", method: "GET" },
    { requestUri: "Employees('2')/EmployeeName", method: "GET" }
  ]}  
};  

odataTest("batch", 4, request , function (response, data) {
  equal(response.statusCode, 202, "StatusCode: 202");
  equal(data.__batchResponses.length, 2, "Number of Responses: 2");
  equal(data.__batchResponses[0].data.EmployeeName, "Walter Winter", "First EmployeeName: Walter Winter");
  equal(data.__batchResponses[1].data.EmployeeName, "Frederic Fall", "Second EmployeeName: Frederic Fall");
});

/* Test 5.2 */

request2 = {
method: 'POST',
resourcePath: "$batch",
data: { __batchRequests:  [ { requestUri: "Employees('2')/EmployeeName", method: "GET" } ,
                            {__changeRequests: [ 
                                  { requestUri: "Employees('2')/EmployeeName", method: "PUT", data: {EmployeeName: "Frederic Fall MODIFIED"}, headers: { "Content-Length" : "100000"} }        
                                               ] 
                            },
                            { requestUri: "Employees('2')/EmployeeName", method: "GET" } ,
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

/* Test 5.3 */

request3 ={
       method: 'POST',
resourcePath: "$batch",
data: { __batchRequests:  [ { requestUri: "Employees('2')/EmployeeName", method: "GET" } ,
                            {__changeRequests: [ 
                              { requestUri: "Employees('2')/EmployeeName", method: "PUT", data: {EmployeeName: "Frederic Fall MODIFIED"}, headers: { "Content-Length" : "100000"} },
                                     
                                { requestUri: "Employes", method: "POST", headers: { "Content-Type" : "application/octet-stream"} },
                                            ] 
                            },
                          ] 
      } 
};  

odataTest("batch with error", 5, request3 , function (response, data) {
equal(response.statusCode, 202, "StatusCode: 202");
equal(data.__batchResponses.length, 2, "Number of Responses: 2");
equal(data.__batchResponses[0].data.EmployeeName, "Frederic Fall", "Second EmployeeName: Frederic Fall");
equal(data.__batchResponses[1].response.statusCode,404,"ChangeSet processing fails. Employes not found");
equal(data.__batchResponses[1].__changeResponses,undefined,"No change responses"); 
      });

module("POST");

/* Test 5.4 */

request4 ={
       method: 'POST',
resourcePath: "$batch",
       data: { __batchRequests:  [ { requestUri: "Employees('2')/EmployeeName", method: "GET",  headers: { "Content-Id":"AAA"}} ,
                            {__changeRequests: [ 
                              { requestUri: "Employees", method: "POST", headers: { "Content-Type" : "application/octet-stream", "Content-ID" : "employee"}},
                              { requestUri: "$employee/EmployeeName", method: "PUT", data: {EmployeeName: "Robert Fall"}, headers: { "Content-Length" : "100000", "Content-Id":"AAA"}}
                             ]},
                           { requestUri: "Employees('7')/EmployeeName", method: "GET" } 
                          ]
      }
};
odataTest("batch with content-ID", 9, request4 , function (response, data) {
equal(response.statusCode, 202, "StatusCode: 202");
equal(data.__batchResponses.length, 3, "Number of Responses: 3");
equal(data.__batchResponses[0].data.EmployeeName, "Frederic Fall", "Second EmployeeName: Frederic Fall");
equal(data.__batchResponses[0].headers["Content-Id"], "AAA", "Content-Id: AAA");
equal(data.__batchResponses[1].__changeResponses.length,2,"Expected 2 __changeResponse");
equal(data.__batchResponses[1].__changeResponses[0].statusCode,201,"New entry Employee created");
equal(data.__batchResponses[1].__changeResponses[0].headers["Content-Id"],"employee","Content-Id: employee");
equal(data.__batchResponses[1].__changeResponses[1].statusCode,204,"No change responses"); 
equal(data.__batchResponses[2].data.EmployeeName, "Robert Fall", "EmployeeName: Robert Fall");
});
/* Test 6.1 */
var entry={ 
   __metadata: {
     id: "Employees('1')",
     uri: "Employees('1')",
     type: "RefScenario.Employee",
     content_type: "image/jpeg",
     media_src: "Employees('1')/$value",
     edit_media: "Employees('1')/$value",
     properties: {
       ne_Manager: {
         associationuri: "Employees('1')/$links/ne_Manager"
       },
       ne_Team: {
         associationuri: "Employees('1')/$links/ne_Team"
       },
       ne_Room: {
         associationuri: "Employees('1')/$links/ne_Room"
       }
     }
   },
   EmployeeName: "Walter Winter",
   ManagerId: "1",
   RoomId: "104",
   TeamId: "4",
   Age: 52,
   ImageUrl: "Employees('1')/$value",
   ne_Manager: {
      __deferred: {
        uri: "Employees('1')/ne_Manager"
       }
   },
   ne_Team: {
     __metadata: {
       id: "Teams('1')",
       uri: "Teams('1')",
       type: "RefScenario.Team",
       properties: {
       	 nt_Employees: {
           associationuri: "Teams('1')/$links/nt_Employees"
         }
       }
     },
   Name: "Team X",
   isScrumTeam: false,
   nt_Employees: {
     __deferred: {
        uri: "Teams('1')/nt_Employees"
     }
   }
     
   },
   ne_Room: {
     __deferred: {
         uri: "Employees('1')/ne_Room"
     }
   }
};
request = {
  method: 'POST',
  resourcePath: "Rooms",
  data: {
 	 __metadata : { // additional data about the entry
       id: 'Rooms(\'1\')',
       uri: 'Rooms(\'1\')', // URI to the entry
   	   etag: 'W/"1"', // entity ETag value used for concurrency checks
   	   type: 'RefScenario.Room' ,
   	},
    Seats: 3,
    Name : 'Room 104',
    nr_Employees: [entry],  
  	},
};  

odataTest("DeepInsert", 3, request , function (response, data) {
  equal(response.statusCode, 201, "StatusCode: 201");
  equal(data.Seats, 3, "Seats : 3");
  equal(data.Name, "Room 104", "Room: 104");
  // var employees = data.nr_Employees.results;
  //equal(employees[0].EmployeeName, "Walter Winter", "Walter Winter");
  //equal(employees[0].EmployeeId, 7, "EmployeeId: 7");
  //var team = employees[0].ne_Team;
  //equal(team.Name, "Team X", "New team with name 'Team X'");
  //equal(team.isScrumTeam, false, "IsScrumTeam: false");
  //equal(team.Id, 4, "Team Id: 4");
});
(function( window ) {
  
  var QUnit = window.QUnit, 
      OData = window.OData,
      config = {},

      removeParameter = function( value ) {
        return value.replace(/;.*/,"").replace(/^\s+|\s+$/g,"");
      },
      
      getHeader = function( headers, name ) {
         for (var prop in headers ) {
            if ( prop.toLowerCase() == name.toLowerCase() )  {
                return removeParameter (headers[prop] );
            }
        }
        return false;
      },
      
      QUnitExt = {

        odataTest: function ( testName, expected, pathOrRequest, callback ) {

          if ( arguments.length === 3 ) {
            callback = pathOrRequest;
            pathOrRequest = expected;
            expected = null;
          }

          QUnit.asyncTest(testName, expected, function() { 

            var request,
                testEnvironment = this;
        
            if (pathOrRequest instanceof String || typeof pathOrRequest === "string") {
              request = { resourcePath: pathOrRequest };
            } else {
              request = pathOrRequest;
            }
            
            if ('resourcePath' in request) {
              request.requestUri = config.serviceRoot + request.resourcePath; 
            }  
            
            if ( config.csrfToken && (
                 request.method === 'PUT'   || 
                 request.method === 'POST'   || 
                 request.method === 'MERGE' || 
                 request.method === 'PATCH' || 
                 request.method === 'DELETE' ) ) {
              if (!request.headers) {
                request.headers = {};
              } 
              request.headers["X-CSRF-Token"] = config.csrfToken;          
            }
            
            OData.request(request, function (data, response) {
                
                try { 
                    callback.call( testEnvironment, response, data);
                } catch ( e ) {
                    QUnit.pushFailure( "Died on odata success callback: " + e.message );
                } 
                start();
              
            }, function(error) {
              
                var data = { 
                        error: { 
                          code:"", 
                          message: { 
                            lang: "en-US", 
                            value: "Unexpected error occured" 
                          } 
                        } 
                    };
              
                try {                  
                  if ( error.response ) {
                      var contentType = getHeader( error.response.headers, "content-type" );
                      if ( contentType == "application/json" ) {
                        data = JSON.parse( error.response.body );
                      } else if ( /application\/(.*\+)?xml/.test(contentType) ) {
                        /* TODO parse xml error response body */
                        data.error.message.value = error.response.body;
                      }
                      callback.call( testEnvironment, error.response, data );  
                    } 
                } catch( e ) { 
                    QUnit.pushFailure( "Died on odata error callback: " + e.message );
                }
                start();
          
            }, null, null, config.metadata);
            
          });
          
      },

      expectedHeaders: function( actHeaders, expHeaders, message) {
        
        var expected = {},
            actual = {};
        
        for (var prop in expHeaders ) {
            var name = prop.toLowerCase();
            expected[name] = removeParameter( expHeaders[prop] );
        }    

        for (var prop in actHeaders ) {
            var name = prop.toLowerCase();
            if ( name in expected ) {
                actual[name] = removeParameter( actHeaders[prop] );
            } 
        }
        
        QUnit.push(QUnit.equiv(actual, expected), actual, expected, message);
        
      }
    
  };
  
  var scriptTags = window.document.getElementsByTagName('script'),
      scriptTag = scriptTags[ scriptTags.length - 1 ];

  QUnit.begin(function () {
    
    var serviceRootParts = null, 
        serviceRoot = null;
        
    serviceRoot = scriptTag.getAttribute("data-service-root");
    if (serviceRoot) { 
        serviceRootParts = /^(?:([^:\/?#]+):)?(?:\/\/([^\/?#]*))?(\/[^?#]*)/.exec(serviceRoot);          
        if (serviceRootParts) {
            config.serviceRoot = serviceRootParts.shift();
        }
    } 
    
    if (!config.serviceRoot) {
        return;
    }
    
    config.serviceRoot = serviceRoot;
    
    if (!serviceRootParts[1]) {
      
      QUnit.config.autostart = false;
    
      var request = { 
        requestUri: config.serviceRoot + "$metadata", 
        headers: { 
          "X-CSRF-Token": "Fetch" 
        } 
      };
    
      OData.read(request, function(data, response){ 
        
        var token = getHeader( response.headers, "x-csrf-token" );
        if (token) {
            config.csrfToken = token;
        }
        config.metadata = data;
        
        QUnit.start();
        
      }, function(error){ 
        
        throw error; 
        
      }, OData.metadataHandler);       
        
    } 
    
  });

    
  QUnit.extend( window, QUnitExt);
  QUnit.extend( QUnit, QUnitExt);
  
}( this ) );
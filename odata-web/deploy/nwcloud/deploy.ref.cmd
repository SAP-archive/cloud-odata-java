set HTTPS_PROXY_HOST=proxy
set HTTPS_PROXY_PORT=8080

call C:\.neosdk\neo-sdk-1.17.0\tools\neo.bat deploy -u p1160370 -password Test1234 odata-web\deploy\nwcloud\deploy.web.ref.odata.properties
call C:\.neosdk\neo-sdk-1.17.0\tools\neo.bat stop -y -u p1160370 -password Test1234 odata-web\deploy\nwcloud\deploy.web.ref.odata.properties
call C:\.neosdk\neo-sdk-1.17.0\tools\neo.bat start -y -u p1160370 -password Test1234 odata-web\deploy\nwcloud\deploy.web.ref.odata.properties
                       
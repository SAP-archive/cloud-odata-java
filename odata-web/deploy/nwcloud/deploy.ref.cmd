set HTTPS_PROXY_HOST=proxy
set HTTPS_PROXY_PORT=8080

rem NEO_SDK_HOME defined on Jenkins server!

call %NEO_SDK_HOME%\tools\neo.bat deploy -u p1160370 -password Test1234 odata-web\deploy\nwcloud\deploy.web.ref.odata.properties
call %NEO_SDK_HOME%\tools\neo.bat stop -y -u p1160370 -password Test1234 odata-web\deploy\nwcloud\deploy.web.ref.odata.properties
call %NEO_SDK_HOME%\tools\neo.bat start -y -u p1160370 -password Test1234 odata-web\deploy\nwcloud\deploy.web.ref.odata.properties
                       
#!/bin/bash
#
# (c) 2013 by SAP AG
#

#
# Example for Linux/Mac shell script
#
########

export https_proxy=https://proxy:8080

# Path of neo sdk (NEO_SDK_HOME) must be defined on Jenkins server (use variable only for local test reasons)
#NEO_SDK_HOME="/opt/neo-sdk-1.17.0"
PROPERTIES_PATH="odata-web/deploy/nwcloud/deploy.web.ref.odata.properties"

if [ -z $NEO_SDK_HOME ]; then
	echo NEO_SDK_HOME is not set and must be defined on Jenkins server!
	exit 1
fi

echo Use neo sdk home path \"$NEO_SDK_HOME\"
echo Deploy with properties \"$PROPERTIES_PATH\"

$NEO_SDK_HOME/tools/neo.sh deploy -u p1160370 -password Test1234 $PROPERTIES_PATH
$NEO_SDK_HOME/tools/neo.sh stop -y -u p1160370 -password Test1234 $PROPERTIES_PATH
$NEO_SDK_HOME/tools/neo.sh start -y -u p1160370 -password Test1234 $PROPERTIES_PATH
                       

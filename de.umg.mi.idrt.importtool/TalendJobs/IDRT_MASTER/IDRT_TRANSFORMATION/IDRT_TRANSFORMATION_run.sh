#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Xms256M -Xmx1024M -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-1.2.16.jar:$ROOT_PATH/../lib/ojdbc6.jar:$ROOT_PATH/../lib/dom4j-1.6.1.jar:$ROOT_PATH/../lib/talendcsv.jar:$ROOT_PATH/../lib/tns.jar:$ROOT_PATH/../lib/talend_file_enhanced_20070724.jar:$ROOT_PATH/../lib/OpenCSV.jar:$ROOT_PATH/../lib/StatusListener29.jar:$ROOT_PATH/../lib/talend-oracle-timestamptz.jar:$ROOT_PATH/idrt_transformation_0_5.jar: i2b2transmart.idrt_transformation_0_5.IDRT_TRANSFORMATION --context=Default "$@" 
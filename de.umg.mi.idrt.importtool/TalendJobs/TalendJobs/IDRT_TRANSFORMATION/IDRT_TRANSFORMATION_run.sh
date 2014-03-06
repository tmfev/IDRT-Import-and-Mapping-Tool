#!/bin/sh
cd `dirname $0`
 ROOT_PATH=`pwd`
 java -Xms256M -Xmx1024M -cp $ROOT_PATH/../lib/dom4j-1.6.1.jar:$ROOT_PATH/../lib/ojdbc5.jar:$ROOT_PATH/../lib/OpenCSV.jar:$ROOT_PATH/../lib/StatusListener29.jar:$ROOT_PATH/../lib/talend-oracle-timestamptz.jar:$ROOT_PATH/../lib/talendcsv.jar:$ROOT_PATH/../lib/talend_file_enhanced_20070724.jar:$ROOT_PATH/../lib/tns.jar:$ROOT_PATH:$ROOT_PATH/../lib/systemRoutines.jar:$ROOT_PATH/../lib/userRoutines.jar::.:$ROOT_PATH/idrt_transformation_0_5.jar: tos.idrt_transformation_0_5.IDRT_TRANSFORMATION --context=Default "$@" 
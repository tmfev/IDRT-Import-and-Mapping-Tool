%~d0
 cd %~dp0
 java -Xms256M -Xmx1024M -cp ../lib/dom4j-1.6.1.jar;../lib/ojdbc5.jar;../lib/OpenCSV.jar;../lib/StatusListener29.jar;../lib/talend-oracle-timestamptz.jar;../lib/talendcsv.jar;../lib/talend_file_enhanced_20070724.jar;../lib/tns.jar;../lib/systemRoutines.jar;../lib/userRoutines.jar;.;idrt_transformation_0_5.jar; tos.idrt_transformation_0_5.IDRT_TRANSFORMATION --context=Default %* 
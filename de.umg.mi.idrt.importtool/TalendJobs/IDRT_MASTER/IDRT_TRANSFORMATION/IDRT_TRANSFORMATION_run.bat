%~d0
cd %~dp0
java -Xms256M -Xmx1024M -cp .;../lib/routines.jar;../lib/log4j-1.2.16.jar;../lib/ojdbc6.jar;../lib/dom4j-1.6.1.jar;../lib/talendcsv.jar;../lib/tns.jar;../lib/talend_file_enhanced_20070724.jar;../lib/OpenCSV.jar;../lib/StatusListener29.jar;../lib/talend-oracle-timestamptz.jar;idrt_transformation_0_5.jar; i2b2transmart.idrt_transformation_0_5.IDRT_TRANSFORMATION --context=Default %* 
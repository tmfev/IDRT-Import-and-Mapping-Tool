%~d0
cd %~dp0
java -Xms256M -Xmx1024M -cp .;../lib/routines.jar;../lib/log4j-1.2.16.jar;../lib/dom4j-1.6.1.jar;../lib/talendcsv.jar;../lib/talend_file_enhanced_20070724.jar;../lib/StatusListener29.jar;p21_erweiterung_2011_2012_0_1.jar; i2b2transmart.p21_erweiterung_2011_2012_0_1.P21_ERWEITERUNG_2011_2012 --context=Default %* 
%~d0
cd %~dp0
java -Xms256M -Xmx1024M -cp .;../lib/routines.jar;../lib/log4j-1.2.16.jar;../lib/dom4j-1.6.1.jar;../lib/ojdbc14.jar;../lib/OpenCSV.jar;../lib/StatusListener29.jar;../lib/ojdbc6.jar;../lib/postgresql-9.2-1003.jdbc3.jar;dropioetables_0_1.jar; i2b2transmart.dropioetables_0_1.DropIOETables --context=Default %* 
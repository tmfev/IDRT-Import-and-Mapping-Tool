%~d0
cd %~dp0
java -Xms256M -Xmx1024M -cp .;../lib/routines.jar;../lib/StatusListener29.jar;../lib/dom4j-1.6.1.jar;../lib/log4j-1.2.16.jar;p21_erweiterung_2010_0_1.jar; idrt.p21_erweiterung_2010_0_1.P21_ERWEITERUNG_2010 --context=Default  --context_param DB_StagingI2B2_DatabaseType="" --context_param DB_StagingI2B2_DriverClass="" --context_param DB_StagingI2B2_Host="" --context_param DB_StagingI2B2_Instance="" --context_param DB_StagingI2B2_jdbcurl="" --context_param DB_StagingI2B2_Password="" --context_param DB_StagingI2B2_Port="" --context_param DB_StagingI2B2_Schema="" --context_param DB_StagingI2B2_Username="" --context_param DB_StagingI2B2_WHType="" %* 
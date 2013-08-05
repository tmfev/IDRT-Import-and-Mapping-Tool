%~d0
 cd %~dp0
java -Xms256M -Xmx1024M -cp classpath.jar; tos.tosidrtconnector_0_1.TOSIDRTConnector --context=Default %* 
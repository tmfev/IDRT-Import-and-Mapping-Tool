cd `dirname $0`
 ROOT_PATH=`pwd`
java -Xms256M -Xmx1024M -cp classpath.jar: tos.tosidrtconnector_0_1.TOSIDRTConnector --context=Default "$@" 
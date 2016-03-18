#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Xms256M -Xmx1024M -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-1.2.16.jar:$ROOT_PATH/../lib/dom4j-1.6.1.jar:$ROOT_PATH/../lib/ojdbc14.jar:$ROOT_PATH/../lib/OpenCSV.jar:$ROOT_PATH/../lib/StatusListener29.jar:$ROOT_PATH/../lib/ojdbc6.jar:$ROOT_PATH/../lib/postgresql-9.2-1003.jdbc3.jar:$ROOT_PATH/dropioetables_0_1.jar: i2b2transmart.dropioetables_0_1.DropIOETables --context=Default "$@" 
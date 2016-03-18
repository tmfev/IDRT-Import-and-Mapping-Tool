#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Xms256M -Xmx1024M -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-1.2.16.jar:$ROOT_PATH/../lib/dom4j-1.6.1.jar:$ROOT_PATH/../lib/StatusListener29.jar:$ROOT_PATH/p21_erweiterung_2010_0_1.jar: i2b2transmart.p21_erweiterung_2010_0_1.P21_ERWEITERUNG_2010 --context=Default "$@" 
#!/bin/sh
ID=`ps -ef |grep '.*SNAPSHOT.jar$'|awk '{print $2}'`
echo $ID
echo "---------------" 
for id in $ID
do
kill -9 $id
echo "killed $id"
done
echo "---------------"

nohup java -jar  -Xms1024m -Xmx1024m  gateway-0.0.1-SNAPSHOT.jar  > gateway.log 2>&1 &
nohup java -jar  -Xms128m -Xmx256m  microsysservice-0.0.1-SNAPSHOT.jar &
nohup java -jar  -Xms64m -Xmx128m  auserver-0.0.1-SNAPSHOT.jar &
nohup java -jar  -Xms64m -Xmx128m  micromonitorservice-0.0.1-SNAPSHOT.jar &

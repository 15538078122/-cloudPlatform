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

nohup java -jar gateway-0.0.1-SNAPSHOT.jar &
nohup java -jar microsysservice-0.0.1-SNAPSHOT.jar &
nohup java -jar auserver-0.0.1-SNAPSHOT.jar &
nohup java -jar micromonitorservice-0.0.1-SNAPSHOT.jar &

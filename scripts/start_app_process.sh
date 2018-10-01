#!/bin/sh

cd /home/ec2-user/case-yellow/
java -jar case-yellow-analysis.jar > /dev/null 2> /dev/null < /dev/null &

cd /home/ec2-user/case-yellow/dashboard

PORT=7777
export MB_JETTY_PORT=$PORT
echo running Metabase on port $PORT
java -jar metabase.jar > logfile.log 2> logfile.log < logfile.log &

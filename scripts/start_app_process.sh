#!/bin/sh

cd /home/ec2-user/case-yellow/
java -jar case-yellow-analysis.jar > /dev/null 2> /dev/null < /dev/null &

/home/ec2-user/case-yellow/dashboard

/run_dashboard.sh > /dev/null 2> /dev/null < /dev/null &
#!/bin/sh

cd /home/ec2-user/case-yellow/
rm -rf case-yellow-analysis.jar

cd /home/ec2-user/case-yellow/dashboard
rm -rf metabase.db.mv.db
rm -rf metabase.db.trace.db
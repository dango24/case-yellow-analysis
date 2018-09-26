#!/usr/bin/env bash

cd /home/ec2-user/case-yellow/dashboard

aws s3 cp s3://cy-codebuild/cy-metabase-artifact/cy-metabase-db.zip cy-metabase-db.zip
unzip cy-metabase-db.zip
rm -rf cy-metabase-db.zip

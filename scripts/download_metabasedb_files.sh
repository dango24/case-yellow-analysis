#!/usr/bin/env bash

cd /home/ec2-user/case-yellow/dashboard
aws s3 sync . s3://cy-codebuild/cy-metabase-artifact/cy-metabase-db.zip

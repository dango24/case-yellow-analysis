#!/usr/bin/env bash

cd /home/ec2-user/case-yellow/dashboard
zip -r cy-metabase-db.zip metabase.db.mv.db metabase.db.trace.db
aws s3 cp cy-metabase-db.zip s3://cy-codebuild/cy-metabase-artifact/

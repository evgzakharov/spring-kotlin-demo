#!/bin/sh -ex

curl -s -XGET http://localhost:8080 | jq .

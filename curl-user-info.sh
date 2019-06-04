#!/bin/sh -ex

curl -XGET http://localhost:8080/1 -v  | jq .

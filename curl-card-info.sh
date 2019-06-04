#!/bin/sh -ex

curl -XGET http://localhost:8080 -v -d '5559 **** 20202' -H "Content-Type: application/json; charset=UTF-8" | jq .

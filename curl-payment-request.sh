#!/bin/sh -ex

curl -XPOST http://localhost:8080 -v -d "@payment-request.json" -H "Content-Type: application/json; charset=UTF-8" | jq .

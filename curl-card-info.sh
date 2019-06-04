#!/bin/sh -ex

curl -XGET http://localhost:8080/55592020 -v | jq .

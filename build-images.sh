#!/bin/sh -ex

docker build --tag service-auth:1.0.0 service-auth/

docker build --tag service-card:1.0.0 service-card/

docker build --tag service-payment:1.0.0 service-payment/

docker build --tag service-user:1.0.0 service-user/

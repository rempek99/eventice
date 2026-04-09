#!/bin/sh

LOCAL_ENV=./local.env

if [ -f $LOCAL_ENV ] ; then
    echo "Reading envs from $LOCAL_ENV"
    set -a;
    . $LOCAL_ENV
    set +a;
else
    echo "$LOCAL_ENV file not found"
fi


if [[ -z "$DB_URL" ]] ; then
    echo "Please provide DB_URL variable!";
    exit 1;
fi

if [[ -z "$DB_USERNAME" ]] ; then
    echo "Please provide DB_USERNAME variable!"
    exit 1;
fi

if [[ -z "$DB_PASSWORD" ]] ; then
    echo "Please provide DB_PASSWORD variable!"
    exit 1;
fi

if [[ -z "$JWT_SECRET" ]] ; then
    echo "Please provide JWT_SECRET variable in base64 format &gt 256bytes!"
    exit 1;
fi

java -jar ./target/*.jar


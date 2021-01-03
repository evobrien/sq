#!/bin/sh

git archive \
    --format zip \
    --output ../squarespace-android-interview.zip \
    --prefix=squarespace-android-interview/ \
    master

echo 'DONE'

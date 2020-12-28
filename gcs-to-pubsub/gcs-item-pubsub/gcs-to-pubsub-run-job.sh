#!/bin/bash

export GCLOUD_PATH=/Users/hgo2/Downloads/google-cloud-sdk/bin

${GCLOUD_PATH}/gcloud dataflow jobs run gerard-gcs-to-pubsub-sample-001 \
--region=us-east4  \
--gcs-location gs://tst1-integration-3ca6-gerard/pipeline/template/template-gcs-sample-001

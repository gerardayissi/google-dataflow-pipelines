#!/bin/bash

export PROJECT_ID=tst1-integration-3ca6

mvn compile exec:java \ls
-Dexec.mainClass=com.google.cloud.gcsitempubsub \
-Dexec.cleanupDaemonThreads=false \
-Dexec.args="--project=${PROJECT_ID} \
--region=us-east4 \
--stagingLocation=gs://tst1-integration-3ca6-gerard/pipeline/staging \
--templateLocation=gs://tst1-integration-3ca6-gerard/pipeline/template/template-gcs-sample-001 \
--serviceAccount=project-service-account@tst1-integration-3ca6.iam.gserviceaccount.com \
--subnetwork=https://www.googleapis.com/compute/v1/projects/network-b2b9/regions/us-east4/subnetworks/np-integration4 \
--numWorkers=10 \
--autoscalingAlgorithm=THROUGHPUT_BASED \
--workerMachineType=n1-standard-2 \
--enableStreamingEngine=true \
--maxNumWorkers=10 \
--filesToStage=/Users/hgo2/IdeaProjects/google-dataflow-pipelines/gcs-to-pubsub/gcs-item-pubsub/target/gcs-item-pubsub-1.0.jar \
--runner=DataflowRunner"
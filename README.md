# google-dataflow-pipelines

in jenkins:
  1. Manage Jenkins -> Global Tool Configuration
    a. Install jdk8 (1.8.0 221 or similar)
    b. Install Maven 3.6.3 (or similar)
add permission to any sh file that will be executed from inside jenkins in the jenkinsfile: git add --chmod=+x "filename"

find gcloud home path: dirname $(which gcloud)

find terraform home path: dirname $(which terraform)

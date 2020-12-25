package com.google.cloud;

import java.io.IOException;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;

        import org.apache.beam.runners.dataflow.DataflowRunner;
        import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
        import org.apache.beam.sdk.Pipeline;
        import org.apache.beam.sdk.io.TextIO;
        import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
        import org.apache.beam.sdk.options.PipelineOptionsFactory;
        import org.apache.beam.sdk.transforms.DoFn;
        import org.apache.beam.sdk.transforms.ParDo;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

/**
 * A starter example for writing Beam programs.
 *
 * <p>The example takes two strings, converts them to their upper-case
 * representation and logs them.
 *
 * <p>To run this starter example locally using DirectRunner, just
 * execute it without any additional parameters from your favorite development
 * environment.
 *
 * <p>To run this starter example using managed resource in Google Cloud
 * Platform, you should specify the following command-line options:
 *   --project=<YOUR_PROJECT_ID>
 *   --stagingLocation=<STAGING_LOCATION_IN_CLOUD_STORAGE>
 *   --runner=DataflowRunner
 */

public class gcsitempubsub {
    private static final Logger LOG = LoggerFactory.getLogger(gcsitempubsub.class);

    private static String workingBucket;
    private static String projectId;;
    private static String region;
    private static String pipelineName;
    private static String subnetwork;
    private static String serviceaccount;
    private static String gcptemplocation;
    private static String templatelocation;
    //private static String topic =  "projects/tasl-omni-stg-01-ops/topics/INB_XINT_ItemQueueMSGType_GCPQ";
    private static String topic =  "projects/tst1-integration-3ca6/topics/tb-mao-pub-item-image-003";
    private static String brand = "TMW";
    //private static String bucket = "gs://tb-mao-item-fullfeed-bucket-tst-001/out/TMW_PRD_ITEM_CODE_1_12072020.csv";
    private static String bucket = "gs://tb-mao-item-image-bucket-test-001/in/*.txt";
    private static int i = 0;

    public static void main(String[] args)
            throws Exception, IOException {


        workingBucket = "gs://tb-mao-processing-tst-001/in";
        projectId = "tst1-integration-3ca6";
        region = "us-east4";
        subnetwork = "https://www.googleapis.com/compute/v1/projects/network-b2b9/regions/us-east4/subnetworks/np-integration4";
        serviceaccount = "project-service-account@tst1-integration-3ca6.iam.gserviceaccount.com";
        pipelineName = "gerard-tbi-mao-gcs-to-pubsub-001";
        gcptemplocation = "gs://tst1-integration-3ca6-gerard/dataflow/temp";
        templatelocation = "gs://tst1-integration-3ca6-gerard/pipeline/template/template-gcs-sample-001";

        DataflowPipelineOptions options = PipelineOptionsFactory.create().as(DataflowPipelineOptions.class);
        options.setRunner(DataflowRunner.class);
        options.setProject(projectId);
        options.setStagingLocation(workingBucket);
        options.setTempLocation(workingBucket + "/temp");
        options.setJobName(pipelineName);
        options.setRegion(region);
        options.setSubnetwork(subnetwork);
        options.setServiceAccount(serviceaccount);
        options.setGcpTempLocation(gcptemplocation);
        options.setTemplateLocation(templatelocation);

        options.setStreaming(true);

        Pipeline p = Pipeline.create(options);
        p.apply("Read Input File", TextIO.read().from(bucket))
                .apply(ParDo.of(new DoFn<String, String>() {
                    private static final long serialVersionUID = 3878919927975874950L;
                    @ProcessElement
                    public void processElement(ProcessContext c) {
                        String msg = c.element();
                        String payload = "{  \"messages\": [    {      \"attributes\": {  \"User\": \"eisuser\",        \"Organization\": \""+brand+"\"      },      \"data\": \""+java.util.Base64.getEncoder().encodeToString(msg.getBytes())+"\"    }  ]}";
                        LOG.info("RECORD COUNT: "+i);
                        LOG.info("PAYLOAD: "+payload);
                        c.output(payload);
                        i=i+1;
                    }
                }))
                .apply("Write To Pubsub Topic", PubsubIO.writeStrings().to(topic));
        p.run();
    }

}
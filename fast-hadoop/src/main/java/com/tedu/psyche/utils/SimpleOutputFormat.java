package com.tedu.psyche.utils;

import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/06
 */
public class SimpleOutputFormat extends TextOutputFormat {
    public static void setOutputName(JobContext job, String name) {
        job.getConfiguration().set(BASE_OUTPUT_NAME, name);
    }
}

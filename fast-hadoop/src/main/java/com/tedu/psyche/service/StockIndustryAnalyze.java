package com.tedu.psyche.service;

import com.tedu.psyche.utils.HDFSUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * describe:
 * 股票行业分析
 * @author liang
 * @date 2019/04/30
 */
public class StockIndustryAnalyze {
    private static Logger log = LoggerFactory.getLogger(StockIndustryAnalyze.class);
    static String input = "hdfs://192.168.221.115:9000/opt/hadoop/data/stocks.csv";
    static String output="hdfs://192.168.221.115:9000/opt/hadoop/output/industry";
    static {
        System.setProperty("hadoop.home.dir", "/Users/sunliangliang/Documents/develop-tools/hadoop-3.1.2");
    }

    /**
     * 按照行业统计，行业内有多少个
     *
     */
    public static class IndustryMap extends Mapper<LongWritable, Text, Text, IntWritable> {
        // 实现map函数
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 输入的一行预处理文本
            int length = value.toString().split(",").length;
            StringTokenizer itr = new StringTokenizer(new String(value.getBytes(),"GBK"));
            while (itr.hasMoreTokens()) {
                String line = itr.nextToken();
                if (line.contains("ts_code")){
                    continue;
                }
                log.info(">>>> industry :{}",line);
                if (!StringUtils.isEmpty(line)) {
                    String []tokens = line.split(",");
                    String industry = tokens[5];//area
                    context.write(new Text(industry), new IntWritable(1));
                }
            }
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable v : values) {
                count += v.get();
            }
            //输出key
            context.write(key, new IntWritable(count));
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "StockStatic");
        HDFSUtil.remove(conf, output);
        job.setJarByClass(StockIndustryAnalyze.class);
        // 设置Map和Reduce处理类
        job.setMapperClass(IndustryMap.class);
        job.setReducerClass(Reduce.class);
        // 设置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        if (job.waitForCompletion(true)) {
            HDFSUtil.cat(conf, output + "/part-r-00000");
            log.info("--->success");
        } else {
            log.info("---->fail");
        }
    }

}

package com.tedu.psyche.service;

import com.tedu.psyche.utils.HDFSUtil;
import com.tedu.psyche.utils.MapReduceOutFormat;
import com.tedu.psyche.utils.SimpleOutputFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/06
 */
public class StockPriceAndAmout {
    private static Logger log = LoggerFactory.getLogger(StockPriceAndAmout.class);
    static String input = "hdfs://192.168.221.115:9000/opt/hadoop/data/000002.csv";
    static String output="hdfs://192.168.221.115:9000/opt/hadoop/output";
    /**
     * 按照行业统计，行业内有多少个
     *date,open,high,low,close,volume,price_change,p_change,p_change,ma5,ma10,ma20,v_ma5,v_ma10,v_ma20
     * 2019-04-23,29.4,29.75,29.25,29.51,599063.81,0.0,0.0,0.0,30.436,31.046,31.051,662205.84,617329.41,655693.8434.61
     * 取5日均价与5日均量
     */
    public static class ScattergraphMap extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        // 实现map函数
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 输入的一行预处理文本
            StringTokenizer itr = new StringTokenizer(new String(value.getBytes(),"GBK"));
            while (itr.hasMoreTokens()) {
                String line = itr.nextToken();

                if (line.contains("date")){
                    continue;
                }
                if (!StringUtils.isEmpty(line)) {
                    String []tokens = line.split(",");
                    String ma5 = tokens[9];
                    String v_max5 = tokens[12];
                    log.info(">>>>date = {}  ma5 :{}，v_max5 = {}",tokens[0],ma5,v_max5);
                    context.write(new Text(ma5), new DoubleWritable(Double.parseDouble(v_max5)));
                }
            }
        }
    }


    public static class ScattergrapReduce extends Reducer<Text, IntWritable, Text, DoubleWritable> {
        public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
            int count = 0;
            for (DoubleWritable v : values) {
                count += v.get();
            }
            //输出key
            context.write(key, new DoubleWritable(count));
        }
    }

    private static class TestOut extends TextOutputFormat {
        protected static void setOutputName(JobContext job, String name) {
            job.getConfiguration().set(BASE_OUTPUT_NAME, name);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "StockStatic");
        HDFSUtil.remove(conf, output);
        job.setJarByClass(StockPriceAndAmout.class);
        // 设置Map和Reduce处理类
        job.setMapperClass(ScattergraphMap.class);
        job.setReducerClass(ScattergrapReduce.class);
        // 设置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(SimpleOutputFormat.class);
        SimpleOutputFormat.setOutputName(job,"price-amount");
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        if (job.waitForCompletion(true)) {
            HDFSUtil.cat(conf, output + "/price-amount-r-00000");
            log.info("--->success");
        } else {
            log.info("---->fail");
        }
    }




















}

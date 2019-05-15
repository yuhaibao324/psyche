package com.tedu.psyche.service;

import com.tedu.psyche.utils.HDFSUtil;
import com.tedu.psyche.utils.SimpleOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/13
 */
public class Average {
    private static Logger log = LoggerFactory.getLogger(Average.class);
    static String input = "hdfs://192.168.221.115:9000/opt/hadoop/data/000002.csv";
    static String output="hdfs://192.168.221.115:9000/opt/hadoop/output";
    public static class AverageMapper extends Mapper<Object, Text, Text, DoubleWritable> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String data = value.toString();
            StringTokenizer splited = new StringTokenizer(data,"\n"); //成绩分隔符
            double totalPrice = 0.0;
            while (splited.hasMoreElements()) {
                String lines = splited.nextToken();
                log.info(">>>>line = {}",lines);
                String []tokens = lines.split(",");
                String max5 = tokens[9];
                String v_max5 = tokens[12];
                if (lines.contains("date")){
                    continue;
                }
                totalPrice += Double.parseDouble(max5);
                log.info(">>>>date = {}  ma5 :{}，v_max5 = {}",tokens[0],max5,v_max5);
                context.write(new Text("max5"), new DoubleWritable(Double.parseDouble(max5)));
                context.write(new Text("v_max5"), new DoubleWritable(Double.parseDouble(v_max5)));
            }
            log.info(">>>>totalPrice = {}",totalPrice);
        }
    }


    public static class AverageReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            Iterator<DoubleWritable> iterator =  values.iterator();
            float sum = 0;
            int count = 0;
            while (iterator.hasNext()){
                double tmp = iterator.next().get();
                sum += tmp;
                count++;
            }
            float average  = sum /count;
            context.write(key,new DoubleWritable(average));
            System.out.println("ke: "+key + " ave: "+ average);
        }
    }


    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = new Job(conf, "Average");
        HDFSUtil.remove(conf, output);
        job.setJarByClass(Average.class);
        // 设置Map和Reduce处理类
        job.setMapperClass(AverageMapper.class);
        job.setReducerClass(AverageReducer.class);
        // 设置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(SimpleOutputFormat.class);
        SimpleOutputFormat.setOutputName(job,"average");
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        if (job.waitForCompletion(true)) {
            HDFSUtil.cat(conf, output + "/average-r-00000");
            log.info("--->success");
        } else {
            log.info("---->fail");
        }





    }






}

package com.tedu.psyche.utils;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/06
 */
public class MapReduceOutFormat extends FileOutputFormat<Text, Text> {

    @Override
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext job)
            throws IOException, InterruptedException {

        FileSystem fs = FileSystem.newInstance(job.getConfiguration());

        //自定义输出路径及文件名，把数学成绩和英语成绩分别输出到不同的文件中
        final FSDataOutputStream math = fs.create(new Path("/score/math.txt"));
        final FSDataOutputStream english = fs.create(new Path("/score/english.txt"));

        RecordWriter<Text, Text> recordWriter = new RecordWriter<Text, Text>() {

            @Override
            public void write(Text key, Text value) throws IOException,
                    InterruptedException {
                if(key.toString().contains("math")){
                    math.writeUTF(key.toString());
                }
                if(key.toString().contains("english")){
                    english.writeUTF(key.toString());
                }

            }

            @Override
            public void close(TaskAttemptContext context) throws IOException,
                    InterruptedException {
                if (math!=null) {
                    math.close();
                }
                if (english!=null) {
                    english.close();
                }
            }
        };

        return recordWriter;
    }

}

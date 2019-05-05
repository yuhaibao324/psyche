package com.tedu.psyche.utils;

import com.liangliang.fastbase.exception.ExceptionLog;
import com.tedu.psyche.service.StockAreaAnalyze;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author liang
 * @date 2019/04/30
 */
public class HDFSUtil {
    private static Logger log = LoggerFactory.getLogger(HDFSUtil.class);

    static {
        System.setProperty("hadoop.home.dir", "/Users/sunliangliang/Documents/develop-tools/hadoop-3.1.2");
    }

    public static void listFiles() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.221.115:9000");
        FileSystem fs = FileSystem.newInstance(conf);
        // true 表示递归查找 false 不进行递归查找
        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path("/"), true);
        while (iterator.hasNext()) {
            LocatedFileStatus next = iterator.next();
            log.info("-------->${path}={}",next.getPath());
        }
        FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
        for (int i = 0; i < fileStatuses.length; i++) {
            FileStatus fileStatus = fileStatuses[i];
            log.info("{fdfspath}:{}",fileStatus.getPath());
        }
    }
    public void upload() throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.221.115:9000");
        FileSystem fs = FileSystem.get(conf);
        fs.copyFromLocalFile(new Path("/Users/sunliangliang/Documents/personal/stocks.csv"), new Path("hdfs://192.168.221.115:9000/opt/hadoop/data/"));  //第一个为本地文件路径,第二个为hadoop路径
        long cost = System.currentTimeMillis() - currentTimeMillis;
        log.info(">>>>>upload  success<<< , ${cost} = {} ",cost);
    }

    public void download(String input,String output) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.221.115:9000");
        FileSystem fs = FileSystem.newInstance(conf);
        fs.copyToLocalFile(new Path(input), new Path(output));
        long c = System.currentTimeMillis() - currentTimeMillis;
        log.info(">>>>>download success<<< , ${cost} = {} ",c);
    }


    /**
     * 在hdfs更目录下面创建test1文件夹
     * @throws IOException
     */
    public void mkdir(String path) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.10.110:9001");
        FileSystem fs = FileSystem.newInstance(conf);
        fs.mkdirs(new Path(path));
        log.info(">>>> mkdir success<<<<<");
    }


    /**
     * 输出指定文件内容
     *
     * @param conf     HDFS配置
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException
     */
    public static void cat(Configuration conf, String filePath) throws IOException {
        InputStream in = null;
        Path file = new Path(filePath);
        FileSystem fileSystem = file.getFileSystem(conf);
        try {
            in = fileSystem.open(file);
            IOUtils.copyBytes(in, System.out, 4096, true);
        } finally {
            if (in != null) {
                IOUtils.closeStream(in);
            }
        }
    }

    /**
     * read hdfs by line and output lines
     * @param conf
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String> readLine(Configuration conf, String filePath){
        List<String> lines = new ArrayList<>();
        InputStream in = null;
        Path file = new Path(filePath);
        try {
            FileSystem fileSystem = file.getFileSystem(conf);
            in = fileSystem.open(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while(reader.ready()) {
                String line = reader.readLine();
                lines.add(line);
            }
        }catch (IOException e){
            log.error("readLine error : error = {}",ExceptionLog.getErrorStack(e));
        }
        return lines;
    }

    /**
     * 删除指定目录
     *
     * @param conf
     * @param dirPath
     * @throws IOException
     */
    public static void remove(Configuration conf, String dirPath) throws IOException {
        boolean delResult = false;
        Path targetPath = new Path(dirPath);
        System.setProperty("hadoop.home.dir", "/Users/sunliangliang/Documents/develop-tools/hadoop-3.1.2");
        FileSystem fs = targetPath.getFileSystem(conf);
        if (fs.exists(targetPath)) {
            delResult = fs.delete(targetPath, true);
            if (delResult) {
                log.info(targetPath + " has been deleted sucessfullly.");
            } else {
                log.info(targetPath + " deletion failed.");
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        listFiles();
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.221.115:9000");
        readLine(conf,"hdfs://192.168.221.115:9000/opt/hadoop/output/part-r-00000");
    }
}

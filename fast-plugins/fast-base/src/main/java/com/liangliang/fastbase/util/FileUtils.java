package com.liangliang.fastbase.util;

import com.alibaba.fastjson.JSON;
import com.liangliang.fastbase.exception.ExceptionLog;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Slf4j
public class FileUtils {
    public static void loadCsv(String path){
        File csv = new File("/Users/sunliangliang/Desktop/stocks.csv");
        csv.setReadable(true);//设置可读
        csv.setWritable(true);//设置可写
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        ArrayList<String> allString = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
                System.out.println(new String(everyLine.getBytes(),"gbk"));
                allString.add(everyLine);
            }
            System.out.println("csv表格中所有行数：" + allString.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static List<String> listFiles(String path){
        File file = new File(path);
        File[] tempList = file.listFiles();
        List<String> fileNames = new ArrayList<>();
        for (File tempFile : tempList){
            if (tempFile.isFile()){
                fileNames.add(tempFile.getName());
            }
        }
        log.info(">>>>filenames = {}", JSON.toJSONString(fileNames));
        return fileNames;
    }

    public static void main(String[] args) {
        listFiles("/Users/sunliangliang/Documents/personal/csv/");
    }

}

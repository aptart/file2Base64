package com.github.aptart.file2Base64;

import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.Base64;

@Log
public class Main {
    public static void main (String[] args) throws IOException {
        init();
        if (args.length == 0) {
            System.out.println("无文件（The file is empty）");
            log.severe("无文件（The file is empty）");
            return;
        } else {
            log.info("文件加载成功（file load success）");
        }
        int tmp = 0;
        List<String> str = new ArrayList<String>();

        for (String filePath : args ) {
            tmp++;
            log.info("正在进行base64编码。。（base64 encoding..）" + tmp + "/" + args.length + "：" + filePath);
            str.add(file2Base64(filePath));
            log.info("base64编码完成（base64 encode success）" + tmp + "/" + args.length);
        }

        for (String demo : str){
            System.out.println(demo);
        }
        //不要使用idea build进行打包，使用idea中maven图标进行打包，吐了。clean-package
        //java -jar xxx.jar
        //java -cp xxxx.jar xxx.Main
    }

    public static String file2Base64(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[fileInputStream.available()];
        fileInputStream.read(buffer);
        String encoder = Base64.getEncoder().encodeToString(buffer);
        fileInputStream.close();

        String suffixName = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (suffixName = filePath.substring(filePath.lastIndexOf(".") + 1)){
            case "png":
                suffixName = "image/png";
                break;
            case "jpg":
                suffixName = "image/jpg";
                break;
        }

        if(suffixName.equals("error")){
            return "错误（encode error）";
        }else {
            return "![文件说明](data:" + suffixName + ";base64," + encoder + ")";
        }
        //![文件说明](base64Encode)
    }

    public static String suffixDetect(String filePath) {
        //文件后缀检测
        String suffixName = filePath.substring(filePath.lastIndexOf(".") + 1);
        if(suffixName.equals("png")){
            return "image/" + suffixName;
        } else if (suffixName.equals("jpg")){
            return "image/" + suffixName;
        } else {
            return "error";
        }
    }

    public static void init() throws IOException {
        String classPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //获取类所在的路径
        try {
            classPath = java.net.URLDecoder.decode(classPath,"UTF-8");
            //解决中文路径问题
        } catch (UnsupportedEncodingException e) {
            log.severe("中文路径异常：" + e.getLocalizedMessage());
            System.out.println("详见日志文件（See the log file for details）");
        }

        File tmp = new File(classPath);
        String jarPath = tmp.getParent();
        String logConf = jarPath + "/src/main/resources/log.properties";




        //从jar包中提取资源文件，并释放到指定目录内
        InputStream inputStream = Main.class.getResourceAsStream("/log.properties");
        if (inputStream != null) {
            File file = new File(logConf);
            if (!file.getParentFile().exists()){ //如果不存在
                file.getParentFile().mkdirs(); //创建目录
                file.delete();
                file.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            outputStream.flush();
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream(logConf));
        properties.setProperty("java.util.logging.FileHandler.pattern",jarPath + "/file2Base64.log");
        System.out.println("程序日志文件（log file）："+jarPath + "/file2Base64.log");
        FileOutputStream fileOutputStream = new FileOutputStream(logConf);
        properties.store(fileOutputStream,"log conf");
        fileOutputStream.close();

        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration(new FileInputStream(logConf));

        //InputStream input = Main.class.getResourceAsStream("/log.properties");
        //不释放jar包中资源文件，直接使用
        //manager.readConfiguration(input);

        log.info("jar文件路径为（jar file path）:" + jarPath);
        log.info("日志配置文件加载成功（log configuration file loaded successfully）：" + logConf);
    }
}

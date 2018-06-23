package com.cup.cw.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by majingmj on 18-1-23.
 */
public class FileUtils {
    private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());
    /**
     * 将字符串写入到指定文件中
     *
     * @param content
     *            准备写入文件的内容
     * @param filepath
     *            准备写入文件的路径
     * @param isdelete
     *             为true将原文件内容删除，重新写入内容；为false将内容追加到原文件中
     * @return 是否写入成功
     */
    public static boolean writeString(String content, String filepath, boolean isdelete){
        try{
            creatFile(filepath, isdelete);
            FileWriter writer;
            if (isdelete) {
                writer = new FileWriter(filepath);
            } else {
                writer = new FileWriter(filepath, true);
            }
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            LOG.error("[FileUtils.writeString]..." + filepath, e);
            return false;
        }

        return true;
    }

    /**
     * 创建指定文件
     *
     * @param filePath
     *            创建文件的路径
     * @param isdelete
     *             为true将原文件内容删除，重新写入内容；为false将内容追加到原文件中
     * @return 是否创建成功
     */
    public static boolean creatFile(String filePath, boolean isdelete){
        if (filePath == null || filePath.trim().length() == 0) {
            return false;
        }
        String[] spath = filePath.split("/");
        String fpath = "";
        String filename = null;
        for (int i = 0; i < spath.length; i++){
            if (i == 0){
                fpath = spath[i];
            } else if (i == spath.length - 1) {
                filename = spath[i];
            } else {
                fpath = fpath + "/" + spath[i];
            }

            File file =new File(fpath);
            if  (!file.exists()  && !file.isDirectory()){
                file .mkdir();
            }
        }

        try {
            File file;
            if (filename == null) {
                file = new File(fpath);
            } else {
                file = new File(fpath + "/" + filename);
            }
            if (isdelete) {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } else {
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            return true;
        } catch (IOException e) {
            LOG.error("[FileUtils.createFile]..." + filePath, e);
            return false;
        }
    }

    /**
     * @param dirPath
     *
     * @param needdelete
     *          为true时会先递归删除目录
     * @return 是否创建成功
     */
    public static boolean creatDir(String dirPath, boolean needdelete) {
        if (needdelete) {
            deletePath(dirPath);
        }
        File file = new File(dirPath);
        boolean result = file.mkdirs();
        return result;
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param path
     *            删除的文件路径
     * @return 是否删除成功
     */
    public static boolean deletePath(String path){
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deletePath(dir + "/" + children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获得目录下所有文件
     *
     * @param path
     *            目录路径
     * @return 该目录下所有文件
     */
    public static List<File> traverseFolder(String path){
        List<File> result = new ArrayList<File>();

        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                result.add(file2);
                if (file2.isDirectory()) {
                    list.add(file2);
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    result.add(file2);
                    if (file2.isDirectory()) {
                        list.add(file2);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 从文件中按行读取存入列表中
     */
    public static List<String> readStrFile(String file){
        List<String> result = new ArrayList<String>();
        FileReader input_reader;
        try {
            input_reader = new FileReader(file);
            BufferedReader input_br = new BufferedReader(input_reader);
            String input_str = null;
            while ((input_str = input_br.readLine()) != null) {
                result.add(input_str);
            }
            input_reader.close();
            input_br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Character> readCharFile(String file) {
        List<Character> result = new ArrayList<Character>();
        FileReader input_reader;
        try {
            input_reader = new FileReader(file);
            BufferedReader input_br = new BufferedReader(input_reader);
            String input_str = null;
            while ((input_str = input_br.readLine()) != null) {
                if (input_str.length() == 1){
                    result.add(input_str.charAt(0));
                }
            }
            input_reader.close();
            input_br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param is
     * @param ue 编码
     * @return
     */
    public static List<String> readStrStream(InputStream is,  String ue) throws IOException {
        List<String> result = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), ue));
        if (br == null) {
            return result;
        }
        String line = br.readLine();
        while (line != null) {
            result.add(line);
            line = br.readLine();
        }
        br.close();
        return result;
    }
}

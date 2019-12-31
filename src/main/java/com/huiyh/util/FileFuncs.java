package com.huiyh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by huiyh on 2016/5/27.
 */
public class FileFuncs {

    /**
     * 遍历指定目录中的所有文件
     *
     * @param parentDir
     * @param func
     */
    public static void eachFile(File parentDir, EachFileFunc func) {
        func.onHasDir(parentDir,true);
        File[] files = parentDir.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                if(func.isOpenDir(file)){
                    eachFile(file,func);
                }
            }else{
                func.onHasFile(file);
            }
        }
        func.onHasDir(parentDir,false);
    }

    public interface EachFileFunc {
        public boolean isOpenDir(File dir);
        public void onHasDir(File dir, boolean isOpen);
        public void onHasFile(File file);
    }

    /**
     * 以行为单位读取文件内容，一次读一整行
     *
     * @param file
     * @param func
     */
    public static void eachLine(File file, EachLineFunc func) {
        // 参数检查
        if (file == null) {
            throw new NullPointerException("file can't be null");
        }
        if (file.isDirectory() || !file.exists()) {
            throw new IllegalStateException("file can't be a dir,and mast be exist");
        }
        if (func == null) {
            throw new NullPointerException("func can't be null");
        }

        // 功能部分
        BufferedReader reader = null;
        FileReader fReader = null;
        try {
            fReader = new FileReader(file);
            reader = new BufferedReader(fReader);
            String lineString;
            int lineIndex = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((lineString = reader.readLine()) != null) {
                func.onHasLine(lineIndex, lineString);
                lineIndex++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fReader != null) {
                try {
                    fReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public interface EachLineFunc {
        public void onHasLine(int lineNum, String line);
    }
}

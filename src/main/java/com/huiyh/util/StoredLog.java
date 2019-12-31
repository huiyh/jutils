package com.huiyh.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huiyh on 2016/2/18.
 */
public class StoredLog {
    private static final byte[] NEXT_LINE = "\n".getBytes();
    private final SimpleDateFormat format = new SimpleDateFormat("\nyyyy.MM.dd HH:mm:ss.SSS");
    private final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS");

    private final File logFile;
    private final Date date = new Date();
    private FileOutputStream outputStream;
    private PrintStream printStream;

    public static StoredLog getLogger(String prefix) {
        StoredLog logger = new StoredLog(prefix);
        return logger;
    }

    public StoredLog(String prefix) {
        if(!Log.hasShow(Log.LEVEL_ERROR)){
            logFile = null;
            return;
        }
        String log_file_time = LOG_FILE_FORMAT.format(new Date());
        logFile = new File(String.format("build/log/%s_%s.txt", prefix, log_file_time));
        File logDir = logFile.getParentFile();
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        try {
            outputStream = new FileOutputStream(logFile);
            printStream = new PrintStream(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        IOUtils.close(printStream);
        IOUtils.close(outputStream);
    }

    public StoredLog logTime() {
        if(!Log.hasShow(Log.LEVEL_INFO)){
            return this;
        }
        date.setTime(System.currentTimeMillis());
        log(format.format(date));
        return this;
    }

    public StoredLog log(String msg) {
        if(!Log.hasShow(Log.LEVEL_INFO)){
            return this;
        }
        System.out.println(msg);
        if (outputStream != null) {
            try {
                outputStream.write(msg.getBytes());
                outputStream.write(NEXT_LINE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public StoredLog error(Throwable t) {
        if(!Log.hasShow(Log.LEVEL_ERROR)){
            return this;
        }
        t.printStackTrace(printStream);
        return this;
    }
    public StoredLog error(Throwable t, String msg) {
        if(!Log.hasShow(Log.LEVEL_ERROR)){
            return this;
        }
        Exception e = new Exception(msg, t);
        e.printStackTrace(printStream);
        return this;
    }

    public void throwError(Throwable t){
        if(Log.hasShow(Log.LEVEL_ERROR)){
            t.printStackTrace(printStream);
        }
        if(t instanceof RuntimeException){
            throw (RuntimeException)t;
        }else {
            throw new RuntimeException(t);
        }
    }
    public void throwError(Throwable t, String msg){
        RuntimeException e = new RuntimeException(msg, t);
        if(Log.hasShow(Log.LEVEL_ERROR)){
            e.printStackTrace(printStream);
        }
        throw e;
    }
}

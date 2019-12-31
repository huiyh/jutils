package com.huiyh.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by huiyh on 2016/8/29.
 */
public class ZipUtils {

    public static final int BUFFER_SIZE = 8 * 1024;

    /**
     * @param sourceJarFile
     * @param out
     * @param entry
     */
    public static boolean copyEntry(ZipFile sourceJarFile, ZipArchiveOutputStream out, ZipArchiveEntry entry) {

        boolean result = false;
        InputStream inputStream = null;
        try {
            inputStream = sourceJarFile.getInputStream(entry);
            ZipArchiveEntry newEntry = new ZipArchiveEntry(entry);
            out.putArchiveEntry(newEntry);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.closeArchiveEntry();
            result = true;
            Log.d("Entry:" + entry.getName() + " copy success");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
        }
        return result;
    }
    /**
     * @param out
     * @param entryName
     */
    public static boolean addEntry(ZipArchiveOutputStream out, String entryName, File source) {

        boolean result = false;
        InputStream inputStream = null;
        try {
            ZipArchiveEntry newEntry = new ZipArchiveEntry(source, entryName);
            out.putArchiveEntry(newEntry);
            inputStream = new FileInputStream(source);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.closeArchiveEntry();
            result = true;
            Log.d("Entry:" + entryName + " add success");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
        }
        return result;
    }

    public static boolean writeEntry(ZipArchiveOutputStream zipOut, String entryName, byte[] data) {

        boolean result = false;
        try {
            ZipArchiveEntry zipEntry = new ZipArchiveEntry(entryName);
            zipOut.putArchiveEntry(zipEntry);
            zipOut.write(data);
            zipOut.closeArchiveEntry();

            result = true;
            System.out.println("Entry:" + entryName + " copy success ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

package com.huiyh.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
     * @param sourceJarFile
     * @param out
     * @param entry
     */
    public static boolean copyEntry(java.util.zip.ZipFile sourceJarFile, ZipOutputStream out, ZipEntry entry) {

        boolean result = false;
        InputStream inputStream = null;
        try {
            inputStream = sourceJarFile.getInputStream(entry);
            ZipEntry jarentry = new ZipEntry(entry.getName());
            out.putNextEntry(jarentry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.closeEntry();
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
     * @param sourceJarFile
     * @param out
     * @param entryName
     */
    @Deprecated
    public static boolean copyEntry(JarFile sourceJarFile, JarOutputStream out, String entryName) {

        ZipEntry entry = sourceJarFile.getEntry(entryName);
        if (entry == null) {
            throw new NullPointerException("Entry:" + entryName + " is null");
        }

        boolean result = false;
        InputStream inputStream = null;
        try {
            inputStream = sourceJarFile.getInputStream(entry);
            JarEntry jarentry = new JarEntry(entryName);
            out.putNextEntry(jarentry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.closeEntry();
            result = true;
            Log.d("Entry:" + entryName + " copy success");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
        }
        return result;
    }

    /**
     * 得到解压到Jar的文件夹
     *
     * @param jarPath
     * @return
     */
    public static String getOutJarFileName(String jarPath) {
        String separator = File.separator;
        String[] splits = FileUtils.splitPath(jarPath);
        StringBuilder sb = new StringBuilder();
        // 适配Linux && OS X
        if (splits[0] != null && !splits[0].equals("")
                && !splits[0].equals(" ")) {
            sb.append(File.separator + splits[0]);
        }
        for (int i = 1; i < splits.length - 1; i++) {
            sb.append(File.separator + splits[i]);
        }
        sb.append(File.separator);
        String jarName = splits[splits.length - 1];
        sb.append(jarName.substring(0, jarName.length() - ".jar".length()));
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 创建Jar的文件夹
     *
     * @param path
     */
    private static void createJarDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            FileUtils.deleteDir(dir.getAbsolutePath());
        }
        dir.mkdirs();
    }

    /**
     * 解压Jar文件到与Jar同目录的与Jar相同名字的文件夹中
     *
     * @param jarPath
     * @throws IOException
     */
    public static void unzipJar(String jarPath) throws IOException {
        // 1.通过Jar的地址，找出Jar的目录
        String outJarPath = getOutJarFileName(jarPath);
        // 2.创建解压的文件
        createJarDir(outJarPath);
        // 3.解压
        doUnZipJar(jarPath, outJarPath);
    }

    /**
     * 做压缩操作
     *
     * @param jarPath
     * @param outJarFile
     * @throws IOException
     */
    private static void doUnZipJar(String jarPath, String outJarFile) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String outFileName = outJarFile + jarEntry.getName();
                File f = new File(outFileName);
                FileUtils.makeSupDir(outFileName);
                if (jarEntry.isDirectory()) {
                    continue;
                }
                FileUtils.writeFile(jarFile.getInputStream(jarEntry), f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(jarFile);
        }

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

package com.huiyh.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huiyh on 2016/2/15.
 */
public class FileUtils {

    /**
     * 递归删除文件夹
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteDir(String dirPath) {
        boolean success = false;
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDir(files[i].getAbsolutePath());
                } else {
                    success &= files[i].delete();
                }
            }
            success &= dir.delete();
            return success;
        } else {
            return success;
        }
    }


    public static void makeSupDir(String outFileName) {
        Pattern p = Pattern.compile("[/\\" + File.separator + "]");
        Matcher m = p.matcher(outFileName);
        while (m.find()) {
            int index = m.start();
            String subDir = outFileName.substring(0, index);
            File subDirFile = new File(subDir);
            if (!subDirFile.exists()) {
                subDirFile.mkdir();
            }
        }
    }

    public static void writeFile(InputStream ips, File outputFile) {
        FileOutputStream fileOutputStream = null;
        OutputStream ops = null;
        try {
            fileOutputStream = new FileOutputStream(outputFile);
            ops = new BufferedOutputStream(fileOutputStream);
            byte[] buffer = new byte[1024];
            int nBytes = 0;
            while ((nBytes = ips.read(buffer)) > 0) {
                ops.write(buffer, 0, nBytes);
            }
            ops.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            IOUtils.close(ops);
            IOUtils.close(ips);
            IOUtils.close(fileOutputStream);
        }
    }

    public static String readFile(String filePath) {
        if (filePath == null || filePath.length() == 0 || filePath.equals("")) {
            return null;
        }
        File file = new File(filePath);
        return readFile(file);
    }

    /**
     * 读文件
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            StringBuilder builder = new StringBuilder();
            byte[] bytes = new byte[1024];
            int len;// 每次读取的实际长度
            while ((len = in.read(bytes)) != -1) {
                builder.append(new String(bytes, 0, len));
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            Log.e("文件: " + file.getAbsolutePath(),e);
        } catch (IOException e) {
            Log.e("文件: " + file.getAbsolutePath(),e);
        } finally {
            IOUtils.close(in);
        }
        return null;
    }

    /**
     * 复制文件
     *
     * @param fromFilePath
     * @param toFilePath
     * @return
     */
    public static boolean copyFile(String fromFilePath, String toFilePath) {
        boolean success = true;
        int byteRead = 0;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(fromFilePath);
            out = new FileOutputStream(toFilePath);
            byte[] buffer = new byte[1024];
            while ((byteRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        } finally {
            IOUtils.close(in);
            IOUtils.close(out);
        }
        return success;
    }

    /**
     * 追加写内容
     * 将Set中的内容写到file中去，每一个是一行
     *
     * @param set
     * @param filePath
     */
    public static void writeFileAppend(Set<String> set, String filePath) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath, true);
            os.write(System.getProperty("line.separator").getBytes());
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String string = iterator.next();
                byte[] temp = string.getBytes();
                os.write(temp);
                byte[] newLine = System.getProperty("line.separator").getBytes();
                os.write(newLine);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(os);
        }
    }

    public static void readMappingFile(String filePath, OnMappingReadingListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener 不能为空!");
        }
        Reader fileReader = null;
        BufferedReader br = null;
        try {
            fileReader = new FileReader(new File(filePath));
            br = new BufferedReader(fileReader);
            String line = br.readLine();
            while (line != null) {
                if (line.endsWith(":")) {
                    String[] mappings = line.split(" -> ");
                    if (mappings.length != 2) {
                    }
                    String first = mappings[0].trim();
                    String second = mappings[1].trim();
                    listener.onLine(line, first, second.substring(0, second.length() - 1));
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(br);
            IOUtils.close(fileReader);
        }
    }

    public interface OnMappingReadingListener {
        void onLine(String totalLine, String originalPackage,
                    String proguardPackage);
    }

    public static void checkAndWriteFileAppend(String checkFile, Set<String> set, String filePath) {
        String content = readFile(checkFile);
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath, true);
            os.write(System.getProperty("line.separator").getBytes());
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String string = iterator.next();
                if (content.contains(string)) {
                    // 如果文件里面已经有这一行了，就跳过
                    continue;
                }
                byte[] temp = string.getBytes();
                os.write(temp);
                byte[] newLine = System.getProperty("line.separator").getBytes();
                os.write(newLine);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(os);
        }
    }

    public static String[] splitPath(String path) {
        String os = System.getProperty("os.name");
        if (os != null && os.length() > 0) {
            String separator = File.separator;
            if (os.toLowerCase().startsWith("win")) {
                return path.split(separator + separator);
            } else {
                return path.split(separator);
            }
        }
        return new String[0];
    }

    /**
     * nio高速拷贝文件
     *
     * @param source
     * @param dest
     * @return
     * @throws IOException
     */
    public static boolean nioTransferCopy(File source, File dest) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            File parent = dest.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dest);
            in = inStream.getChannel();
            out = outStream.getChannel();
            return in.transferTo(0, in.size(), out) == in.size();
        } finally {
            IOUtils.close(inStream);
            IOUtils.close(in);
            IOUtils.close(outStream);
            IOUtils.close(out);
        }
    }

    /**
     * 根据指定文件,创建一个添加了指定后缀的新文件
     *
     * @param sourceFile
     * @param append
     * @return
     */
    public static File getAppendName(File sourceFile, String append) {
        String appendName = getAppendName(sourceFile.getName(), append);
        File appendFile = new File(sourceFile.getParent(), appendName);
        return appendFile;
    }

    /**
     * 根据指定文件名,创建一个添加了指定后缀的新文件名
     *
     * @param sourceName
     * @param append
     * @return
     */
    public static String getAppendName(String sourceName, String append) {
        int indexOf = sourceName.lastIndexOf(".");
        return indexOf > 0 ? sourceName.substring(0, indexOf) + append + sourceName.substring(indexOf) : sourceName + append;
    }

    public static Properties loadProperties(File file) {
        FileReader reader = null;
        try {
            Properties properties = new Properties();
            reader = new FileReader(file);
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(reader);
        }
    }

    public static void storeProperties(Properties properties, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            properties.store(fos, null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(fos);
        }
    }

    /**
     * 将文件内容按行读取放到Set里面
     *
     * @param filePath
     * @return
     */
    public static Set<String> readSet(String filePath) {
        Set<String> set = new HashSet<>();
        Reader fileReader = null;
        BufferedReader br = null;
        try {
            fileReader = new FileReader(new File(filePath));
            br = new BufferedReader(fileReader);
            String line = br.readLine();
            while (line != null) {
                set.add(line.trim());
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(br);
            IOUtils.close(fileReader);
        }
        return set;
    }

    public static List<String> readLines(File file) {

        List<String> lines = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = reader.readLine()) != null) { //数据以逗号分隔
                lines.add(lineTxt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(reader);
        }

        return lines;
    }

    public static void writeLines(File file, List<String> lines) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    public static void write(File file, String line) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(line);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }
}

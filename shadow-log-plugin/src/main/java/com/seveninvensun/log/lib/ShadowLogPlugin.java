package com.seveninvensun.log.lib;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ShadowLogPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        FileLoggerGenerator.generate();
    }

}


/*
 * Copyright (C) 2020 AndyZheng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.invensun.seven.a3demo.accessibility;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Message;
//import android.util.Log;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.CharBuffer;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import java.io.FileWriter;
//
///**
// * 文件日志.
// *
// * @author AndyZheng
// * @since 2019/5/4
// */
//public class FileLogger {

//    private static SimpleDateFormat localFormat;
//    private static Date localDate;
//
//    private static File file;
//    private static File fileDir;
//    private static String fileName;
//    private static String fileSuffix;
//    private static int fileCount = 1;
//    private static CharBuffer buffer;
//    private static Handler fileHandler;
//
//    public static void init(Context context) {
//        if (file != null) {return;}
//        try {
//            file = new File(context.getExternalCacheDir().getAbsolutePath() + "/ShadowLog/" + getTime("MM-dd HH:mm:ss") + "-Log.txt");
//            File parent = file.getParentFile();
//            if (!parent.exists()) {
//                parent.mkdir();
//            }
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//        } catch (Exception e) {
//            Log.d("FileLogger", Log.getStackTraceString(e));
//        }
//        buffer = CharBuffer.allocate(512);
//        initFileParam();
//        initFileHandler();
//    }
//
//    public static void log(int level, String tag, String msg) {
//        msg = ensureNotNull(msg);
//        tag = ensureNotNull(tag);
//        write(format(level, tag, msg));
//    }
//
//
//    private static String ensureNotNull(String value) {
//        if (value == null) {
//            value = "";
//        }
//        return value;
//    }
//
//    private static String format(int level, String tag, String msg) {
//        return String.format("%s %s/%s %s\n", getTime("MM-dd HH:mm:ss"), getPriorityInfo(level),tag, msg);
//    }
//
//    public static String getTime(String timePattern) {
//        if (localFormat == null) {
//            localFormat = new SimpleDateFormat(timePattern, Locale.getDefault());
//        }
//        if (localDate == null) {
//            localDate = new Date();
//        }
//        localDate.setTime(System.currentTimeMillis());
//        return localFormat.format(localDate);
//    }
//
//    /**
//     * 优先级信息.
//     */
//    public static String getPriorityInfo(int priority) {
//        switch (priority) {
//            case Log.VERBOSE:
//                return "V";
//            case Log.DEBUG:
//                return "D";
//            case Log.INFO:
//                return "I";
//            case Log.WARN:
//                return "W";
//            case Log.ERROR:
//                return "E";
//            case Log.ASSERT:
//                return "A";
//            default:
//                return "";
//        }
//    }
//
//    private static void initFileParam() {
//        fileDir = file.getParentFile();
//        fileSuffix = ".txt";
//        String name = file.getName();
//        if (!fileSuffix.isEmpty()) {
//            fileName = name.substring(0, name.indexOf(fileSuffix));
//        } else {
//            fileName = name;
//        }
//    }
//
//    private static void initFileHandler() {
//        HandlerThread thread = new HandlerThread("FileLogger", android.os.Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//        fileHandler = new Handler(thread.getLooper(), new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (msg.what == 1) {
//                    writeInternal((String) msg.obj);
//
//                }
//                return true;
//            }
//        });
//    }
//
//    private static void writeInternal(String data) {
//        if (data.length() < buffer.remaining()) {
//            buffer.put(data);
//        } else {// buffer剩余空间不足
//            int start = 0;
//            while (start < data.length()) {//循环检查的目的是防止data过大，分多次才能写完
//                int length = Math.min(data.length() - start, buffer.remaining());
//                buffer.put(data, start, start + length);
//                start = start + length;
//                if (buffer.remaining() == 0) { //buffer full
//                    writeToFile();
//                }
//            }
//        }
//    }
//
//    public static void writeToFile() {
//        if (buffer.position() == 0) {//有数据时才写文件
//            return;
//        }
//        if (file.length() >= 1024 * 512L) {//文件超过最大限制时创建新文件
//            if (renameOldFile()) { //NOSONAR
//                createNewFile();
//            }
//        }
//        write(file, buffer.array(), 0, buffer.position());
//        buffer.clear();
//    }
//
//    private static boolean renameOldFile() {
//        File newFile = getNewFile();
//        return file.renameTo(newFile);
//    }
//
//    private static void createNewFile() {
//        fileCount++;
//        file = getNewFile();
//    }
//
//    private static File getNewFile() {
//        return new File(fileDir, getNewFileName());
//    }
//
//    private static String getNewFileName() {
//        return fileName + fileCount + fileSuffix;
//    }
//
//    private static void write(String data) {
//        Message.obtain(fileHandler, 1, data).sendToTarget();
//    }
//
//    private static void write(File file, char[] data, int off, int len) {
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(file, true);
//            writer.write(data, off, len);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//
//                }
//            }
//        }
//    }
//}

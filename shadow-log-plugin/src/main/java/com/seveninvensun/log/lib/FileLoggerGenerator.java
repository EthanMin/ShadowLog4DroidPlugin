package com.seveninvensun.log.lib;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

public class FileLoggerGenerator {

    public static void generate() {

        TypeSpec.Builder fileLoggerBuilder = TypeSpec.classBuilder("FileLogger")
                .addModifiers(Modifier.PUBLIC);

        ClassName simpleDateFormatClass = ClassName.get("java.text", "SimpleDateFormat");
        FieldSpec simpleDateFormatField = FieldSpec.builder(simpleDateFormatClass, "localFormat").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(simpleDateFormatField);

        ClassName dateClass = ClassName.get("java.util", "Date");
        FieldSpec dateField = FieldSpec.builder(dateClass, "localDate").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(dateField);

        ClassName fileClass = ClassName.get("java.io", "File");
        FieldSpec fileField = FieldSpec.builder(fileClass, "file").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(fileField);

        FieldSpec fileDirField = FieldSpec.builder(fileClass, "fileDir").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(fileDirField);

        ClassName stringClass = ClassName.get("java.lang", "String");
        FieldSpec fileNameField = FieldSpec.builder(stringClass, "fileName").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(fileNameField);

        FieldSpec fileSuffixField = FieldSpec.builder(stringClass, "fileSuffix").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(fileSuffixField);


        ClassName intClass = ClassName.get("java.lang", "Integer");
        FieldSpec fileCountField = FieldSpec.builder(intClass, "fileCount").addModifiers(Modifier.STATIC, Modifier.PRIVATE).initializer("1").build();
        fileLoggerBuilder.addField(fileCountField);

        ClassName charBufferClass = ClassName.get("java.nio", "CharBuffer");
        FieldSpec bufferField = FieldSpec.builder(charBufferClass, "buffer").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(bufferField);

        ClassName handlerClass = ClassName.get("android.os", "Handler");
        FieldSpec handlerField = FieldSpec.builder(handlerClass, "fileHandler").addModifiers(Modifier.STATIC, Modifier.PRIVATE).build();
        fileLoggerBuilder.addField(handlerField);

        ClassName contextClass = ClassName.get("android.content", "Context");

        ParameterSpec contextPara = ParameterSpec.builder(contextClass, "context")
                .build();

        MethodSpec initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(contextPara)
                .addStatement("if (file != null) {return;}")
                .addCode("        try {\n" +
                        "            file = new File(context.getExternalCacheDir().getAbsolutePath() + \"/ShadowLog/\" + getTime(\"MM-dd HH:mm:ss\") + \"-Log.txt\");\n" +
                        "            File parent = file.getParentFile();\n" +
                        "            if (!parent.exists()) {\n" +
                        "                parent.mkdir();\n" +
                        "            }\n" +
                        "            if (!file.exists()) {\n" +
                        "                file.createNewFile();\n" +
                        "            }\n" +
                        "\n" +
                        "        } catch (Exception e) {\n" +
                        "            Log.d(\"FileLogger\", Log.getStackTraceString(e));\n" +
                        "        }")
                .addStatement("buffer = CharBuffer.allocate(512);")
                .addStatement("initFileParam();")
                .addStatement("initFileHandler();")
                .build();

        fileLoggerBuilder.addMethod(initMethod);


        ParameterSpec levelPara = ParameterSpec.builder(intClass, "level")
                .build();

        ParameterSpec tagPara = ParameterSpec.builder(stringClass, "tag")
                .build();

        ParameterSpec msgPara = ParameterSpec.builder(stringClass, "msg")
                .build();

        MethodSpec logMethod = MethodSpec.methodBuilder("log")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(levelPara)
                .addParameter(tagPara)
                .addParameter(msgPara)
                .addStatement("msg = ensureNotNull(msg);")
                .addStatement("tag = ensureNotNull(tag);")
                .addStatement("write(format(level, tag, msg));")
                .build();

        fileLoggerBuilder.addMethod(logMethod);

        ParameterSpec valuePara = ParameterSpec.builder(stringClass, "value")
                .build();

        MethodSpec ensureNotNullMethod = MethodSpec.methodBuilder("ensureNotNull")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(valuePara)
                .returns(String.class)
                .addCode("        if (value == null) {\n" +
                        "            value = \"\";\n" +
                        "        }\n" +
                        "        return value;")
                .build();

        fileLoggerBuilder.addMethod(ensureNotNullMethod);


        MethodSpec formatMethod = MethodSpec.methodBuilder("format")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(levelPara)
                .addParameter(tagPara)
                .addParameter(msgPara)
                .returns(String.class)
                .addStatement("return String.format(\"%s %s/%s %s\\n\", getTime(\"MM-dd HH:mm:ss\"), getPriorityInfo(level),tag, msg);")
                .build();

        fileLoggerBuilder.addMethod(formatMethod);


        ParameterSpec timePatternPara = ParameterSpec.builder(stringClass, "timePattern")
                .build();

        MethodSpec getTimeMethod = MethodSpec.methodBuilder("getTime")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(timePatternPara)
                .returns(String.class)
                .addCode("        if (localFormat == null) {\n" +
                        "            localFormat = new SimpleDateFormat(timePattern, Locale.getDefault());\n" +
                        "        }\n" +
                        "        if (localDate == null) {\n" +
                        "            localDate = new Date();\n" +
                        "        }\n" +
                        "        localDate.setTime(System.currentTimeMillis());\n" +
                        "        return localFormat.format(localDate);")
                .build();

        fileLoggerBuilder.addMethod(getTimeMethod);

        ParameterSpec priorityPatternPara = ParameterSpec.builder(int.class, "priority")
                .build();

        MethodSpec getPriorityInfoMethod = MethodSpec.methodBuilder("getPriorityInfo")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(priorityPatternPara)
                .returns(String.class)
                .addCode("      switch (priority) {\n" +
                        "            case Log.VERBOSE:\n" +
                        "                return \"V\";\n" +
                        "            case Log.DEBUG:\n" +
                        "                return \"D\";\n" +
                        "            case Log.INFO:\n" +
                        "                return \"I\";\n" +
                        "            case Log.WARN:\n" +
                        "                return \"W\";\n" +
                        "            case Log.ERROR:\n" +
                        "                return \"E\";\n" +
                        "            case Log.ASSERT:\n" +
                        "                return \"A\";\n" +
                        "            default:\n" +
                        "                return \"\";\n" +
                        "        }")
                .build();

        fileLoggerBuilder.addMethod(getPriorityInfoMethod);

        MethodSpec initFileParamMethod = MethodSpec.methodBuilder("initFileParam")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addCode("    fileDir = file.getParentFile();\n" +
                        "        fileSuffix = \".txt\";\n" +
                        "        String name = file.getName();\n" +
                        "        if (!fileSuffix.isEmpty()) {\n" +
                        "            fileName = name.substring(0, name.indexOf(fileSuffix));\n" +
                        "        } else {\n" +
                        "            fileName = name;\n" +
                        "        }")
                .build();

        fileLoggerBuilder.addMethod(initFileParamMethod);

        MethodSpec initFileHandlerMethod = MethodSpec.methodBuilder("initFileHandler")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addCode("        HandlerThread thread = new HandlerThread(\"FileLogger\", android.os.Process.THREAD_PRIORITY_BACKGROUND);\n" +
                        "        thread.start();\n" +
                        "        fileHandler = new Handler(thread.getLooper(), new Handler.Callback() {\n" +
                        "            @Override\n" +
                        "            public boolean handleMessage(Message msg) {\n" +
                        "                if (msg.what == 1) {\n" +
                        "                    writeInternal((String) msg.obj);\n" +
                        "\n" +
                        "                }\n" +
                        "                return true;\n" +
                        "            }\n" +
                        "        });")
                .build();

        fileLoggerBuilder.addMethod(initFileHandlerMethod);



        ParameterSpec dataPara = ParameterSpec.builder(stringClass, "data")
                .build();

        MethodSpec writeInternalMethod = MethodSpec.methodBuilder("writeInternal")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(dataPara)
                .addCode("        if (data.length() < buffer.remaining()) {\n" +
                        "            buffer.put(data);\n" +
                        "        } else {// buffer剩余空间不足\n" +
                        "            int start = 0;\n" +
                        "            while (start < data.length()) {//循环检查的目的是防止data过大，分多次才能写完\n" +
                        "                int length = Math.min(data.length() - start, buffer.remaining());\n" +
                        "                buffer.put(data, start, start + length);\n" +
                        "                start = start + length;\n" +
                        "                if (buffer.remaining() == 0) { //buffer full\n" +
                        "                    writeToFile();\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }")
                .build();

        fileLoggerBuilder.addMethod(writeInternalMethod);


        MethodSpec writeToFileMethod = MethodSpec.methodBuilder("writeToFile")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addCode("      if (buffer.position() == 0) {//有数据时才写文件\n" +
                        "            return;\n" +
                        "        }\n" +
                        "        if (file.length() >= 1024 * 512L) {//文件超过最大限制时创建新文件\n" +
                        "            if (renameOldFile()) { //NOSONAR\n" +
                        "                createNewFile();\n" +
                        "            }\n" +
                        "        }\n" +
                        "        write(file, buffer.array(), 0, buffer.position());\n" +
                        "        buffer.clear();")
                .build();

        fileLoggerBuilder.addMethod(writeToFileMethod);


        MethodSpec renameOldFileMethod = MethodSpec.methodBuilder("renameOldFile")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .returns(boolean.class)
                .addStatement("File newFile = getNewFile();")
                .addStatement("return file.renameTo(newFile);")
                .build();

        fileLoggerBuilder.addMethod(renameOldFileMethod);


        MethodSpec createNewFileMethod = MethodSpec.methodBuilder("createNewFile")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addStatement("fileCount++;")
                .addStatement("file = getNewFile();")
                .build();

        fileLoggerBuilder.addMethod(createNewFileMethod);

        MethodSpec getNewFileMethod = MethodSpec.methodBuilder("getNewFile")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .returns(File.class)
                .addStatement("return new File(fileDir, getNewFileName());")
                .build();

        fileLoggerBuilder.addMethod(getNewFileMethod);

        MethodSpec getNewFileNameMethod = MethodSpec.methodBuilder("getNewFileName")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .returns(String.class)
                .addStatement("return fileName + fileCount + fileSuffix;")
                .build();

        fileLoggerBuilder.addMethod(getNewFileNameMethod);

        MethodSpec writeMethod = MethodSpec.methodBuilder("write")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(dataPara)
                .addStatement("Message.obtain(fileHandler, 1, data).sendToTarget();")
                .build();

        fileLoggerBuilder.addMethod(writeMethod);


        ParameterSpec filePara = ParameterSpec.builder(fileClass, "file")
                .build();

//        ClassName charArrayClass = ClassName.get("java.io", "File");

        ParameterSpec charPara = ParameterSpec.builder(char[].class, "data")
                .build();

        ParameterSpec offPara = ParameterSpec.builder(intClass, "off")
                .build();

        ParameterSpec lenPara = ParameterSpec.builder(intClass, "len")
                .build();

        MethodSpec writeXMethod = MethodSpec.methodBuilder("write")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .addParameter(filePara)
                .addParameter(charPara)
                .addParameter(offPara)
                .addParameter(lenPara)
                .addCode("      FileWriter writer = null;\n" +
                        "        try {\n" +
                        "            writer = new FileWriter(file, true);\n" +
                        "            writer.write(data, off, len);\n" +
                        "        } catch (IOException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        } finally {\n" +
                        "            if (writer != null) {\n" +
                        "                try {\n" +
                        "                    writer.close();\n" +
                        "                } catch (IOException e) {\n" +
                        "\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }")
                .build();

        fileLoggerBuilder.addMethod(writeXMethod);


        JavaFile file = JavaFile.builder("com.invensun.seven.log", fileLoggerBuilder.build()).build();

        try {
            file.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.seveninvensun.log.lib;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

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


//        ClassName override = ClassName.get("java.lang", "Override");
//
//        ClassName bundle = ClassName.get("android.os", "Bundle");
//
//        ClassName nullable = ClassName.get("android.support.annotation", "Nullable");
//
//        ParameterSpec savedInstanceState = ParameterSpec.builder(bundle, "savedInstanceState")
//                .addAnnotation(nullable)
//                .build();
//
//        MethodSpec onCreate = MethodSpec.methodBuilder("onCreate")
//                .addAnnotation(override)
//                .addModifiers(Modifier.PROTECTED)
//                .addParameter(savedInstanceState)
//                .addStatement("super.onCreate(savedInstanceState)")
//                .addStatement("setContentView(R.layout.activity_main)")
//                .build();
//
//        TypeSpec mainActivity = mainActivityBuilder.addMethod(onCreate)
//                .build();

        JavaFile file = JavaFile.builder("com.invensun.seven.log", fileLoggerBuilder.build()).build();

        try {
            file.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

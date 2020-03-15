package com.yline.annotation.compile;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 必须是 openJDK 才有 AbstractProcessor 类的相关代码
 *
 * @author yline 2020-03-15 -- 17:54
 */
@AutoService(Processor.class) // 自动为 JAVA Processor 生成 META-INF 信息
public class CompileAnnotationProcessor extends AbstractProcessor {
    /**
     * 初始化工作
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        // 用来对程序元素进行操作的实用工具方法
        Elements elementUtils = processingEnvironment.getElementUtils();

        // 在生成java文件时候使用。javapoet配合使用
        Filer filer = processingEnvironment.getFiler();

        // 用来对类型进行操作的实用工具方法。
        Types typeUtils = processingEnvironment.getTypeUtils();

        // 提供注释处理器用来报告错误消息、警告和其他通知的方式。可以传递元素、注释和注释值，以提供消息的位置提示。
        Messager messager = processingEnvironment.getMessager();

        // 对应getSupportedSourceVerrsion()
        SourceVersion sourceVersion = processingEnvironment.getSourceVersion();
    }

    /**
     * 指定当前注解器使用的Jdk版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        // @SupportedSourceVersion(SourceVersion.RELEASE_7) 效果一致
        return SourceVersion.latestSupported();
    }

    /**
     * 指出注解处理器 支持处理哪种注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // @SupportedAnnotationTypes({"com.yline.annotation.compile.CompileAnnotation"})
        Set<String> annotationSet = new LinkedHashSet<>(); // 保证先后顺序
        annotationSet.add(CompileAnnotation.class.getCanonicalName());
        return annotationSet;
    }

    /**
     * APT扫描到注解后调用方法
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 如果在以前的处理 round 中发生错误，则返回 true；否则返回 false
        // roundEnv.errorRaised();

        // 返回使用给定注释类型注释的元素
        // roundEnv.getElementsAnnotatedWith()
        System.out.println("xxx - process start");
        if (null != annotations && !annotations.isEmpty()) {
            processInner(annotations, roundEnv);
            return true;
        }
        return false;
    }

    /**
     * 处理注解逻辑，生成java文件
     * 1，遍历得到源码中，需要解析的元素列表
     * 2，输入 生成java文件
     */
    private void processInner(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // set of track
        Map<String, String> trackMap = new HashMap<>();
        // print on gradle console
        Messager messager = processingEnv.getMessager();

        // 遍历annotations获取annotation类型 @SupportedAnnotationTypes
        for (TypeElement te : annotations) {
            for (Element e : roundEnv.getElementsAnnotatedWith(te)) { // 获取所有被annotation标注的元素
                // 打印
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.toString());
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.getSimpleName());
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.getEnclosingElement().toString());
                // 获取注解
                CompileAnnotation annotation = e.getAnnotation(CompileAnnotation.class);
                // 获取名称
                String name = "".equals(annotation.value()) ? e.getSimpleName().toString() : annotation.value();

                // 保存映射信息
                trackMap.put(e.getSimpleName().toString(), name);
                messager.printMessage(Diagnostic.Kind.NOTE, "映射关系：" + e.getSimpleName().toString() + "-" + name);
            }
        }

        // 生成的包名
        final String packageName = "com.yline.generate";
        // 生成的类名
        final String className = "TrackManager";

        System.out.println("xxx - process write");

        // 创建Java文件
        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(className);
            buildJavaCode(fileObject, packageName, className, trackMap);
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.toString());
        }
    }

    private static void buildJavaCode(JavaFileObject fileObject, String packageName, String className,
                                      Map<String, String> trackMap) throws IOException {
        Writer writer = null;
        try {
            writer = fileObject.openWriter();
            PrintWriter printWriter = new PrintWriter(writer);

            printWriter.println("package " + packageName + ";\n");
            printWriter.println("import java.util.Map;");
            printWriter.println("/**");
            printWriter.println("* this file is auto-create by compiler,please don`t edit it");
            printWriter.println("* 页面路径映射关系表");
            printWriter.println("*/");
            {
                printWriter.println("public class " + className + " {");
                {
                    printWriter.println("    public void loadInto(Map<String, String> map) {");
                    Iterator<String> keys = trackMap.keySet().iterator();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = trackMap.get(key);
                        printWriter.println("        map.put(\"" + key + "\",\"" + value + "\");");
                    }
                    printWriter.println("    }");
                }
                printWriter.println("}");
            }
            printWriter.flush();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

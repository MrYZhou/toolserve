package com.lar.example;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 获取当前的文件路径
 */
public class FileRead {

    public static void main(String[] args) throws IOException {
        FileRead me = new FileRead();

        //class 获取方式 获取当前类文件所在路径
        String classPath = Objects.requireNonNull(me.getClass().getResource("")).getPath();
        File file = new File(classPath + "/1.json");
        String content = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(content);

    }
}

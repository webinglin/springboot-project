package com.meiya.springboot.utils;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConvertGradle2Maven {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("C:\\Users\\Aoc\\.gradle\\caches\\modules-2\\files-2.1\\");
        Files.walk(path).forEach(filePath -> {
            if (!Files.isDirectory(filePath)) {
                String paths = filePath.getParent().toString().replace("C:\\Users\\Aoc\\.gradle\\caches\\modules-2\\files-2.1\\", "D:\\MavenRepo\\");
                //.replace(".","\\");
                String[] pathsArray = paths.split("\\\\");
                //替换第二个为路径
                String packages = pathsArray[2];
                pathsArray[2] = packages.replace(".", "\\");
                StringBuffer stringBuffer = new StringBuffer();
                Arrays.stream(pathsArray).forEach(s -> {
                    if (s.length() < 30 || (s.contains("\\"))) {
                        stringBuffer.append(s).append("\\");
                    }
                });
                Path relPath = Paths.get(stringBuffer.toString());
                if (!Files.exists(relPath) || !Files.isDirectory(relPath)) {
                    try {
                        Files.createDirectories(relPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    FileUtils.copyFileToDirectory(filePath.toFile(), relPath.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(filePath.toString() + "===>" + relPath.toString());
            }
            //System.out.println(filePath);
        });
    }
}


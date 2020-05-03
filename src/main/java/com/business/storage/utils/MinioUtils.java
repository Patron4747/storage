package com.business.storage.utils;

/**
 * @author: mmustafin@context-it.ru
 * @created: 10.04.2020
 */
public class MinioUtils {

    public static String retrieveFileNameFromString(String path) {
        final int i = path.lastIndexOf("/");
        final String fileName = path.substring(i + 1);
        if (fileName.contains(".")) {
            return fileName;
        } else {
            return null;
        }
    }
}

package com.business.storage.cache;

import java.io.Serializable;

/**
 * @author: mmustafin@context-it.ru
 * @created: 16.04.2020
 */
public class GetFileCache implements Serializable {
    private byte[] bytes;
    private String contentType;
    private String fileName;

    public GetFileCache() {
    }

    public GetFileCache(byte[] bytes, String contentType, String fileName) {
        this.bytes = bytes;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

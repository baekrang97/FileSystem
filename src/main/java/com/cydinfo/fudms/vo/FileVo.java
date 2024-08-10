package com.cydinfo.fudms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {
    private int id;
    private String name;
    private String path;
    private long size;
    private String description;
    private String type;
    private int attachmentId;
    private String hash;
    private String mimeType;

    public FileVo(String name, String path, long size, String description, String type, String hash, String mimeType) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.description = description;
        this.type = type;
        this.hash = hash;
        this.mimeType = mimeType;
    }

}


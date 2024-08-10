package com.cydinfo.fudms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * TODO : 파일 다운로드 VO
 */
@Data
@AllArgsConstructor
public class FileDownloadVo {
    private final Long id;
    private final String name;
    private final String path;
    private final Long size;
    private final String type;
    private final String hash;
}

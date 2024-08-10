package com.cydinfo.fudms.dto;

import com.cydinfo.fudms.vo.FileDownloadVo;
import lombok.Getter;
import lombok.Setter;


/**
 * TODO : VO의 불변성을 보장하기 위한 파일 다운로드 DTO
 */
@Setter
@Getter
public class FileDownloadDto {

    private Long id;
    private String name;
    private String path;
    private Long size;
    private String type;
    private String hash;

    public FileDownloadDto() {
    }

    public FileDownloadDto(FileDownloadVo fileDownloadVo) {
        this.id = fileDownloadVo.getId();
        this.name = fileDownloadVo.getName();
        this.path = fileDownloadVo.getPath();
        this.size = fileDownloadVo.getSize();
        this.type = fileDownloadVo.getType();
        this.hash = fileDownloadVo.getHash();
    }

}

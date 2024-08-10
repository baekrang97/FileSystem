package com.cydinfo.fudms.controller;

import com.cydinfo.fudms.dto.ResponseDto;
import com.cydinfo.fudms.service.FileUploadService;
import com.cydinfo.fudms.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FileUploadApiController 클래스는 파일업로드를 위한 Controller입니다.
 */
@RestController
public class FileUploadApiController {

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadApiController(FileUploadService fileUploadService){
        this.fileUploadService = fileUploadService;
    }


    /**
     * 파일을 업로드하는 Client와 service를 연결해주는 메소드
     * @param metadataJson client에서 받아온 attachment와 file정보가 담긴 Json
     * @param files client에서 받아온 파일
     * @return ResponseDto<String>
     */
    @PostMapping("api/attachments")
    public ResponseDto<String> FileUpload(@RequestParam("metadata") String metadataJson, MultipartFile[] files) {

        // JSON으로 바로 받기 찾아보기

        // 파일 저장
        List<FileVo> fileVos = fileUploadService.saveFile(files, metadataJson);

        return new ResponseDto<String>(HttpStatus.OK.value(),"성공적으로 업로드가 되었습니다.");

    }

}

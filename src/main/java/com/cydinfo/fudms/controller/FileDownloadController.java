package com.cydinfo.fudms.controller;

import com.cydinfo.fudms.service.FileDownloadService;
import com.cydinfo.fudms.dto.FileDownloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 파일 다운로드 테스트용 컨트롤러
 */
@Controller
public class FileDownloadController {

    @Autowired
    private final FileDownloadService fileDownloadService;
    public FileDownloadController(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }


    /**
     * TODO : 파일 다운로드 테스트 페이지
     * FileDownloadDto에 모든 파일 정보를 가져와 download.HTML 매핑
     */
    @GetMapping("/download")
    public String FileDownloadIndex(Model model){
        List<FileDownloadDto> fileDownloadDto = fileDownloadService.getAllFileInfo();
        model.addAttribute("files",fileDownloadDto);
        return "download";
    }
}


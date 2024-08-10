package com.cydinfo.fudms.controller;

import com.cydinfo.fudms.dto.FileDownloadDto;
import com.cydinfo.fudms.service.FileDownloadService;
import com.cydinfo.fudms.util.FileUtils;
import com.cydinfo.fudms.vo.AttachmentVo;
import com.cydinfo.fudms.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

/**
 * 파일 다운로드 기능 컨트롤러
 */
@RestController
public class FileDownloadApiController {

    /**
     * file과 attachment 다운로드를 동시에 처리하기위해 두개의 서비스를 받아옴
     */
    @Autowired
    private final FileDownloadService fileDownloadService;
    @Autowired
    private final AttachmentService attachmentService;

    public FileDownloadApiController(FileDownloadService fileDownloadService, AttachmentService attachmentService) {
        this.fileDownloadService = fileDownloadService;
        this.attachmentService = attachmentService;
    }

    /**
     * TODO : 파일 다운로드 API
     * '../file/download?attachment=n&id=n&id=n+a...'으로 받아오면 attachment 값은 attachmentId에 id값들은 idList에 list로 매핑
     * 파일이 1개일 때는 해당 파일 단독으로 return
     * 파일이 복수개일 때는 attachment를 받아온 후 idList로 파일목록을 받아와 attachmentname을 압축파일명으로 압축하여 return
     */
    @ResponseBody
    @RequestMapping(value = "/file/download")
    public ResponseEntity<Resource> fileDownload(@RequestParam("attachment") Long attachmentId, @RequestParam("id") List<Integer> idList) throws IOException {
        // attachment ID와 요청에서 전달된 파일 ID 목록 처리
        Long fileId = 0L;
        AttachmentVo vo = attachmentService.getAttachment(attachmentId);
        String attName = vo.getName();
        if (idList.size() == 1) {
            List<FileDownloadDto> fileDownloadDto = fileDownloadService.selectFileInfo(Long.valueOf(idList.get(0)));
            return FileUtils.selectOneFile(fileDownloadDto);
        }
        for (Integer id : idList) {
            // 각 ID에 대한 파일을 다운로드하거나 처리하는 작업 수행
            fileId = Long.valueOf(id);
        }
        List selectIdList = new ArrayList();
        for (Integer i : idList) {
            Map<String, Object> idMap = new HashMap<>();
            idMap.put("id", i);
            selectIdList.add(idMap);
        }
        List<FileDownloadDto> fileDto = fileDownloadService.selectFilesInfo(selectIdList);
        return FileUtils.selectFiles(fileDto,attName);
    }

    /**
     * TODO : 첨부파일 다운로드 API
     *'../attachment/download?attachment=n'으로 받아오면 attachment 값을 attachmentId로 매핑시켜
     * 해당 attachment내 모든 파일을 압축후 attachmentname을 압축파일 명으로 return
     */
    @ResponseBody
    @RequestMapping(value = "/attachment/download")
    public ResponseEntity<Resource> attachmentDownload(@RequestParam("attachment") Long attachmentId) throws IOException {
        AttachmentVo vo = attachmentService.getAttachment(attachmentId);
        return FileUtils.selectFiles(fileDownloadService.selectAttachmentInfo((long) vo.getId()),vo.getName());
    }
}

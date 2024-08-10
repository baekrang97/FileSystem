package com.cydinfo.fudms.controller;

import com.cydinfo.fudms.service.FileService;
import com.cydinfo.fudms.vo.AttachmentVo;
import com.cydinfo.fudms.vo.FileVo;
import com.cydinfo.fudms.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttachmentRestController {

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    FileService fileService;

    /**
     * 존재하는 모든 attachment의 전체 리스트
     */
    @GetMapping("api/attachments")
    public List<AttachmentVo> attachments() {
        return attachmentService.getAttachments();
    }

    /**
     * attachment id를 받아서 attachment 한개의 상세 detail
     */
    @GetMapping("api/attachments/detail/{id}")
    public AttachmentVo attachmentsDetail(@PathVariable Long id) {
        return attachmentService.getAttachment(id);
    }

    /**
     * attachment id를 받아서 해당 attachment에 속하는 파일리스트
     */
    @GetMapping("api/files/{id}")
    public List<FileVo> files(@PathVariable int id) {
        return fileService.getFilesByAttachmentId(id);
    }

}

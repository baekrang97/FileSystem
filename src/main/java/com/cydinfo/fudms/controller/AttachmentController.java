package com.cydinfo.fudms.controller;

import com.cydinfo.fudms.service.AttachmentService;
import com.cydinfo.fudms.vo.AttachmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    /**
     * 파일 리스트 페이지
     *  pageNum = 페이징하고 기본 페이지값 ex) 1페이지부터 시작
     *  pageSize = 한개의 페이지에 들어갈 개수 ex) 1개의페이지에 6개의 목록이 존재
     *  pageNumbers = 한개의 페이지에 몇개의 페이징목록이 들어갈지 선택 ex) 1,2,3,4,5 // 3,4,5,6,7..
     */
    @GetMapping("attachmentlist")
    public String attachmentList(Model model, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize) {
        List<AttachmentVo> attachments = attachmentService.getPagedData(pageNum, pageSize);
        model.addAttribute("attachments", attachments);

        model.addAttribute("pageNum", pageNum);
        int totalCount = attachmentService.getTotalCount();
        int totalPages = attachmentService.calculateTotalPages(totalCount, pageSize);
        List<Integer> pageNumbers = attachmentService.getPageNumbers(pageNum, totalPages, 5);

        model.addAttribute("pageNumbers", pageNumbers);

        return "attachment-list";
    }

    /**
     * 파일 업로드 페이지
     */
    @GetMapping("/attachments")
    public String fileUploadIndex(){
        return "attachment-upload";
    }


}

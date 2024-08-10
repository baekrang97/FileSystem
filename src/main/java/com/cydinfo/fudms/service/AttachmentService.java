package com.cydinfo.fudms.service;

import com.cydinfo.fudms.vo.AttachmentVo;
import com.cydinfo.fudms.dao.AttachmentDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentService {

    private final AttachmentDao attachmentDao;

    public AttachmentService(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    /**
     * attachment의 전체 리스트를 가져오기위한 서비스
     */
    public List<AttachmentVo> getAttachments() {
        return attachmentDao.selectList("AttachmentMapper.selectAllAttachments");
    }

    /**
     * 한개의 attachment 의 정보 가져오는 서비스
     */
    public AttachmentVo getAttachment(Long id) {
        return attachmentDao.selectOne("AttachmentMapper.selectAttachmentById", Math.toIntExact(id));
    }

    /**
     * 페이징을위한 서비스
     */
    public int getTotalCount() {
        return attachmentDao.getTotalCount("AttachmentMapper.getTotalCount");
    }

    public List<AttachmentVo> getPagedData(int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        return attachmentDao.getPagedData("AttachmentMapper.getPagedData",start, pageSize);
    }

    /**
     * 페이징 을 위한 계산 서비스
     * 페이지의 총 개수 계산등
     */
    public int calculateTotalPages(int start, int pageSize) {
        return (int) Math.ceil((double) start / pageSize);
    }

    public List<Integer> generatePageNumbers(int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }
    public List<Integer> getPageNumbers(int currentPage, int totalPages, int groupSize) {
        List<Integer> pageNumbers = new ArrayList<>();
        int startPage = Math.max(currentPage - (groupSize / 2), 1);
        int endPage = Math.min(startPage + groupSize - 1, totalPages);
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }
}

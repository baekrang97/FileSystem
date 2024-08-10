package com.cydinfo.fudms.service;

import com.cydinfo.fudms.dao.FileDao;
import com.cydinfo.fudms.vo.FileVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileDao fileDao;

    public FileService(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    /**
     * attachment id를 받아서 해당 attachment에 속하는 파일리스트
     */
    public List<FileVo> getFilesByAttachmentId(int id) {
        return fileDao.selectList("FileMapper.selectFilesByAttachmentId",id);
    }
}

package com.cydinfo.fudms.service;

import com.cydinfo.fudms.dao.FileDao;
import com.cydinfo.fudms.dto.FileDownloadDto;
import com.cydinfo.fudms.vo.FileDownloadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileDownloadService {

    @Autowired
    private final FileDao dao;

    public FileDownloadService(FileDao dao) {
        this.dao = dao;
    }

    public FileDownloadDto convertFileDto(FileDownloadVo vo) {
        return new FileDownloadDto(vo);
    }

    public List<FileDownloadDto> getAllFileInfo(){
        List<FileDownloadVo> files = dao.selectFileData("DownloadFileMapper.selectAllInfo");
        return files.stream()
                .map(this::convertFileDto)
                .collect(Collectors.toList());
    }

    public List<FileDownloadDto> selectFileInfo(Long id) {
        List<FileDownloadVo> files = dao.selectFileInfo("DownloadFileMapper.selectIdInfo", id);
        return files.stream()
                .map(this::convertFileDto)
                .collect(Collectors.toList());
    }


    public List<FileDownloadDto> selectFilesInfo(List list) {
        List<FileDownloadVo> files = dao.selectFileInfo("DownloadFileMapper.selectBatchIdInfo", list);
        System.out.println("성공");
        return files.stream()
                .map(this::convertFileDto)
                .collect(Collectors.toList());
    }

    public List<FileDownloadDto> selectAttachmentInfo(Long id) {
        List<FileDownloadVo> files = dao.selectFileInfo("DownloadFileMapper.selectAttachmentInfo", id);
        System.out.println("성공");
        return files.stream()
                .map(this::convertFileDto)
                .collect(Collectors.toList());
    }
}

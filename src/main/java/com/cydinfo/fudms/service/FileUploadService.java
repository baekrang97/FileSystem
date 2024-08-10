package com.cydinfo.fudms.service;

import com.cydinfo.fudms.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cydinfo.fudms.dao.FileUploadDao;
import com.cydinfo.fudms.vo.AttachmentVo;
import com.cydinfo.fudms.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.*;

/**
 * FileUploadService 클래스는 파일 업로드 기능을 제공합니다.
 *
 */
@Service
public class FileUploadService {

    @Autowired
    private FileUploadDao fileUploadDao;

    private FileUtils fileUtils;

    @Autowired
    public FileUploadService(FileUtils fileUtils){
        this.fileUtils = fileUtils;
    }

    /**
     *
     * @param files 업로드할 파일
     * @param metadataJson attachment와 파일 상세 정보를 갖고 있는 json
     * @return List<FileVo>
     */
    public List<FileVo> saveFile(MultipartFile[] files, String metadataJson) {

        // Json 데이터를 Map<>으로 파싱
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> map = gson.fromJson(metadataJson, mapType);

        if (map.get("systemId") == null) {
            throw new IllegalArgumentException("시스템명을 확인해주세요.");
        }
        if (map.get("userName") == null) {
            throw new IllegalArgumentException("사용자를 확인해주세요.");
        }

        int systemId = Integer.parseInt(map.get("systemId").toString());
        String userName = map.get("userName").toString();

        // 파일의 Description 추출
        List<String> fileDescriptions = (List<String>) map.get("fileDescriptions");

        /**
         * attachmentVo 생성
         */
        AttachmentVo attachmentVo = AttachmentVo.builder()
                        .name(map.get("attachmentName").toString())
                        .description(map.get("attachmentDescription").toString())
                        .hashTag("test, test2, test3")
                        .userName(userName)
                        .systemId(systemId).build();

        /**
         * 파일 업로드
         */
        List<FileVo> fileVos = fileUtils.uploadFile(files, fileDescriptions, map.get("attachmentName").toString());

        /**
         * attachment를 insert하고 그 id를 반환
         */
        attachmentVo = fileUploadDao.insertAttachment(attachmentVo);

        /**
         * fileVo의 외래키 attachmentId 주입
         */
        for(FileVo fileVo : fileVos){
            fileVo.setAttachmentId(attachmentVo.getId());
        }

        /**
         * FILE MetaData insert
         */
        fileUploadDao.insertFiles(fileVos);

        return fileVos;
    }

}

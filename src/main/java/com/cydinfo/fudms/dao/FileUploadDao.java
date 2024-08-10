package com.cydinfo.fudms.dao;


import com.cydinfo.fudms.vo.AttachmentVo;
import com.cydinfo.fudms.vo.FileVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileUploadDao는 파일 업로드를 위한 DB access object 입니다.
 * attachment와 file의 생성하는 기능을 갖습니다.
 */
@Repository
public class FileUploadDao {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public FileUploadDao(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * attachment를 생성하는 메서드
     * @param attachmentVo
     * @return attachmentVo 생성된 attachment의 id를 갖고 반환합니다.
     */
    public AttachmentVo insertAttachment(AttachmentVo attachmentVo){
        this.sqlSessionTemplate.insert("FileUploadMapper.insertAttachment", attachmentVo);
        return attachmentVo;
    }

    /**
     * attachment id를 참조하여 파일을 생성하는 메서드
     * @param fileVos 파일들의 리스트
     * @return int 생성된 행수 반환
     */
    public int insertFiles(List<FileVo> fileVos){
        return this.sqlSessionTemplate.insert("FileUploadMapper.insertFiles", fileVos);
    }
}




package com.cydinfo.fudms.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/*
 * TODO : 파일 다운로드 dao
 * 현재는 파일 다운로드와 업로드 dao가 분리되어 있지만 이후 통합한 filedao 한 개만 사용하는 것을 고려
 */
@Repository
public class FileDao {

    @Autowired
    private final SqlSessionTemplate sqlSessionTemplate;

    public FileDao(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public <E> List<E> selectFileData(String statementName) {
        return sqlSessionTemplate.selectList(statementName);
    }

    public <E> List<E> selectFileInfo(String statementName, Object parameterObejct) {
        return sqlSessionTemplate.selectList(statementName, parameterObejct);
    }

    public <E> List<E> selectList(String statementName, int id) {
        return this.sqlSessionTemplate.selectList(statementName,id);
    }
}

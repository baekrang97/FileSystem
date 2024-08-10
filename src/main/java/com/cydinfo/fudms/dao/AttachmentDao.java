package com.cydinfo.fudms.dao;

import com.cydinfo.fudms.vo.AttachmentVo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AttachmentDao {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public AttachmentDao(SqlSessionTemplate sqlSessionTemplate, SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    // attachment의 전체 리스트를 가져오기위한 dao
    public List<AttachmentVo> selectList(String statementName) {
        return this.sqlSessionTemplate.selectList(statementName);
    }
    // 한개의 attachment 의 정보 가져오는 dao
    public AttachmentVo selectOne(String statementName, int id) {
        return this.sqlSessionTemplate.selectOne(statementName, id);
    }


    /*
    페이징을 위한 dao처리

    * 전체 attachment의 개수와 세팅한 page의 리스트 가져오기.
    */

    public int getTotalCount(String statementName) {
        return sqlSessionTemplate.selectOne(statementName);
    }

    public List<AttachmentVo> getPagedData(String statementName, int pageNum, int pageSize) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", pageNum);
        params.put("pageSize", pageSize);
        return sqlSessionTemplate.selectList(statementName, params);
    }



}

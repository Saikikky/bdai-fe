package com.bdai.fe.service;

import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.dto.SchemaDtoList;
import com.bdai.fe.entity.Schema;

import java.util.List;

public interface SchemaService {

    // 保存预处理流程数据（flow表和schema表）
    boolean schemaSave(Schema schema);

    // 根据name查询id
    Integer getIdByName(String name);

    // 更新schema表
    boolean schemaUpdate(Integer id, String name, Integer device_type);

    // 判断schema数据是否解析正确
    ResultCode isJSONCorrect(String name, String device_type);

    SchemaDtoList getSchemaList();

    SchemaDtoList getSchemaById(Integer id);

    ResultCode deleSchemaById(Integer id);

}

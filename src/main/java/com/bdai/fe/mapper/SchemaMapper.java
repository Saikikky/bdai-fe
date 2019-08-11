package com.bdai.fe.mapper;

import com.bdai.fe.entity.Schema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SchemaMapper {

    // 保存至schema表中
    boolean schemaSave(@Param("name") String name, @Param("device_type") Integer device_type,@Param("create_by") Integer create_by);

    // 根据name查id
    Integer getIdByName(String name);

    boolean schemaUpdate(@Param("id") Integer id, @Param("name") String name, @Param("device_type") Integer device_type);

    // 查询所有的记录（没有参数）test
    List<Schema> getSchema();

    //根据id查询记录
    Schema getSchemaById( Integer id);

    Integer deleSchemaById(Integer id);

    Integer getDeviceTypeBySchemaId(int schema_id);
}


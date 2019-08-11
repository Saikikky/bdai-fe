package com.bdai.fe.service.Impl;

import com.bdai.fe.dto.FlowDto;
import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.dto.SchemaDto;
import com.bdai.fe.dto.SchemaDtoList;
import com.bdai.fe.entity.Flow;
import com.bdai.fe.entity.Schema;
import com.bdai.fe.mapper.FlowMapper;
import com.bdai.fe.mapper.SchemaMapper;
import com.bdai.fe.service.SchemaService;
import com.bdai.fe.util.Obj2byte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchemaServiceImpl implements SchemaService {

    @Autowired
    SchemaMapper schemaMapper;

    @Autowired
    FlowMapper flowMapper;

    //二进制数组转换为对象类型，提取为方法
    public Object bytes2Obj (byte[] flowBytes){
            try {
                Object paramsObj = Obj2byte.byte2obj( flowBytes);
                return paramsObj;
            } catch (Exception e) {
                System.out.println("数据库二进制转换出问题");
                e.printStackTrace();
            }return null;
    }
    //查找所有schema中的数据
    @Override
    public SchemaDtoList getSchemaList() {
        SchemaDtoList sdl = new SchemaDtoList();

        List<Schema> schemaList = schemaMapper.getSchema();
        ArrayList<SchemaDto> sdList = new ArrayList<>();
        for (Schema sche : schemaList){//给每个Schema表中的字段加上对应id中Flow的值;
            List<Flow> flowBySidList = flowMapper.getFlowBySid(sche.getId());
            List<FlowDto> fdList = new ArrayList<>();//存放FlowDto的数组
            for(Flow flow : flowBySidList){
                FlowDto fd = new FlowDto(flow.getId(),flow.getIndex(),flow.getMethod(),bytes2Obj(flow.getParams()));
                fdList.add(fd);//所有FlowDto对象添加进FlowDto列表里
            }
            //把schema字段中的属性添加进SchemaDto里
            SchemaDto sd = new SchemaDto(sche.getId(),sche.getName(),sche.getDevice_type(),sche.getCreate_by(),fdList);
            sdList.add(sd);//所有SchemaDto对象添加进SchemaDto列表里
        }
        sdl.setCode(0);
        sdl.setSchemas(sdList);
        return sdl;
    }

    //根据shcema的id查找schema的数据
    @Override
    public SchemaDtoList getSchemaById(Integer id) {
        SchemaDtoList sdl = new SchemaDtoList();
        Schema schemaById = schemaMapper.getSchemaById(id);//查找出对应id的Schema表
        List<Flow> flowBySid = flowMapper.getFlowBySid(id);//查找出所有外键对应Schema的Flow表
        if (schemaById==null){
            return new SchemaDtoList(30202,null);//找不到对应id->查不到对应id的schema里面的值
        }
        try{
            ArrayList<SchemaDto> sdList = new ArrayList<>();//存放ShemaDto的数组
            List<FlowDto> fdList = new ArrayList<>();//存放FlowDto的数组

            for(Flow flow : flowBySid) {
                FlowDto fd = new FlowDto(flow.getId(),flow.getIndex(),flow.getMethod(),bytes2Obj(flow.getParams()));
                fdList.add(fd);//所有FlowDto对象添加进FlowDto列表里
            }
            SchemaDto sd = new SchemaDto(schemaById.getId(),schemaById.getName(),schemaById.getDevice_type(),schemaById.getCreate_by(),fdList);
            sdList.add(sd);//所有SchemaDto对象添加进SchemaDto列表里

            sdl.setCode(0);
            sdl.setSchemas(sdList);
            return sdl;
        }catch (Exception e){
            return new SchemaDtoList(30200,null);//内部未知错误->抛出异常了
        }
    }

    //根据id删除schema中的值
    @Override
    public ResultCode deleSchemaById(Integer id) {
        Integer deleteId = schemaMapper.deleSchemaById(id);
        if (deleteId==1){
            return new ResultCode(0);//删除成功
        }else if (deleteId==0) {
            return new ResultCode(30402);//id没找到
        }
            return new ResultCode(30400);//内部未知错误

    }

    @Override
    public boolean schemaSave(Schema schema) {
        return schemaMapper.schemaSave(schema.getName(), schema.getDevice_type(), schema.getCreate_by());
    }

    @Override
    public Integer getIdByName(String name) {
        return schemaMapper.getIdByName(name);
    }

    @Override
    public boolean schemaUpdate(Integer id, String name, Integer device_type) {
        return schemaMapper.schemaUpdate(id, name, device_type);
    }

    @Override
    public ResultCode isJSONCorrect(String name, String device_type) {
        // 参数为空
        if ("".equals(name) || "".equals(device_type)) {
            // 返回1为参数未知错误 去接口方法中返回特定的code
            return new ResultCode(1);
        }
        //  device_type不为数字
        for (int i = 0; i < device_type.length(); i++) {
            if (!Character.isDigit(device_type.charAt(i))) {
                return new ResultCode(1);
            }
        }
        // 参数正确
        return new ResultCode(0);
    }
}

package com.bdai.fe.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.dto.SchemaDtoList;
import com.bdai.fe.entity.Flow;
import com.bdai.fe.entity.Schema;
import com.bdai.fe.service.FlowService;
import com.bdai.fe.service.PrivilegeService;
import com.bdai.fe.service.SchemaService;
import com.bdai.fe.util.Obj2byte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/preparation/v1")
@RestController
public class SchemaController {

    @Autowired
    SchemaService schemaService;

    @Autowired
    FlowService flowService;

    @Autowired(required = true)
    PrivilegeService privilegeService;


    @GetMapping("/schema")
    public SchemaDtoList getSchema(HttpServletRequest request) {//查询所有schema处理流程
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new SchemaDtoList(30201, null);
        }

        SchemaDtoList schema = schemaService.getSchemaList();
        System.out.println(schema);
        return schema;

    }

    @GetMapping("/schema/{id}")
    public SchemaDtoList getSchemaById(@PathVariable("id") Integer id, HttpServletRequest request) {//查询单个schema处理流程的过程
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new SchemaDtoList(30201, null);
        }
        SchemaDtoList schemaById = schemaService.getSchemaById(id);
        System.out.println(schemaById);
        return schemaById;
    }

    @DeleteMapping("/schema/{id}")
    public ResultCode deleteSchemaById (@PathVariable("id") Integer id, HttpServletRequest request){
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new ResultCode(30401);
        }

        ResultCode delCode = schemaService.deleSchemaById(id);
        return delCode;
    }

    // 新增
    @PutMapping("/schema")
    @ResponseBody
    public ResultCode schemaSave(@RequestBody JSONObject body, HttpServletRequest request){
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new ResultCode(30101);
        }

        try {
            // 解析json数据
            String name = body.getString("name"); //预处理名称
            String deviceType = body.getString("device_type"); // 设备类型
            ResultCode code = schemaService.isJSONCorrect(name, deviceType);
            if (code.getCode() == 1) {
                return new ResultCode(30102);
            }

            Integer device_type = Integer.parseInt(deviceType);
            Integer userId = Integer.parseInt(username);
            Schema schema = new Schema(name, device_type, userId);

            // 保存
            schemaService.schemaSave(schema);

            // 查schemaId
            Integer sid = schemaService.getIdByName(name);

            List<Flow> flows = new ArrayList<>();
            JSONArray jsonArray = body.getJSONArray("flow");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject  =  jsonArray.getJSONObject(i);

                String inx = jsonObject.getString("index");
                String method = jsonObject.getString("method");
                // 参数列表
                Object p = jsonObject.get("params");

                code = flowService.isJSONCorrect(inx, method, p);
                if (code.getCode() == 1) {
                    return new ResultCode(30102);
                }

                Integer index = Integer.parseInt(inx);
                // 二进制化
                byte[] params = Obj2byte.obj2byte(p);

                Flow flow = new Flow(sid, index, method, params);
                flows.add(flow);
            }
            for (int i = 0; i < flows.size(); i++ ) {
                flowService.flowSave(flows.get(i));
            }
        } catch (Exception e) {
            return new ResultCode(30100);
        }

        return new ResultCode(0);
    }


    // 修改
    @PostMapping("/schema")
    @ResponseBody
    public ResultCode schemaUpdate(@RequestBody JSONObject body, HttpServletRequest request){
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new ResultCode(30301);
        }
        try {
            // 获取schemaId用于修改，获取id或则ID的值
            String s = body.getString("ID") == null?body.getString("id"):body.getString("ID");
            Integer sId = Integer.parseInt(s);

            String name = body.getString("name");
            String deviceType = body.getString("device_type");

            ResultCode code = schemaService.isJSONCorrect(name, deviceType);
            if (code.getCode() == 1) {
                return new ResultCode(30302);
            }

            Integer device_type = Integer.parseInt(deviceType);
            schemaService.schemaUpdate(sId, name, device_type);

            JSONArray jsonArray = body.getJSONArray("flow");
            List<Flow> flows = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 用于查找
                String id = jsonObject.getString("fid");
                String method = jsonObject.getString("method");
                String inx = jsonObject.getString("index");
                Object p = jsonObject.get("params");
                code = flowService.isJSONCorrect(inx, method, p);
                if (code.getCode() == 1) {
                    return new ResultCode(30302);
                }

                Integer fId = Integer.parseInt(id);
                byte[] params = Obj2byte.obj2byte(p);

                flowService.flowUpdate(fId, method, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultCode(30300);
        }
        return new ResultCode(0);
    }
}

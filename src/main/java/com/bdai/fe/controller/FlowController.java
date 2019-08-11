package com.bdai.fe.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.entity.Flow;
import com.bdai.fe.mapper.FlowMapper;
import com.bdai.fe.service.FlowService;
import com.bdai.fe.service.PrivilegeService;
import com.bdai.fe.util.Obj2byte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/preparation/v1")
@RestController
public class FlowController {

    @Autowired
    FlowService flowService;

    @Autowired(required = true)
    PrivilegeService privilegeService;

    // 修改
    @PostMapping("/flow")
    @ResponseBody
    public ResultCode flowUpdate(@RequestBody JSONObject body, HttpServletRequest request){
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new ResultCode(30301);
        }
        try {
            // 获取flowId用于修改
            String f = body.getString("fid");
            Integer fId = Integer.parseInt(f);

            String method = body.getString("method");
            String inx = body.getString("index");
            Object p = body.get("params");

            ResultCode code = flowService.isJSONCorrect(inx, method, p);
            if (code.getCode() == 1) {
                return new ResultCode(30302);
            }

            byte[] params = Obj2byte.obj2byte(p);

            flowService.flowUpdate(fId, method, params);
        } catch (Exception e) {
            return new ResultCode(30300);
        }
        return new ResultCode(0);
    }


    @DeleteMapping("/flow/{id}")
    public ResultCode deleteFlowId (@PathVariable("id") Integer id, HttpServletRequest request){
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new ResultCode(30401);
        }

        ResultCode delCode = flowService.deleFlowById(id);
        return delCode;
    }
}

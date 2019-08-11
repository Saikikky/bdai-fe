package com.bdai.fe.service.Impl;

import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.entity.Flow;
import com.bdai.fe.mapper.FlowMapper;
import com.bdai.fe.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowServiceImpl implements FlowService {

    @Autowired
    FlowMapper flowMapper;

    @Override
    public boolean flowSave(Flow flow) {
        return flowMapper.flowSave(flow.getSid(), flow.getIndex(), flow.getMethod(), flow.getParams());
    }

    @Override
    public boolean flowUpdate(Integer id, String method, byte[] params) {
        return flowMapper.flowUpdate(id, method, params);
    }

    @Override
    public ResultCode deleFlowById(Integer id) {
        Integer deleteId = flowMapper.deleFlowById(id);
        if (deleteId==1){
            return new ResultCode(0);//删除成功
        }else if (deleteId==0) {
            return new ResultCode(30402);//id没找到
        }
        return new ResultCode(30400);//内部未知错误

    }

    @Override
    public ResultCode isJSONCorrect(String index, String method, Object params) {
        if ("".equals(index) || "".equals(method) || params.equals(null)) {
            return new ResultCode(30102);
        }
        for (int i = 0; i < index.length(); i++) {
            if (!Character.isDigit(index.charAt(i))) {
                return new ResultCode(30102);
            }
        }
        return new ResultCode(0);
    }
}

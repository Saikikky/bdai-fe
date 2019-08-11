package com.bdai.fe.service;

import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.entity.Flow;

public interface FlowService {

    //  保存至flow表
    boolean flowSave(Flow flow);

    // flow表更新
    boolean flowUpdate(Integer id, String method, byte[] params);

    // 删除flow
    ResultCode deleFlowById(Integer id);

    // 判断flow表数据解析是否正确
    ResultCode isJSONCorrect(String index, String method, Object params);
}

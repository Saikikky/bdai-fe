package com.bdai.fe.util;

import com.alibaba.fastjson.JSONObject;
import com.bdai.fe.service.JobService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class TestR {
    private final Log logger = LogFactory.getLog(getClass());

    JobService jobService;
    @PutMapping("/stream")
    public Map<String,Object> jobStreamStart(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String ip = jsonObject.getString("ip");
        String port = jsonObject.getString("port");
        String topic = jsonObject.getString("topic");
        String pre = jsonObject.getString("pre");
        String cmd = ip + " " + port + " " + topic + " "+ pre;
        logger.debug("传入参数："+cmd);
        RemoteShellExecutor executor = new RemoteShellExecutor("115.156.128.241", "root", "root");
        Map<String, Object> result = new HashMap<>();
        try {
            executor.exec("source /etc/profile\n nohup python3 /py_code/preprocessing.py "+cmd+"&  \necho $!");
        } catch (Exception e) {
            logger.debug("!!!!!!!!数据流脚本启动失败!!!!!!!!!!!!");
            result.put("code",30802);
            e.printStackTrace();
        }
        String pid = executor.getPid();
        jobService.jobStreamSave(ip,port,topic,pid);
        result.put("pid",pid);
        result.put("code",0);
        return result;
    }
    /*public static void main(String[] args)throws Exception {
//        RemoteShellExecutor executor = new RemoteShellExecutor("115.156.128.241", "root", "root");
//        executor.exec("nohup hive                  &        \necho $!");

    }*/
}

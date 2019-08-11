package com.bdai.fe.util;


import com.bdai.fe.mapper.JobMapper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class JobLazyExecute implements Runnable {
    private JobMapper jobMapper;//这里网上说用Autowired的对象的话要新建对象https://www.cnblogs.com/jasenin/p/7234672.html
    private RemoteShellExecutor executor;
    private String commad;
    private Integer jobId;



    private final Log logger = LogFactory.getLog(getClass());

    public JobLazyExecute() {
    }

    public JobLazyExecute(JobMapper jobMapper, RemoteShellExecutor executor, String commad, Integer jobId) {
        this.jobMapper = jobMapper;
        this.executor = executor;
        this.commad = commad;
        this.jobId = jobId;
    }

    @Override
    public void run() {
        try {
            executor.exec(commad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!==============!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("python脚本执行失败");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!==============!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            logger.debug("python脚本执行失败");
            logger.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            jobMapper.updateStatus(2,jobId);//执行失败
        }
    }
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getCommad() {
        return commad;
    }

    public void setCommad(String commad) {
        this.commad = commad;
    }

    public RemoteShellExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(RemoteShellExecutor executor) {
        this.executor = executor;
    }

    public JobMapper getJobMapper() {
        return jobMapper;
    }

    public void setJobMapper(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }
}

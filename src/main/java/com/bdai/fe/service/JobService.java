package com.bdai.fe.service;
import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.dto.JobDto;
import com.bdai.fe.entity.Job;

public interface JobService {

    JobDto getJobs();

    JobDto getJobs(Integer id);

	ResultCode deleJobById(Integer id);

	//返回插入job的id
	Integer jobSave (Integer schemaId, Integer userId, String dataSet, String devices );

	boolean jobStreamSave(String ip, String port, String topic, String pid);
}

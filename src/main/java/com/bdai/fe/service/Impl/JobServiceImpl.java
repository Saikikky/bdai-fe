package com.bdai.fe.service.Impl;

import com.bdai.fe.dto.JobDto;
import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.entity.Job;
import com.bdai.fe.entity.JobDevices;
import com.bdai.fe.entity.JobStream;
import com.bdai.fe.mapper.JobDevicesMapper;
import com.bdai.fe.mapper.JobMapper;
import com.bdai.fe.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobMapper jobMapper;
    @Autowired
    JobDevicesMapper jobDevicesMapper;


    @Override
    public ResultCode deleJobById(Integer id) {
        Integer deleteId = jobMapper.delJobById(id);
        if (deleteId==1){
            return new ResultCode(0);//删除成功
        }else if (deleteId==0) {
            return new ResultCode(30702);//id没找到
        }
        return new ResultCode(30700);//内部未知错误
    }

    @Override
    public Integer jobSave(Integer schemaId, Integer userId, String dataSet, String devices ) {
        Job job = new Job();
        job.setSid(schemaId);
        job.setExcutor_id(userId);
        job.setDataset_name(dataSet);
        System.out.println("=======job的信息=======");
        System.out.println(job.toString());
        jobMapper.jobSave(job);
        System.out.println("========job的信息和job的id============");
        System.out.println(job.toString()+"---------"+ job.getId());
        String[] deviceToTable = devices.split(",");
        for (String deviceId : deviceToTable) {
            JobDevices jobDevices = new JobDevices();
            jobDevices.setDevice_id(Integer.parseInt(deviceId.trim()));
            jobDevices.setJob_id(job.getId());
            System.out.println("=======jobDevice的信息=======");
            System.out.println(jobDevices.toString());
            jobDevicesMapper.jobDeviceSave(jobDevices);
        }
        //jobMapper.jobSave(job);
        return job.getId();
    }


    @Override
    public JobDto getJobs() {
        try {
            JobDto jobDto = new JobDto();

            ArrayList<JobDto.Jobmsg> jobmsgList = new ArrayList<>();//保存JobDto中jobs的列表
            List<Job> jobsList = new ArrayList<>();//保存数据库中jobs查询出的元组列表
            jobsList = jobMapper.getJobs();
            System.out.println(jobsList);//打印jobs
            //遍历每个job元组
            //id没找到
            if (jobsList == null) {
                return new JobDto(30602, null);
            }
            for (Job jobs : jobsList) {
                List<Integer> deviceIds = (ArrayList) jobDevicesMapper.getDeviceIdsByJobID(jobs.getId());
                //创建一个JobDto的内部类对象
                JobDto.Jobmsg jobmsg = jobDto.new Jobmsg(jobs.getId(), jobs.getSid()/*schema就是sid*/, jobs.getProgress(), jobs.getDataset_name(), jobs.getExcutor_id(), deviceIds);
                jobmsgList.add(jobmsg);
            }
            jobDto.setCode(0);
            jobDto.setJobs(jobmsgList);
            return jobDto;
        }catch (Exception e ){
            return new JobDto(30600,null);
        }
    }

    @Override
    public JobDto getJobs(Integer id) {
        try {
            JobDto jobDto = new JobDto();
            List<Integer> deviceIds = (ArrayList) jobDevicesMapper.getDeviceIdsByJobID(id);

            //存放job数据，devices要查
            Job jobList = new Job();
            ArrayList<JobDto.Jobmsg> jobmsgList = new ArrayList<>();
            jobList = jobMapper.getJobById(id);

            //id没找到
            if (jobList == null) {
                return new JobDto(30602, null);
            }
            //创建一个JobDto的内部类对象
            JobDto.Jobmsg jobmsg = jobDto.new Jobmsg(jobList.getId(), jobList.getSid()/*schema就是sid*/, jobList.getProgress(), jobList.getDataset_name(), jobList.getExcutor_id(), deviceIds);
            jobmsgList.add(jobmsg);
            jobDto.setCode(0);
            jobDto.setJobs(jobmsgList);
            return jobDto;
        }catch (Exception e ){
            return new JobDto(30600,null);
        }
    }

    @Override
    public boolean jobStreamSave(String ip, String port, String topic, String pid) {
        JobStream jobStream = new JobStream(ip, port, topic, pid);
        return jobMapper.jobStreamSave(jobStream);
    }

}

package com.bdai.fe.mapper;

import com.bdai.fe.entity.Job;
import com.bdai.fe.entity.JobStream;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobMapper {
    List<Job> getJobs();

    Job getJobById(Integer id);

	Integer delJobById(Integer id);

    void updateStatus(@Param("code") int code, int jobId);

    //void jobSave(int schema_id,int excutor_id,String dataset_name,Boolean is_finished);
    void jobSave(Job job);

    boolean jobStreamSave(JobStream jobStream);
    Integer getJobIdBySchemaId (Integer schema_id);//怎么返回job的id？
}

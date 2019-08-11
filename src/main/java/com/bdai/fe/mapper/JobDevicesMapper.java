package com.bdai.fe.mapper;

import com.bdai.fe.entity.JobDevices;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobDevicesMapper {
    List<Integer> getDeviceIdsByJobID (Integer jobId);

    void jobDeviceSave (JobDevices jobDevices);

    /*测试DM的根据设备id和属性名称查属性id
     */
    Integer getField_ByDeviceTypeAndFieldName(@Param("deviceType") Integer deviceType , @Param("fieldName") String fieldName);
}

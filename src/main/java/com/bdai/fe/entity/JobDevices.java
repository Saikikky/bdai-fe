package com.bdai.fe.entity;

public class JobDevices {

    // id
    private Integer id;

    // 外键job_id
    private Integer job_id;

    // 设备id
    private Integer device_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    @Override
    public String toString() {
        return "JobDevices{" +
                "id=" + id +
                ", job_id=" + job_id +
                ", device_id=" + device_id +
                '}';
    }
}

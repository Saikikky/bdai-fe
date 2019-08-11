package com.bdai.fe.dto;

import java.util.List;

public class JobDto {
    //状态返回码
    Integer code ;

    //job的系列信息
    List<Jobmsg>  jobs;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Jobmsg> getJobs() {
        return jobs;
    }

    public void setJobs(List<Jobmsg> jobs) {
        this.jobs = jobs;
    }

    public JobDto(Integer code, List<Jobmsg> jobs) {
        this.code = code;
        this.jobs = jobs;
    }

    public JobDto() {
    }

    @Override
    public String toString() {
        return "JobDto{" +
                "code=" + code +
                ", jobs=" + jobs +
                '}';
    }

    public class Jobmsg {
        //job id
        private Integer id ;

        //处理方案名称
        private Integer schema;

        //处理运行状态
        private Integer progress;

        //数据集名称
        private String dataset_name;

        //用户id
        private Integer user;

        //所有设备实例
        private List<Integer> devices;

        public Jobmsg() {
        }

        public Jobmsg(Integer id, Integer schema, Integer progress, String dataset_name, Integer user, List<Integer> devices) {
            this.id = id;
            this.schema = schema;
            this.progress = progress;
            this.dataset_name = dataset_name;
            this.user = user;
            this.devices = devices;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSchema() {
            return schema;
        }

        public void setSchema(Integer schema) {
            this.schema = schema;
        }

        public Integer getProgress() {
            return progress;
        }

        public void setProgress(Integer progress) {
            this.progress = progress;
        }

        public String getDataset_name() {
            return dataset_name;
        }

        public void setDataset_name(String dataset_name) {
            this.dataset_name = dataset_name;
        }

        public Integer getUser() {
            return user;
        }

        public void setUser(Integer user) {
            this.user = user;
        }

        public List<Integer> getDevices() {
            return devices;
        }

        public void setDevices(List<Integer> devices) {
            this.devices = devices;
        }

        @Override
        public String toString() {
            return "Job{" +
                    "id=" + id +
                    ", schema=" + schema +
                    ", progress=" + progress +
                    ", dataset_name='" + dataset_name + '\'' +
                    ", user=" + user +
                    ", devices=" + devices +
                    '}';
        }
    }
}

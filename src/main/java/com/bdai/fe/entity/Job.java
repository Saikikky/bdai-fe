package com.bdai.fe.entity;

public class Job {

    // id
    private Integer id;

    // sid
    private Integer sid;

    // 执行者id
    private Integer excutor_id;

    // 目标数据集名称
    private String dataset_name;

    // 运行状态
    private Integer progress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }


    public Integer getExcutor_id() {
        return excutor_id;
    }

    public void setExcutor_id(Integer excutor_id) {
        this.excutor_id = excutor_id;
    }

    public String getDataset_name() {
        return dataset_name;
    }

    public void setDataset_name(String dataset_name) {
        this.dataset_name = dataset_name;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", sid=" + sid +
                ", excutor_id=" + excutor_id +
                ", dataset_name='" + dataset_name + '\'' +
                ", progress=" + progress +
                '}';
    }
}

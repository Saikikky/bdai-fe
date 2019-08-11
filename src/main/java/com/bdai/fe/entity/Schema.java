package com.bdai.fe.entity;

import java.util.List;

public class Schema {

    // id
    private Integer id;

    // 预处理流程名称
    private String name;

    //外键
    private Integer device_type;

    // 创建者id
    private Integer create_by;

    //流程
    private List<Flow> flowList;

    public List<Flow> getFlowList() { return flowList; }

    public void setFlowList(List<Flow> flowList) { this.flowList = flowList; }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schema(){}

    public Schema(String name, Integer device_type, Integer create_by) {
        this.name = name;
        this.device_type = device_type;
        this.create_by = create_by;
    }

    public Integer getDevice_type() {
        return device_type;
    }

    public void setDevice_type(Integer device_type) {
        this.device_type = device_type;
    }

    public Integer getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Integer create_by) {
        this.create_by = create_by;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", device_type=" + device_type +
                ", create_by=" + create_by +
                '}';
    }
}

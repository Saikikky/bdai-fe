package com.bdai.fe.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class SchemaDto {
    private Integer ID;
    private String name;
    private Integer device_type;
    private Integer create_by;
    private List<FlowDto> flow;

    public SchemaDto() {
    }

    public SchemaDto(Integer ID, String name, Integer device_type,Integer create_by, List<FlowDto> flow) {
        this.ID = ID;
        this.name = name;
        this.device_type = device_type;
        this.create_by = create_by;
        this.flow = flow;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDevice_type() {
        return device_type;
    }

    public void setDevice_type(Integer device_type) {
        this.device_type = device_type;
    }

    public List<FlowDto> getFlow() {
        return flow;
    }

    public void setFlow(List<FlowDto> flow) {
        this.flow = flow;
    }

    public Integer getCreate_by() { return create_by; }

    public void setCreate_by(Integer create_by) { this.create_by = create_by; }

    @Override
    public String toString() {
        return "SchemaDto{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", device_type=" + device_type +
                ", create_by=" + create_by +
                ", flow=" + flow +
                '}';
    }
}

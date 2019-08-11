package com.bdai.fe.entity;

public class Flow {

    // id
    private Integer id;

    // sid（外键）
    private Integer sid;

    // 流程顺序
    private Integer index;

    // 流程类型名称
    private String method;

    // 流程参数的byte类型
    // JSON类型的mysql数据，与mybatis的映射需要做额外操作
    private byte[] params;

    public  Flow(){}
    public Flow(Integer sid, Integer index, String method, byte[] params) {
        this.sid = sid;
        this.index = index;
        this.method = method;
        this.params = params;
    }

    public Flow(Integer index, String method, byte[] params) {
        this.index = index;
        this.method = method;
        this.params = params;
    }

    public Flow(Integer id, Integer sid, Integer index, String method, byte[] params) {
        this.id = id;
        this.sid = sid;
        this.index = index;
        this.method = method;
        this.params = params;
    }


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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public byte[] getParams() { return params; }

    public void setParams(byte[] params) { this.params = params; }


    @Override
    public String toString() {
        return "Flow{" +
                "id=" + id +
                ", sid=" + sid +
                ", index=" + index +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}

package com.bdai.fe.dto;

public class FlowDto {
    private Integer fid;
    private Integer index;
    private String method;
    private Object params;

    public FlowDto() {
    }

    public FlowDto(Integer fid, Integer index, String method, Object params) {
        this.fid = fid;
        this.index = index;
        this.method = method;
        this.params = params;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
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

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "FlowDto{" +
                "fid=" + fid +
                ", index=" + index +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}

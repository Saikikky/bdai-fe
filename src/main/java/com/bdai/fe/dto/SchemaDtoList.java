package com.bdai.fe.dto;

import java.util.List;

public class SchemaDtoList {
    private Integer code;
    private List<SchemaDto> schemas;

    public Integer getCode() { return code; }

    public void setCode(Integer code) { this.code = code; }

    public List<SchemaDto> getSchemas() { return schemas; }

    public void setSchemas(List<SchemaDto> schemas) { this.schemas = schemas; }

    public SchemaDtoList() {
    }

    public SchemaDtoList(Integer code, List<SchemaDto> schemas) {
        this.code = code;
        this.schemas = schemas;
    }

    @Override
    public String toString() {
        return "SchemaDtoList{" +
                "code=" + code +
                ", schemas=" + schemas +
                '}';
    }
}

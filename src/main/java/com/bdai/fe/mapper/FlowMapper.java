package com.bdai.fe.mapper;

import com.bdai.fe.entity.Flow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FlowMapper {


    boolean flowSave(@Param("sid") Integer sid, @Param("index") Integer index, @Param("method") String method, @Param("params") byte[] params);

    Flow getFlowById(Integer id);

    boolean flowUpdate(@Param("id") Integer id, @Param("method") String method, @Param("params") byte[] params);

    Flow getFlow();

    Integer deleFlowById(Integer id);

    List<Flow> getFlowBySid(Integer sid);

    byte[] getParams(@Param("id") int id);
}

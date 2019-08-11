package com.bdai.fe.service;


import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.List;

public interface IUtilService {

    String verifyUser(Cookie[] cookies, List<Object> intValues) throws IOException;
    /* 根据设备类型和属性名称查属性id */
    Integer getField_ByDeviceTypeAndFieldName(Cookie[] cookies,Integer deviceType,String devieceType_fieldName) throws IOException;


}

package com.bdai.fe.service.Impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdai.fe.service.IHttpClientService;
import com.bdai.fe.service.IUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.Cookie;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilServiceImpl implements IUtilService {
    @Autowired
    private IHttpClientService iHttpClientService;

    /*
    List 为包含权限码 ，若不包含则抛出异常  ，这里 也可返回 空 自己选择  line 52

            //auth
            try {
                List<Object> list = new ArrayList<>();
                list.add(1);
                userName = iUtilService.verifyUser(request.getCookies(),list);
            } catch (IOException e) {
                Result result = Result.error();
                result.setMessage("error to get auth..");
                return result;
            }

    */
    public String verifyUser(Cookie[] cookies,List<Object> intValues) throws IOException {
        try {
            Map<String, String> cookieMap = new HashMap<>();
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        } catch (Exception e) {
            return null;
        }

        String userName = null;
        String userId = null;
        //verify auth
        String result = iHttpClientService.Get("http://auth:8080/usermanagement/v1/currentuser",
                null, cookies);
        //
        System.out.println(result + "sss");
        if (result == null) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject.get("code"));
        if (!jsonObject.get("code").equals(0)) {
            return null;
        }
        String tmp = jsonObject.get("user").toString();
        System.out.println(tmp);
        List privilege = JSON.parseArray(JSON.parseObject(tmp).get("privilege").toString());
        System.out.println(privilege);
        System.out.println(intValues);
        System.out.println(privilege.containsAll(intValues));
        userName = JSON.parseObject(tmp).get("nickname").toString();
        userId = JSON.parseObject(tmp).get("ID").toString();

        if (!privilege.containsAll(intValues)) {
            return null;
        }

        return userId;
    }

    @Override
    public Integer getField_ByDeviceTypeAndFieldName(Cookie[] cookies,Integer deviceType, String devieceType_fieldName) throws IOException {
        //调用段铭的接口返回json数据
        String result = iHttpClientService.Get("http://device:8080/devicemanagement/v1/devicetype?ID="+deviceType,null,cookies);
        System.out.println(result + "--------------------");
        if (result == null) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(result);
        JSONObject dtype = jsonObject.getJSONObject("dtype");
        JSONArray runtime_props = dtype.getJSONArray("runtime_props");
        for (int i = 0 ; i < runtime_props.size();i++){
            JSONObject jsonObj = runtime_props.getJSONObject(i);
            String prop_name = jsonObj.getString("prop_name").trim();
            if (prop_name.equals(devieceType_fieldName))
                //如果属性名称相同，返回该名称id
                return Integer.parseInt(jsonObj.getString("prop_id").trim());
        }
        return null;
    }


}

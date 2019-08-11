package com.bdai.fe.service.Impl;

import com.bdai.fe.service.IUtilService;
import com.bdai.fe.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    @Autowired
    IUtilService iUtilService;

    @Override
    public String verify(Cookie[] cookies, List<Object> intValues) {
        String userId;
        try {
            userId = iUtilService.verifyUser(cookies, intValues);
        } catch (IOException e) {
            // 30101
            return "2";
        }

        if (userId == null) {
            // 30101
            return "2";
        }
        return userId;
    }
}

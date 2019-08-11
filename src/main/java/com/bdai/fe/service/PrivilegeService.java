package com.bdai.fe.service;

import javax.servlet.http.Cookie;
import java.util.List;

public interface PrivilegeService {

    String verify(Cookie[] cookies, List<Object> intValues);
}

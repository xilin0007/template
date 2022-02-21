package com.fxl.template.user.controller;

import com.fxl.template.user.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/demo")
public class DemoController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/get")
    @ResponseBody
    public Object get(HttpServletRequest request,
                      UserInfo userInfo,
                       Map<String, String> paramsMap
                      ) {

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        log.info("-------------------getParameter id: " + id);
        log.info("-------------------getParameter name: " + name);

//        String id1 = paramsMap.get("id");
//        String name1 = paramsMap.get("name");
        log.info("-------------------userInfo: " + userInfo);
        log.info("-------------------paramsMap id: " + paramsMap.get("id"));

        return "get success";
    }

    @RequestMapping("/send")
    @ResponseBody
    public Object send(HttpServletRequest request,
                       Map<String, String> paramsMap) {

        log.info("**************send paramsMap: " + paramsMap);
//        Map<String, String> paramsMap1 =  new HashMap<>();
//        paramsMap1.put("version", "1.0");
        paramsMap = paramsMap == null ? new HashMap<>() : paramsMap;
        paramsMap.put("id", request.getParameter("id1"));
        paramsMap.put("name", request.getParameter("name1"));
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("abcdefg");
        Object o = get(request, userInfo, paramsMap);

        return "send success";
    }
}

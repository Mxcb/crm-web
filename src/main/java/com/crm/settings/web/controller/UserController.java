package com.crm.settings.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.MD5Util;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    private static final long serialVersionUID = -906350263799244032L;

    protected void service(HttpServletRequest request, HttpServletResponse response){
       String path=request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String loginAct=request.getParameter("loginAct");
        String loginPwd=request.getParameter("loginPwd");
        //获取IP地址
        String ip=request.getRemoteAddr();
        System.out.println(ip);
        loginPwd= MD5Util.getMD5(loginPwd);
        //未来的业务开发统一使用代理类形态的接口对象
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
           User user=userService.login(loginAct,loginPwd,ip);
           request.getSession().setAttribute("user",user);
           //System.out.println(request.getSession().getAttribute("user"));
            //如果登陆验证失败,出现异常就不会执行到此处
           PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
           e.printStackTrace();
           String msg=e.getMessage();
           Map<String,Object> map=new HashMap<>();
           map.put("success",false);
           map.put("msg",msg);
           PrintJson.printJsonObj(response,map);
        }

    }
}

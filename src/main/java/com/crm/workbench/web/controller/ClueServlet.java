package com.crm.workbench.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.clue.Clue;
import com.crm.workbench.domain.transaction.Tran;
import com.crm.workbench.service.ActivityService;
import com.crm.workbench.service.ClueService;
import com.crm.workbench.service.impl.ActivityServiceImpl;
import com.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String path=request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request,response);
        }else if ("/workbench/clue/saveClue.do".equals(path)){
            saveClue(request,response);
        }else if ("/workbench/clue/clueDetail.do".equals(path)){
            clueDetail(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbind.do".equals(path)){
            unbind(request,response);
        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }else if ("/workbench/clue/bind.do".equals(path)) {
            bind(request,response);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到线索转换操作");
        String clueId=request.getParameter("clueId");
        String method=request.getMethod();
        if ("POST".equals(method)){
            String name=request.getParameter("name");
            String money=request.getParameter("money");
            String stage=request.getParameter("stage");
            String expectedTime=request.getParameter("expectedTime");
            String activityId=request.getParameter("activityId");
            String id=UUIDUtil.getUUID();
            String createTime=DateTimeUtil.getSysTime();
            String createBy=((User)request.getSession().getAttribute("user")).getName();
            Tran tran=new Tran(id,money,name,expectedTime,stage,activityId,createBy,createTime);
            ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
            boolean flag=clueService.convert(tran,clueId,createBy);
        }else {
            String createBy=((User)request.getSession().getAttribute("user")).getName();
            ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
            Tran tran=null;
            boolean flag=clueService.convert(tran,clueId,createBy);
        }
        response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String name=request.getParameter("name");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activities=activityService.getActivityListByName(name);
        PrintJson.printJsonObj(response,activities);
    }

    private void bind(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到添加市场活动操作");
        String cid=request.getParameter("cid");
        String[] aids=request.getParameterValues("aid");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=clueService.bind(aids,cid);
        System.out.println(flag);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取关联市场活动列表操作");
        String name=request.getParameter("name");
        String clueId=request.getParameter("clueId");
        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("clueId",clueId);
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activities=activityService.selectByLikeName(map);
        PrintJson.printJsonObj(response,activities);
    }

    private void unbind(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入解除市场关联的操作");
        String id=request.getParameter("id");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=clueService.unbind(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到根据线索ID获取市场活动列表的操作");
        String clueId=request.getParameter("clueId");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activities=activityService.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,activities);
    }

    private void clueDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索的详细信息页");
        String id=request.getParameter("id");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=clueService.clueDetail(id);
        request.setAttribute("clueOwner",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索添加操作");
        String  id= UUIDUtil.getUUID();
        String  fullname= request.getParameter("fullname");
        String  appellation= request.getParameter("appellation");
        String  owner= request.getParameter("owner");
        String  company= request.getParameter("company");
        String  job= request.getParameter("job");
        String  email= request.getParameter("email");
        String  phone= request.getParameter("phone");
        String  website= request.getParameter("website");
        String  mphone= request.getParameter("mphone");
        String  state= request.getParameter("state");
        String  source= request.getParameter("source");
        String  createBy= ((User)request.getSession().getAttribute("user")).getName();
        String  createTime= DateTimeUtil.getSysTime();
        String  description= request.getParameter("description");
        String  contactSummary= request.getParameter("contactSummary");
        String  nextContactTime= request.getParameter("nextContactTime");
        String  address= request.getParameter("address");
        Clue clue=new Clue(id,fullname,appellation,owner,company,job,email,phone,
                website,mphone,state,source,createBy,createTime,description,contactSummary,nextContactTime,address);
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=clueService.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索模块获取用户列表");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=userService.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}

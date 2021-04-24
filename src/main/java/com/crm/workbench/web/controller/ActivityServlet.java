package com.crm.workbench.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;
import com.crm.utils.UUIDUtil;
import com.crm.vo.Pagination;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.ActivityRemark;
import com.crm.workbench.service.ActivityService;
import com.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityServlet extends HttpServlet {
    protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        System.out.println("进入到市场活动控制器");
        String path=request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)) {
            update(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/getRemarkListByActId.do".equals(path)){
            getRemarkListByActId(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改备注信息列表操作");
        String id=request.getParameter("id");
        String noteContent=request.getParameter("noteContent");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="1";
        ActivityRemark remark=new ActivityRemark(id,noteContent,editTime,editBy,editFlag);
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.updateRemark(remark);
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("ac",remark);
        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到添加备注信息列表操作");
        String activityId=request.getParameter("activityId");
        String noteContent=request.getParameter("noteContent");
        String id=UUIDUtil.getUUID();
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="0";
        ActivityRemark remark=new ActivityRemark(id,noteContent,createTime,createBy,editFlag,activityId);
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.saveRemark(remark);
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("actRem",remark);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除备注信息列表操作");
        String id=request.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByActId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id,获取备注信息列表");
        String activityId=request.getParameter("activityId");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> activityRemarks=activityService.getRemarkListByActId(activityId);
        PrintJson.printJsonObj(response,activityRemarks);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转详细信息页的操作");
        String id=request.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity=activityService.detail(id);
        request.setAttribute("act",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到市场活动的修改操作");
        String id= request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String editTime= DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();

        Activity activity=new Activity();
        activity.setCost(cost);
        activity.setCreateBy(editBy);
        activity.setCreateTime(editTime);
        activity.setDescription(description);
        activity.setEndDate(endDate);
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);

        ActivityService ac= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=ac.updateById(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据和根据市场活动id查询单条记录操作");
        String id=request.getParameter("id");
        ActivityService ac= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map=ac.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");
        //获取参数数组,适用于参数名字相同
        String[] ids=request.getParameterValues("id");
        ActivityService ac= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=ac.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询市场列表活动的操作");
        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String pageSizeStr=request.getParameter("pageSize");
        String pageNoStr=request.getParameter("pageNo");
        //每页展示的记录数
        Integer pageSize=Integer.valueOf(pageSizeStr);
        //当前是第几页
        Integer pageNo=Integer.valueOf(pageNoStr);
        Integer skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService ac= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Pagination<Activity> pa=ac.pageList(map);
        PrintJson.printJsonObj(response,pa);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();

        Activity activity=new Activity();
        activity.setCost(cost);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        activity.setDescription(description);
        activity.setEndDate(endDate);
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);

        ActivityService ac= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=ac.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request,HttpServletResponse response) {
         UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
         List<User> users=userService.getUserList();
         PrintJson.printJsonObj(response,users);
    }
}

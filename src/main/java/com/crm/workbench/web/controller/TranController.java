package com.crm.workbench.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.domain.customer.Customer;
import com.crm.workbench.domain.transaction.Tran;
import com.crm.workbench.domain.transaction.TranHistory;
import com.crm.workbench.service.CustomerService;
import com.crm.workbench.service.TranService;
import com.crm.workbench.service.impl.CustomerServiceImpl;
import com.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;

public class TranController extends HttpServlet {

    private static final long serialVersionUID = -3415895055099856444L;

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易控制器");
        String path=request.getServletPath();
        if ("/workbench/transaction/create.do".equals(path)){
            create(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerNames(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/transaction/getTranHistory.do".equals(path)){
            getTranHistory(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        } else if ("/workbench/chart/transaction/getCharts.do".equals(path)) {
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取交易漏斗图数据的操作");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> list= tranService.getCharts();
        PrintJson.printJsonObj(response,list);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改阶段的操作");
        String id=request.getParameter("id");
        String stage=request.getParameter("stage");
        String money=request.getParameter("money");
        String expectedDate=request.getParameter("expectedDate");
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editTime= DateTimeUtil.getSysTime();
        Map<String,String> m= (Map<String, String>) request.getServletContext().getAttribute("mapBundle");
        Tran t=new Tran();
        t.setId(id);
        t.setMoney(money);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);;
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setPossibility(m.get(stage));
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=tranService.changeStage(t);
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("tran",t);
        PrintJson.printJsonObj(response,map);
    }

    private void getTranHistory(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取历史列表的操作");
        String tranId=request.getParameter("tranId");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> tranHistory=tranService.selectHistoryById(tranId);
        Map<String,String> map= (Map<String, String>) request.getServletContext().getAttribute("mapBundle");
        tranHistory.forEach(tranHis -> tranHis.setPossibility(map.get(tranHis.getStage())) );
        PrintJson.printJsonObj(response,tranHistory);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转详细页");
        String id=request.getParameter("id");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran=tranService.detail(id);
        Map<String,String> map= (Map<String, String>) request.getServletContext().getAttribute("mapBundle");
        tran.setPossibility(map.get(tran.getStage()));
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到执行添加交易的操作");
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedDate");
        String customerId=request.getParameter("customerId");
        String customerName=request.getParameter("customerName");
        String stage=request.getParameter("stage");
        String type=request.getParameter("type");
        String source=request.getParameter("source");
        String activityId=request.getParameter("activityId");
        String contactsId=request.getParameter("contactsId");
        String createBy=request.getParameter("createBy");
        String createTime=request.getParameter("createTime");
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        Tran tran=new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setCustomerId(customerId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=tranService.save(tran,customerName);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerNames(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取客户列表的操作");
        String name=request.getParameter("name");
        CustomerService customerService= (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Customer> customers=customerService.getCustomersByLike(name);
        PrintJson.printJsonObj(response,customers);
    }

    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转交易添加页的操作");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users=userService.getUserList();
        request.setAttribute("users",users);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }

}

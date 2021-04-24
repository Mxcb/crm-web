package com.crm.web.listener;

import com.crm.settings.domain.DicType;
import com.crm.settings.domain.DicValue;
import com.crm.settings.service.DicService;
import com.crm.settings.service.impl.DicServiceImpl;
import com.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.*;

public class SysInitListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public SysInitListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        //通过sec对象获取监听到的上下文域对象,source就是application
        //javax.servlet.ServletContextEvent[source=org.apache.catalina.core.ApplicationContextFacade@626f893a]
        System.out.println("上下文对象创建,服务器缓存数据字典开始");
        ServletContext application=sce.getServletContext();
        DicService dicService= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map=dicService.getDic();
        Set<String> keys=map.keySet();
        for (String key:keys){
            application.setAttribute(key,map.get(key));
        }
//        System.out.println(map);
        System.out.println("服务器缓存数据字典结束");

        ResourceBundle resourceBundle=ResourceBundle.getBundle("Stage2Possibility");
        Map<String,String> mapBundle=new HashMap<>();
        Enumeration<String> e=resourceBundle.getKeys();
        while (e.hasMoreElements()){
           String key= e.nextElement();
           String value=resourceBundle.getString(key);
           mapBundle.put(key,value);
        }
        System.out.println(mapBundle);
//        String s="{'key':13,}";
        application.setAttribute("mapBundle",mapBundle);
//        application.setAttribute("s",s);
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
    }
}

package com.crm.web.filter;

import com.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("验证登陆的Filter");
        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;
        String path=request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);
        }else {
            HttpSession session=request.getSession();
            User user= (User) session.getAttribute("user");
            if (user!=null){
                chain.doFilter(req, resp);
            }else {
                /**
                 * 无论是转发还是重定向,一律使用绝对路径:
                 *      转发使用的是一种特殊的绝对路径,不加项目名,这种路径也称为内部路径;
                 *      重定向前面必须以/项目名开头。
                 */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}

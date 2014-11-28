package com.hotelmaster.home.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * AcegiAjax过滤器 防止服务端redirect刷新Client端页面(eg:erro页面)
 * 
 * @
 * @Date May 8, 2008
 */
public class AcegiAjaxFilter extends OncePerRequestFilter {   
    
    protected void doFilterInternal(HttpServletRequest request,   
                                    HttpServletResponse response,    
                                    FilterChain filterChain) throws ServletException, IOException {   
    	System.out.println("reach acegiajaxfilter");
        if (!isAjaxRequest(request)) {   
            filterChain.doFilter(request, response);
            System.out.println("reach acegiajaxfilter not ajax");
            return;   
        }
        
        RedirectResponseWrapper redirectResponseWrapper = new RedirectResponseWrapper(response);   
    
        filterChain.doFilter(request, redirectResponseWrapper);   
        
        if (redirectResponseWrapper.getRedirect() != null) {   
        	
            request.setCharacterEncoding("UTF-8");   
            response.setContentType("text/json;charset=utf-8");   
    
            response.setHeader("Cache-Control", "no-cache");   
            response.setDateHeader("Expires", 0);   
            response.setHeader("Pragma", "no-cache");   
  
    
            String redirectURL = redirectResponseWrapper.getRedirect();   
            String content;
            if (redirectURL.indexOf("login_error=1") == -1) {   
            	response.getWriter().write("{success: true}");   
            } else {   
            	response.getWriter().write("{errors:[{id:'j_username',msg:'用户名或密码错误'},{id:'j_password',msg:'用户名或密码错误'}]}");
                /*content = "error:" + ((AuthenticationException) request.   
                                    getSession().getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage();*/   
            }   
           // response.getOutputStream().write(content.getBytes("UTF-8"));   
        }   
    }   
  
    
    private boolean isAjaxRequest(HttpServletRequest request) {   
        return request.getParameter("ajax") != null;   
    }
    private boolean isLogoutAction(HttpServletRequest request){
    	return false;
    }
}   


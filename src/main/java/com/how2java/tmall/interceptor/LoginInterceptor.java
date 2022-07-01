package com.how2java.tmall.interceptor;

import com.how2java.tmall.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hsqldb.lib.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {//对于没有登录不能访问的界面进行拦截
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception{
        HttpSession session=httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
        String [] requireAuthPages=new String[]{//需要登录才能访问的路径
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",
                "review",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };
        String uri=httpServletRequest.getRequestURI();
        uri= StringUtils.remove(uri,contextPath+"/");//去掉前缀/tmall_springboot
        String page=uri;
        if (begingWith(page,requireAuthPages)){
            Subject subject= SecurityUtils.getSubject();
            if (!subject.isAuthenticated()){
                httpServletResponse.sendRedirect("login");
                return false;
            }
        }
        return true;
    }
    private boolean begingWith(String page,String[] requiredAuthPages){
        boolean result=false;
        for (String requiredAuthPage:requiredAuthPages){
            if (StringUtils.startsWith(page,requiredAuthPage)){//判断是否以requiredAuthPage中的需要登录的为前缀
                result=true;
                break;
            }
        }
        return result;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)throws Exception{}
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}

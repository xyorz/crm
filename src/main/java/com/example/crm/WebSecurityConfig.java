package com.example.crm;

import com.example.crm.entity.Employee;
import com.example.crm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    public final static String SESSION_KEY="employeenum";

    @Autowired
    EmployeeRepository employeeRepository;

    public void  addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new SecurityInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/login**").excludePathPatterns("/error").excludePathPatterns("/assets/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();

//            判断是否已有该用户登录的session
            if(session.getAttribute(SESSION_KEY) != null){
                //  更新登陆员工信息session
                Optional<Employee> optionalEmployee = employeeRepository.findById( Integer.parseInt((String) session.getAttribute(SESSION_KEY)));
                if(optionalEmployee.isPresent()){
                    Employee employee = optionalEmployee.get();

                    //  产品经理只允许访问产品信息页面
                    if(employee.getLevel()>=1){
                        if(request.getRequestURI().indexOf("/product")!=0){
                            response.sendRedirect("/product");
                        }
                    }

                    session.setAttribute("loginEmployee", employee);
                    return true;
                }
            }

//            跳转到登录页
            String url = "/login";
            response.sendRedirect(url);
            return false;
        }
    }
}





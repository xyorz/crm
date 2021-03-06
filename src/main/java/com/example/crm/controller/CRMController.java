package com.example.crm.controller;

import com.example.crm.WebSecurityConfig;
import com.example.crm.entity.Employee;
import com.example.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class CRMController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView loginVerify(String username, String password, HttpSession session, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();

        try{
            Integer id = Integer.valueOf(username);
            Employee employee = employeeRepository.findByIdAndPassword(id,password);
            if (employee != null){
                session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
                session.setAttribute("loginEmployeeId", id);
                session.setAttribute("level", employee.getLevel());
                session.setAttribute("loginEmployee", employee);

                //  产品经理只配访问产品管理页面
                if(employee.getLevel()>=1)
//                    mav.setViewName("product");
                    response.sendRedirect("/product");
                else
                    mav.setViewName("index");
            }
            else {
                mav.setViewName("login");
                mav.addObject("errorInfo", 0);
            }
        }
        catch (NumberFormatException nfe){
            mav.setViewName("login");
            mav.addObject("errorInfo","用户名格式错误");
        }
        catch (Exception e){
            mav.setViewName("login");
            mav.addObject("errorInfo","错误");
        }
        return mav;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "login";
    }

}



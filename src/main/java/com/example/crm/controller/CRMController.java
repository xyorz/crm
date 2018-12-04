package com.example.crm.controller;

import com.example.crm.WebSecurityConfig;
import com.example.crm.entity.Employee;
import com.example.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
//@RequestMapping(path = "")
public class CRMController {

//    @RequestMapping(path = "")
//    public String index(){
//        return "index";
//    }

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

//    @GetMapping("/index")
//    public String index1()
//    {
//        return "index";
//    }

    @PostMapping("/login")
    public ModelAndView loginVerify(String username, String password, HttpSession session){
        ModelAndView mav = new ModelAndView();

        try{
            Integer id = Integer.valueOf(username);
            Employee employee = employeeRepository.findByIdAndPassword(id,password);
            if (employee != null){
                session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
                mav.setViewName("/");
            }
            mav.setViewName("login");
            mav.addObject("errorInfo","用户名或密码错误");

        }
        catch (Exception e){

        }
        return mav;

    }
}


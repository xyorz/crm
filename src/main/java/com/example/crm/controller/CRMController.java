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

    @PostMapping("/login")
    public ModelAndView loginVerify(String username, String password, HttpSession session){

//        Optional<Employee> optionalEmployee = Optional.of(employeeRepository.findByIdAndPassword(Integer.valueOf(username),password));
        Employee employee = employeeRepository.findByIdAndPassword(Integer.valueOf(username),password);
        ModelAndView mav = new ModelAndView();
        if (employee != null){
            session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            mav.setViewName("index");
            return mav;
        }
        mav.setViewName("login");
        mav.addObject("errorInfo","用户名或密码错误");
        return mav;
    }
}


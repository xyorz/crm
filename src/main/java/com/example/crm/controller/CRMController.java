package com.example.crm.controller;

import com.example.crm.WebSecurityConfig;
import com.example.crm.entity.Employee;
import com.example.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String loginVerify(String username, String password, HttpSession session){

        Optional<Employee> optionalEmployee = Optional.of(employeeRepository.findByIdAndPassword(Integer.valueOf(username),password));
        if (optionalEmployee.isPresent()){
            session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            return "index";
        }
        return "redirect:/login";
    }
}


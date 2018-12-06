package com.example.crm.controller.crm;

import com.example.crm.entity.Employee;
import com.example.crm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(path = "employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView employees(){
        ModelAndView mav = new ModelAndView("employee");
        mav.addObject("employees", employeeRepository.findAll());
        return mav;
    }

    @PostMapping(path = "")
    public ModelAndView addEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return employees();
    }

    @PostMapping(path = "update")
    public ModelAndView update(@RequestBody Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        if(optionalEmployee.isPresent()){
            Employee emp = optionalEmployee.get();
            emp.setName(employee.getName());
            emp.setTel(emp.getTel());
            emp.setSex(emp.getSex());
            emp.setRemark(emp.getRemark());
            emp.setEmpty(employee.getEmpty());
            employeeRepository.save(employee);
        }
        return employees();
    }
}

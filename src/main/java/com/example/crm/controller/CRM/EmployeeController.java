package com.example.crm.controller.CRM;

import com.example.crm.entity.Employee;
import com.example.crm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ModelAndView addEmployee(@RequestParam String name, @RequestParam String tel, @RequestParam String password, @RequestParam boolean sex, @RequestParam boolean empty, @RequestParam String remark) {
        Employee employee = new Employee(name, sex, tel, password, empty, remark);
        employeeRepository.save(employee);
        return employees();
    }

    @PostMapping(path = "update")
    public ModelAndView update(@RequestParam Integer id, @RequestParam String name, @RequestParam String tel, @RequestParam boolean sex, @RequestParam boolean empty, @RequestParam String remark) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            employee.setName(name);
            employee.setTel(tel);
            employee.setSex(sex);
            employee.setRemark(remark);
            employee.setEmpty(empty);
            employeeRepository.save(employee);
        }
        return employees();
    }
}

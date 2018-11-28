package com.example.crm.controller;

import com.example.crm.entity.Customer;
import com.example.crm.repository.*;
import com.example.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/")
public class CRMController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LinkManRepository linkManRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SaleOpportunityRepository saleOpportunityRepository;


    @RequestMapping(path = "/customers")
    public ModelAndView showAllCustomers() {
        ModelAndView mav = new ModelAndView("customers");
        mav.addObject("customers", customerRepository.findAll());
        return mav;
    }

    @RequestMapping(path = "/addCustomers")
    public ResponseEntity<Object> addCustomer(@RequestParam String name, @RequestParam String tel, @RequestParam String address, @RequestParam Integer credit, @RequestParam String text) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setTel(tel);
        customer.setAddress(address);
        customer.setCredit(credit);
        customer.setText(text);
        customerRepository.save(customer);
        Map<String, String> msg = new HashMap<String, String>();
        msg.put("data", "Success to add");
        return new ResponseEntity<Object>(msg, HttpStatus.OK);
    }

    @RequestMapping(path = "/employees")
    public ModelAndView showAllEmployees() {
        ModelAndView mav = new ModelAndView("customers");
        mav.addObject("customers", employeeRepository.findAll());
        return mav;
    }
}


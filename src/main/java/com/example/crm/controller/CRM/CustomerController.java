package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("")
    public ModelAndView customers(){
        ModelAndView mav = new ModelAndView("customer");
        mav.addObject("customers", customerRepository.findAll());
        return mav;
    }

    @PostMapping(path = "")
    public ModelAndView addCustomer(@RequestParam String name, @RequestParam String tel, @RequestParam String address, @RequestParam Integer credit, @RequestParam String text) {
        Customer customer = new Customer(name, tel, address, text, credit);
        customerRepository.save(customer);
        return customers();
    }

    @PostMapping(path = "update")
    public ModelAndView update(@RequestParam Integer id, @RequestParam String name, @RequestParam String tel, @RequestParam String address, @RequestParam Integer credit, @RequestParam String text) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            customer.setName(name);
            customer.setTel(tel);
            customer.setAddress(address);
            customer.setCredit(credit);
            customer.setText(text);
            customerRepository.save(customer);
        }
        return customers();
    }
}

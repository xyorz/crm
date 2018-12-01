package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.repository.CustomerRepository;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(path = "customer")
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
    public ResponseEntity<Map<String, String>> add(@RequestBody Customer customer) {
        customerRepository.save(customer);

        Map<String, String> map = new HashMap<>();
        map.put("message", "success");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if(optionalCustomer.isPresent()){
            Customer cus = optionalCustomer.get();
            cus.setName(customer.getName());
            cus.setTel(customer.getTel());
            cus.setAddress(customer.getAddress());
            cus.setCredit(customer.getCredit());
            cus.setText(customer.getText());
            customerRepository.save(cus);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "customer did not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "delete")
    public ResponseEntity<Map<String, String>> delete(@RequestBody JSONObject jsonObject) {
        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("id"));
        if(optionalCustomer.isPresent()){
            customerRepository.delete(optionalCustomer.get());

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "customer did not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

}

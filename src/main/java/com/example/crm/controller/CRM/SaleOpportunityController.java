package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.SaleOpportunity;
import com.example.crm.entity.Product;
import com.example.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(path = "sale_opportunity")
public class SaleOpportunityController {

    @Autowired
    private SaleOpportunityRepository saleOpportunityRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView saleOpportunity(){
        ModelAndView mav = new ModelAndView("sale_opportunity");
        mav.addObject("saleOpportunities", saleOpportunityRepository.findAll());
        return mav;
    }

    @PostMapping(path = "")
    public ModelAndView addSaleOpportunity(@RequestParam Integer customerId, @RequestParam Integer employeeId,
                                           @RequestParam(value = "product_ids") Integer[] productIds,
                                           @RequestParam String record, @RequestParam boolean isDeclare) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        Iterable<Product> productIt = productRepository.findAllById(Arrays.asList(productIds));
        List<Product> products = new ArrayList<>();
        while(productIt.iterator().hasNext()){
            Product p = productIt.iterator().next();
            products.add(p);
        }
        if(optionalCustomer.isPresent()&&optionalEmployee.isPresent()){
            Customer customer = optionalCustomer.get();
            Employee employee = optionalEmployee.get();
            SaleOpportunity saleOpportunity = new SaleOpportunity(isDeclare, employee, products, customer, record);
            saleOpportunityRepository.save(saleOpportunity);
        }
        return saleOpportunity();
    }

}

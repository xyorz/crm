package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.Orders;
import com.example.crm.entity.Product;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.EmployeeRepository;
import com.example.crm.repository.OrderRepository;
import com.example.crm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "/order")
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView orders(){
        ModelAndView mav = new ModelAndView("order");
        mav.addObject("orders", orderRepository.findAll());
        return mav;
    }

    @PostMapping(path = "")
    public ModelAndView addOrders(@RequestParam Integer customerId, @RequestParam Integer employeeId, @RequestParam Integer productId,
                                  @RequestParam String date, @RequestParam float value, @RequestParam float paidValue,
                                  @RequestParam String variety, @RequestParam String status) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalCustomer.isPresent()&&optionalEmployee.isPresent()&&optionalProduct.isPresent()){
            Customer customer = optionalCustomer.get();
            Employee employee = optionalEmployee.get();
            Product product = optionalProduct.get();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date d = sdf.parse(date);
                Orders order = new Orders(customer, employee, product, status, d, value, paidValue, variety);
                orderRepository.save(order);
            } catch (ParseException e){
                e.printStackTrace();
            }
            finally {
                return orders();
            }
        }
        return orders();
    }

}

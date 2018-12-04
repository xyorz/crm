package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.Orders;
import com.example.crm.entity.Product;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.EmployeeRepository;
import com.example.crm.repository.OrderRepository;
import com.example.crm.repository.ProductRepository;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "order")
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView orders(){
        ModelAndView mav = new ModelAndView("order");
        mav.addObject("orders", orderRepository.findAll());
        return mav;
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String, String>> add(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customerId"));
        Optional<Employee> optionalEmployee = employeeRepository.findById(jsonObject.getInt("employeeId"));
        Optional<Product> optionalProduct = productRepository.getByName(jsonObject.getString("productName"));
        if(optionalCustomer.isPresent()&&optionalEmployee.isPresent()&&optionalProduct.isPresent()){
            Customer customer = optionalCustomer.get();
            Employee employee = optionalEmployee.get();
            Product product = optionalProduct.get();
            Orders order = new Orders(customer, employee, product, jsonObject.getString("status"), new Date(),
                    Float.parseFloat((String) jsonObject.get("paidValue")), Float.parseFloat((String) jsonObject.get("paidValue")),
                    jsonObject.getString("variety"));
            orderRepository.save(order);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "customer or employee or product do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<Orders> optionalSaleOpportunity = orderRepository.findById(jsonObject.getInt("id"));
        if (optionalSaleOpportunity.isPresent()) {
            Orders order = optionalSaleOpportunity.get();
            Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customerId"));
            Optional<Employee> optionalEmployee = employeeRepository.findById(jsonObject.getInt("employeeId"));
            Optional<Product> optionalProduct = productRepository.getByName(jsonObject.getString("productName"));
            if (optionalCustomer.isPresent() && optionalEmployee.isPresent() && optionalProduct.isPresent()) {
                Customer customer = optionalCustomer.get();
                Employee employee = optionalEmployee.get();
                Product product = optionalProduct.get();
                order.setCustomer(customer);
                order.setEmployee(employee);
                order.setProduct(product);
                order.setDate(new Date());
                order.setPaidValue(Float.parseFloat((String) jsonObject.get("paidValue")));
                order.setValue(Float.parseFloat((String) jsonObject.get("value")));
                order.setStatus(jsonObject.getString("status"));
                order.setVariety(jsonObject.getString("variety"));
                orderRepository.save(order);

                Map<String, String> map = new HashMap<>();
                map.put("message", "success");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> map = new HashMap<>();
            map.put("message", "customer or employee or product did not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "order did not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "delete")
    public ResponseEntity<Map<String, String>> delete(@RequestBody JSONObject jsonObject) {
        try{
            jsonObject.getInt("id");
        }
        catch (JSONException e){
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<Orders> optionalOrders = orderRepository.findById(jsonObject.getInt("id"));
        if(optionalOrders.isPresent()){
            orderRepository.delete(optionalOrders.get());

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "orders did not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            jsonObject.getInt("id");
            jsonObject.getInt("customerId");
            jsonObject.getInt("employeeId");
            jsonObject.getString("productName");
            Float.parseFloat((String) jsonObject.get("paidValue"));
            Float.parseFloat((String) jsonObject.get("value"));
            jsonObject.getString("status");
            jsonObject.getString("variety");
        }catch (JSONException e){
            return false;
        }
        return true;
    }
}

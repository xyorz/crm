package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.SaleOpportunity;
import com.example.crm.entity.Product;
import com.example.crm.repository.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static java.util.Arrays.asList;

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
    public ResponseEntity<Map<String ,String>>  add(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        List<Product> products = new ArrayList<>();
        Optional<Customer> optionalCustomer = customerRepository.findByName(jsonObject.getString("customerName"));
        Optional<Employee> optionalEmployee = employeeRepository.findById(jsonObject.getInt("employeeId"));
        List<Object> productIdObj = Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
        List<Integer> productIds = new ArrayList<>();
        for(Object obj : productIdObj)
            productIds.add(Integer.parseInt((String) obj));
        for (Integer id : productIds){
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(!optionalProduct.isPresent()){
                Map<String, String> map = new HashMap<>();
                map.put("message", "product id=" + id + " do not exist");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            products.add(optionalProduct.get());
        }
        if(optionalCustomer.isPresent() && optionalEmployee.isPresent()){
            Customer customer = optionalCustomer.get();
            Employee employee = optionalEmployee.get();
            saleOpportunityRepository.save(new SaleOpportunity(jsonObject.getBoolean("isDeclare"),
                    employee, products, customer, jsonObject.getString("record")));

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message",  "customer or employee do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String ,String>> update(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("id"));
        if (optionalSaleOpportunity.isPresent()) {
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            List<Product> products = new ArrayList<>();
            Optional<Customer> optionalCustomer = customerRepository.findByName(jsonObject.getString("customerName"));
            Optional<Employee> optionalEmployee = employeeRepository.findById(jsonObject.getInt("employeeId"));
            List<Object> productIdObj = Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
            List<Integer> productIds = new ArrayList<>();
            for(Object obj : productIdObj)
                productIds.add(Integer.parseInt((String) obj));
            for (Integer id : productIds){
                Optional<Product> optionalProduct = productRepository.findById(id);
                if(!optionalProduct.isPresent()){
                    Map<String, String> map = new HashMap<>();
                    map.put("message", "product id=" + id + " do not exist");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
                }
                products.add(optionalProduct.get());
            }
            if (optionalCustomer.isPresent() && optionalEmployee.isPresent()) {
                Customer customer = optionalCustomer.get();
                Employee employee = optionalEmployee.get();
                saleOpportunity.setCustomer(customer);
                saleOpportunity.setEmployee(employee);
                saleOpportunity.setDeclare(jsonObject.getBoolean("isDeclare"));
                saleOpportunity.setProducts(products);
                saleOpportunity.setRecord(jsonObject.getString("record"));
                saleOpportunityRepository.save(saleOpportunity);

                Map<String, String> map = new HashMap<>();
                map.put("message", "success");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> map = new HashMap<>();
            map.put("message", "customer or employee do not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "saleOpportunity did not exist");
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

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("id"));
        if(optionalSaleOpportunity.isPresent()){
            saleOpportunityRepository.delete(optionalSaleOpportunity.get());

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "saleOpportunity did not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            jsonObject.getInt("id");
            jsonObject.getString("customerName");
            jsonObject.getInt("employeeId");
            List<Object> productIdObj = Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
            for(Object obj : productIdObj)
                Integer.parseInt((String) obj);
            jsonObject.getBoolean("isDeclare");
            jsonObject.getString("record");
        }catch (JSONException e){
            return false;
        }
        return true;
    }
}

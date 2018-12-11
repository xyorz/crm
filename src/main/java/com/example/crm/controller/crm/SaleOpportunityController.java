package com.example.crm.controller.crm;

import com.example.crm.entity.*;
import com.example.crm.repository.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private FollowUpRecordRepository followUpRecordRepository;

    @GetMapping("")
    public ModelAndView saleOpportunity(){
        ModelAndView mav = new ModelAndView("sale_opportunity");
        mav.addObject("saleOpportunities", saleOpportunityRepository.findAll());
        return mav;
    }

    @GetMapping("my_opp")
    public ModelAndView mySaleOpp(@RequestParam Integer employeeId){
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        ModelAndView mav = new ModelAndView("my_sale_opp");
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            List<SaleOpportunity> manageSaleOpportunities = saleOpportunityRepository.findAllByEmployeeAndIsDeclareFalse(employee);
            List<SaleOpportunity> findSaleOpportunities = saleOpportunityRepository.findAllByFindEmployee(employee);
            mav.addObject("manageSaleOpportunities", manageSaleOpportunities);
            mav.addObject("findSaleOpportunities", findSaleOpportunities);
        }
        return mav;
    }

    @GetMapping(path = "gain")
    public ResponseEntity<Map<String, String>> gain(@RequestParam Integer id, @RequestParam Integer employeeId){
        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(id);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(optionalSaleOpportunity.isPresent() && optionalEmployee.isPresent()){
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            Employee employee = optionalEmployee.get();
            saleOpportunity.setEmployee(employee);
            saleOpportunity.setFindEmployee(null);
            saleOpportunityRepository.save(saleOpportunity);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "employee or saleOpportunity do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "complete")
    public ResponseEntity<Map<String,String>> complete(@RequestBody JSONObject jsonObject){

        try{
            jsonObject.getInt("saleOpportunityId");
        }
        catch (JSONException e){
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("saleOpportunityId"));
        if(optionalSaleOpportunity.isPresent()) {
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            List<FollowUpRecord> followUpRecords = followUpRecordRepository.findAllBySaleOpportunity(saleOpportunity);
            for (FollowUpRecord f:followUpRecords) {
                if (f.getDeclare()== false){
                    Map<String, String> map = new HashMap<>();
                    map.put("message","Some records haven't been declred");
                    return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
                }
            }
            saleOpportunity.setDeclare(true);
            saleOpportunityRepository.save(saleOpportunity);
            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "saleOpportunity do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String ,String>> add(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        List<Product> products = new ArrayList<>();
        Optional<Customer> optionalCustomer = customerRepository.findByName(jsonObject.getString("customerName"));
        Optional<Employee> optionalFindEmployee = employeeRepository.findById(jsonObject.getInt("findEmployeeId"));
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
        if(optionalCustomer.isPresent() && optionalFindEmployee.isPresent()){
            Customer customer = optionalCustomer.get();
            Employee findEmployee = optionalFindEmployee.get();
            saleOpportunityRepository.save(new SaleOpportunity(false, null, findEmployee, products, customer));

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
        Optional<Employee> optionalFindEmployee = employeeRepository.findById(jsonObject.getInt("findEmployeeId"));
        if (optionalSaleOpportunity.isPresent()) {
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            List<Product> products = new ArrayList<>();
            Optional<Customer> optionalCustomer = customerRepository.findByName(jsonObject.getString("customerName"));
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
            if (optionalCustomer.isPresent() && optionalFindEmployee.isPresent()) {
                Customer customer = optionalCustomer.get();
                Employee findEmployee = optionalFindEmployee.get();
                saleOpportunity.setCustomer(customer);
                saleOpportunity.setEmployee(null);
                saleOpportunity.setDeclare(false);
                saleOpportunity.setProducts(products);
                saleOpportunity.setFindEmployee(findEmployee);
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
            if(jsonObject.get("id")!=null&&jsonObject.get("id")!="")
                jsonObject.getInt("id");
            jsonObject.getString("customerName");
            jsonObject.getInt("findEmployeeId");
            List<Object> productIdObj = Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
            for(Object obj : productIdObj)
                Integer.parseInt((String) obj);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

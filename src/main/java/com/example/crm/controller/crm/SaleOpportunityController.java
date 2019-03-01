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

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ModelAndView saleOpportunity(@SessionAttribute Employee loginEmployee){

        ModelAndView mav = new ModelAndView("sale_opportunity");
        Set<Customer> manageCustomers = loginEmployee.getCustomers();
        Iterable<SaleOpportunity> iterable = saleOpportunityRepository.findAll();
        List<SaleOpportunity> resSaleOpportunities = new ArrayList<>();
        for(SaleOpportunity saleOpportunity : iterable){
            if(manageCustomers.contains(saleOpportunity.getCustomer()))
                resSaleOpportunities.add(saleOpportunity);
            mav.addObject("saleOpportunities", resSaleOpportunities);
        }
        return mav;
    }

    @GetMapping("my_opp")
    public ModelAndView mySaleOpp(@SessionAttribute Employee loginEmployee){
        ModelAndView mav = new ModelAndView("my_sale_opp");
        List<SaleOpportunity> manageSaleOpportunities = saleOpportunityRepository.findAllByEmployeeAndIsDeclareFalse(loginEmployee);
        List<SaleOpportunity> findSaleOpportunities = saleOpportunityRepository.findAllByFindEmployee(loginEmployee);
        mav.addObject("manageSaleOpportunities", manageSaleOpportunities);
        mav.addObject("findSaleOpportunities", findSaleOpportunities);
        return mav;
    }

    @GetMapping(path = "gain")
    public ResponseEntity<Map<String, String>> gain(@RequestParam Integer id, @SessionAttribute Employee loginEmployee){
        Map<String, String> responseMap = new HashMap<>();
        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(id);
        if(optionalSaleOpportunity.isPresent()){
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();

            if(!loginEmployee.getCustomers().contains(saleOpportunity.getCustomer())){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            if(saleOpportunityRepository.findAllByEmployeeAndIsDeclareFalse(loginEmployee).size()>=3){
                responseMap.put("message", "同时获取的销售机会最多为3个");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            saleOpportunity.setEmployee(loginEmployee);
            saleOpportunity.setFindEmployee(null);
            saleOpportunityRepository.save(saleOpportunity);

            responseMap.put("message", "获取成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        responseMap.put("message", "客户或员工不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "complete")
    public ResponseEntity<Map<String,String>> complete(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee){
        Map<String, String> responseMap = new HashMap<>();
        try{
            jsonObject.getInt("saleOpportunityId");
        }
        catch (JSONException e) {
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("saleOpportunityId"));
        if(optionalSaleOpportunity.isPresent()) {
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();

            if(!loginEmployee.getCustomers().contains(saleOpportunity.getCustomer())){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            List<FollowUpRecord> followUpRecords = followUpRecordRepository.findAllBySaleOpportunity(saleOpportunity);
            for (FollowUpRecord f:followUpRecords) {
                if (!f.getDeclare()){
                    responseMap.put("message","存在未申报的记录");
                    return new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
                }
            }
            saleOpportunity.setDeclare(true);
            saleOpportunityRepository.save(saleOpportunity);
            responseMap.put("message", "成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        responseMap.put("message", "销售机会不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String ,String>> add(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)) {
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        List<Product> products = new ArrayList<>();
        Optional<Customer> optionalCustomer = customerRepository.findByName(jsonObject.getString("customerName"));
        Optional<Employee> optionalFindEmployee = employeeRepository.findById(jsonObject.getInt("findEmployeeId"));
        List<Object> productIdObj = Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
        List<String> productNames = new ArrayList<>();
        for(Object obj : productIdObj)
            productNames.add((String) obj);
        for (String pruductName : productNames){
            Optional<Product> optionalProduct = productRepository.findByVariety(pruductName);
            if(!optionalProduct.isPresent()){
                responseMap.put("message", "产品 " + pruductName + " 不存在");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
            products.add(optionalProduct.get());
        }
        if(optionalCustomer.isPresent() && optionalFindEmployee.isPresent()){
            Customer customer = optionalCustomer.get();

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            Employee findEmployee = optionalFindEmployee.get();
            saleOpportunityRepository.save(new SaleOpportunity(false, null, findEmployee, products, customer));

            responseMap.put("message", "添加成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        responseMap.put("message",  "客户或员工不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String ,String>> update(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)) {
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("id"));
        Optional<Employee> optionalFindEmployee = employeeRepository.findById(jsonObject.getInt("findEmployeeId"));
        if (optionalSaleOpportunity.isPresent()) {
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            List<Product> products = new ArrayList<>();
            Optional<Customer> optionalCustomer = customerRepository.findByName(jsonObject.getString("customerName"));
            List<Object> productIdObj = Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
            List<String> productNames = new ArrayList<>();
            for(Object obj : productIdObj)
                productNames.add((String) obj);
            for (String productName : productNames){
                Optional<Product> optionalProduct = productRepository.findByVariety(productName);
                if(!optionalProduct.isPresent()){
                    responseMap.put("message", "产品 " + productNames + " 不存在");
                    return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
                }
                products.add(optionalProduct.get());
            }
            if (optionalCustomer.isPresent() && optionalFindEmployee.isPresent()) {
                Customer customer = optionalCustomer.get();

                if(!loginEmployee.getCustomers().contains(customer)){
                    responseMap.put("message", "没有权限");
                    return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
                }

                Employee findEmployee = optionalFindEmployee.get();
                saleOpportunity.setCustomer(customer);
                saleOpportunity.setEmployee(null);
                saleOpportunity.setDeclare(false);
                saleOpportunity.setProducts(products);
                saleOpportunity.setFindEmployee(findEmployee);
                saleOpportunityRepository.save(saleOpportunity);

                responseMap.put("message", "修改成功");
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
            responseMap.put("message", "客户或员工不存在");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        responseMap.put("message", "销售机会不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "delete")
    public ResponseEntity<Map<String, String>> delete(@RequestBody JSONObject jsonObject,
                                                      @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        try{
            jsonObject.getInt("id");
        }
        catch (JSONException e){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("id"));
        if(optionalSaleOpportunity.isPresent()){
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();

            if(!loginEmployee.getCustomers().contains(saleOpportunity.getCustomer())){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            saleOpportunityRepository.delete(saleOpportunity);

            responseMap.put("message", "删除成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        responseMap.put("message", "销售机会不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            if(jsonObject.get("id")!=null&&jsonObject.get("id")!="")
                jsonObject.getInt("id");
            jsonObject.getString("customerName");
            jsonObject.getInt("findEmployeeId");
            Arrays.asList(jsonObject.getJSONArray("productIds").toArray());
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

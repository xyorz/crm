package com.example.crm.controller.crm;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ModelAndView orders(@SessionAttribute Employee loginEmployee){
        ModelAndView mav = new ModelAndView("order");
        Set<Customer> manageCustomers = loginEmployee.getCustomers();
        Iterable<Orders> iterable = orderRepository.findAll();
        List<Orders> resOrders = new ArrayList<>();
        for(Orders order : iterable){
            if(manageCustomers.contains(order.getCustomer()))
                resOrders.add(order);
        }
        mav.addObject("orders", resOrders);
        return mav;
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String, String>> add(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "数据错误");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customerId"));
        Optional<Product> optionalProduct = productRepository.findById(jsonObject.getInt("productId"));
        if(optionalCustomer.isPresent()&&optionalProduct.isPresent()){
            Customer customer = optionalCustomer.get();

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限新增该用户的订单");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            Product product = optionalProduct.get();
            float value = Float.parseFloat((String) jsonObject.get("value"));
            float paidValue = Float.parseFloat((String) jsonObject.get("paidValue"));
            if(paidValue > value){
                responseMap.put("message", "已付金额不能大于总金额");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
            // 如果总金额 == 已付金额，订单状态改为完成
            boolean status = paidValue == value;
            Orders order = new Orders(customer, loginEmployee, product, status,
                    false, new Date(),
                    value , paidValue, jsonObject.getString("record"));
            orderRepository.save(order);

            responseMap.put("message", "添加成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        responseMap.put("message", "客户或员工或产品不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)) {
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<Orders> optionalSaleOpportunity = orderRepository.findById(jsonObject.getInt("id"));
        if (optionalSaleOpportunity.isPresent()) {
            Orders order = optionalSaleOpportunity.get();
            Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customerId"));
            Optional<Product> optionalProduct = productRepository.findById(jsonObject.getInt("productId"));
            if (optionalCustomer.isPresent() && optionalProduct.isPresent()) {
                Customer customer = optionalCustomer.get();

                if(!loginEmployee.getCustomers().contains(customer)){
                    responseMap.put("message", "没有权限修改该用户的订单");
                    return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
                }

                Product product = optionalProduct.get();

                float value = Float.parseFloat((String) jsonObject.get("value"));
                float paidValue = Float.parseFloat((String) jsonObject.get("paidValue"));
                if(paidValue > value){
                    responseMap.put("message", "已付金额不能大于总金额");
                    return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
                }
                order.setCustomer(customer);
                order.setEmployee(loginEmployee);
                order.setProduct(product);
//                order.setDate(new Date());
                order.setPaidValue(paidValue);
                order.setValue(Float.parseFloat((String) jsonObject.get("value")));
                order.setStatus(paidValue == value);
                order.setReceiptStatus(jsonObject.getBoolean("receiptStatus"));
                order.setRecord(jsonObject.getString("record"));
                orderRepository.save(order);

                responseMap.put("message", "修改成功");
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
            responseMap.put("message", "客户或员工或产品不存在");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        responseMap.put("message", "订单不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "delete")
    public ResponseEntity<Map<String, String>> delete(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        try{
            jsonObject.getInt("id");
        }
        catch (JSONException e){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<Orders> optionalOrders = orderRepository.findById(jsonObject.getInt("id"));
        if(optionalOrders.isPresent()){

            Orders order = optionalOrders.get();

            if(!loginEmployee.getCustomers().contains(order.getCustomer())){
                responseMap.put("message", "没有权限删除该用户的订单");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            if(order.getPaidValue()!=0){
                responseMap.put("message", "订单已经进行了支付，不能删除");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            orderRepository.delete(order);

            Map<String, String> map = new HashMap<>();
            map.put("message", "删除成功");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "订单不存在");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            if(jsonObject.get("id")!=null && jsonObject.get("id")!="")
                jsonObject.getInt("id");
            jsonObject.getInt("customerId");
//            jsonObject.getInt("employeeId");
            jsonObject.getInt("productId");
            Float.parseFloat((String) jsonObject.get("paidValue"));
            Float.parseFloat((String) jsonObject.get("value"));
//            jsonObject.getBoolean("status");
            jsonObject.getBoolean("receiptStatus");
            jsonObject.getString("record");
        }catch (Exception e){
//            e.printStackTrace();
            return false;
        }
        return true;
    }
}

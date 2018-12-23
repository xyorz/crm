package com.example.crm.controller.crm;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.SaleOpportunity;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.EmployeeRepository;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(path = "customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView customers(@SessionAttribute Employee loginEmployee){

        ModelAndView mav = new ModelAndView("customer");
        Iterable<Customer> iterable = loginEmployee.getCustomers();
        mav.addObject("customers", iterable);
        return mav;
//        return new ModelAndView("index");
    }

    @GetMapping("share")
    public ModelAndView share(@SessionAttribute Employee loginEmployee){
        ModelAndView mav = new ModelAndView("customer_share");
        Iterable<Employee> employees = employeeRepository.findAll();
        Set<Customer> customerList = loginEmployee.getCustomers();
        mav.addObject("employees", employees);
        mav.addObject("manageCustomers", customerList);
        return mav;
    }

    @GetMapping("shareCustomer")
    public ResponseEntity<Map<String, String>> shareCustomer(@RequestParam Integer customerId, @RequestParam Integer employeeId,
                                                             @SessionAttribute Employee loginEmployee){
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Map<String, String> responseMap = new HashMap<>();
        if(optionalEmployee.isPresent() && optionalCustomer.isPresent()){
            Employee employee = optionalEmployee.get();
            Customer customer = optionalCustomer.get();

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有共享权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            Set<Customer> manageCustomers = employee.getCustomers();
            if(!manageCustomers.contains(customer)){
//                Set<Customer> listCustomer = employee.getCustomers();
                manageCustomers.add(customer);
                employee.setCustomers(manageCustomers);
                employeeRepository.save(employee);

                Map<String, String> map = new HashMap<>();
                map.put("message", "共享成功");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            responseMap.put("message", "该员工已经享有该客户");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        String message = "共享失败";
        if(!optionalEmployee.isPresent()) message = "共享的员工不存在";
        else if(loginEmployee==null) message = "登陆信息错误";
        responseMap.put("message", message);
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("analysis")
    public String analysis(){
        return "customer_analysis";
    }

    @GetMapping("list")
    public ResponseEntity<Map<String, Object>> list(@SessionAttribute Employee loginEmployee){
        Iterable<Customer> iterable = loginEmployee.getCustomers();
        List<Map<String, String>> cusInfoList = new ArrayList<>();
        for(Customer cus : iterable){
            Map<String, String> cusMap = new HashMap<>();
            cusMap.put("id", cus.getId().toString());
            cusMap.put("name", cus.getName());
            cusMap.put("tel", cus.getTel());
            cusInfoList.add(cusMap);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("value", cusInfoList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String, String>> add(@RequestBody Customer customer, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();

        if(!dataCheck(customer)){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        if (customerRepository.findByName(customer.getName()).isPresent()){
            responseMap.put("message", "客户名已存在");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        // 初始积分为0
        customer.setCredit(0);
        customerRepository.save(customer);
        Set<Customer> manageCustomers = loginEmployee.getCustomers();
        manageCustomers.add(customer);
        loginEmployee.setCustomers(manageCustomers);
        employeeRepository.save(loginEmployee);

        responseMap.put("message", "添加成功");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);

    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Customer customer, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();

        if(!dataCheck(customer)){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if(optionalCustomer.isPresent()){
            Customer cus = optionalCustomer.get();

            // 如果修改了用户名，检查修改的用户名是否已存在
            if(!customer.getName().equals(cus.getName())){
                if(customerRepository.findByName(customer.getName()).isPresent()){
                    responseMap.put("message", "客户名已存在");
                    return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
                }
            }

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限修改该客户的信息");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            cus.setName(customer.getName());
            cus.setTel(customer.getTel());
            cus.setAddress(customer.getAddress());
            // 不允许修改积分
//            cus.setCredit(customer.getCredit());
            cus.setText(customer.getText());
            customerRepository.save(cus);

            responseMap.put("message", "修改成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        responseMap.put("message", "客户不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "delete")
    public ResponseEntity<Map<String, String>> delete(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        try{
            jsonObject.getInt("id");
        }
        catch (JSONException e){
            responseMap.put("message", "数据类型错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("id"));
        if(optionalCustomer.isPresent()){

            Customer customer = optionalCustomer.get();

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限删除该用户");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            customerRepository.delete(customer);

            responseMap.put("message", "删除成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        responseMap.put("message", "客户不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }



    private boolean dataCheck(Customer customer){
        try{
//            if(customer.getId()!=null && jsonObject.get("id")!="")
//                jsonObject.getInt("id");
//            jsonObject.getString("name");
//            jsonObject.getString("tel");
            if(customer.getName().equals("")||customer.getAddress().equals("")||customer.getTel().equals(""))
                return false;
        }catch (Exception e){
            return false;
        }
        return true;
    }


}

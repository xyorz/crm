package com.example.crm.controller.crm;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
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
    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("")
    public ModelAndView customers(){
        ModelAndView mav = new ModelAndView("customer");
        //通过session来获取员工ID
        HttpSession httpSession = httpServletRequest.getSession();
        Integer employeeid =(Integer) httpSession.getAttribute("id");
        mav.addObject("customers",employeeRepository.queryById(employeeid));
        return mav;
    }

    @GetMapping("share")
    public String share(){
        return "customer_share";
    }

    @GetMapping("analysis")
    public String analysis(){
        return "customer_analysis";
    }

    @GetMapping("list")
    public ResponseEntity<Map<String, Object>> list(){
        HttpSession httpSession = httpServletRequest.getSession();
        Integer employeeid =(Integer) httpSession.getAttribute("id");
        Iterable<Customer> iterable = employeeRepository.queryById(employeeid);
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
    public ResponseEntity<Map<String, String>> add(@RequestBody Customer customer) {
        if (customerRepository.findByName(customer.getName()).isPresent()){
            Map<String, String> map = new HashMap<>();
            map.put("message", "customer name already exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        customerRepository.save(customer);

        HttpSession httpSession = httpServletRequest.getSession();
        Integer employeeid =(Integer) httpSession.getAttribute("id");
        Optional<Employee>  employeeOptional = employeeRepository.findById(employeeid);
        Employee employee = employeeOptional.get();
        List<Customer> customerList = employee.getCustomers();
        customerList.add(customer);
        employee.setCustomers(customerList);
        employeeRepository.save(employee);

        Map<String, String> map = new HashMap<>();
        map.put("message", "success");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Customer customer) {

        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if(optionalCustomer.isPresent()){
            Customer cus = optionalCustomer.get();

            // 如果修改了用户名，检查修改的用户名是否已存在
            if(!customer.getName().equals(cus.getName())){
                if(customerRepository.findByName(customer.getName()).isPresent()){
                    Map<String, String> map = new HashMap<>();
                    map.put("message", "customerName already exist");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
                }
            }

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
        try{
            jsonObject.getInt("id");
        }
        catch (JSONException e){
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("id"));
        if(optionalCustomer.isPresent()){
            customerRepository.delete(optionalCustomer.get());

            HttpSession httpSession = httpServletRequest.getSession();
            Integer employeeid =(Integer) httpSession.getAttribute("id");
            Optional<Employee>  employeeOptional = employeeRepository.findById(employeeid);
            Employee employee = employeeOptional.get();
            List<Customer> customerList = employee.getCustomers();
            customerList.remove(optionalCustomer.get());
            employee.setCustomers(customerList);
            employeeRepository.save(employee);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "customer did not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            if(jsonObject.get("id")!=null && jsonObject.get("id")!="")
                jsonObject.getInt("id");
            jsonObject.getString("name");
            jsonObject.getString("tel");
        }catch (Exception e){
            return false;
        }
        return true;
    }


}

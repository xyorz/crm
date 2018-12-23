package com.example.crm.controller.crm;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.LinkMan;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.LinkManRepository;
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
@RequestMapping(path = "link_man")
public class LinkManController {
    @Autowired
    private LinkManRepository linkManRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping("")
    public ModelAndView linkMen(@RequestParam Integer customerId, @SessionAttribute Employee loginEmployee){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        ModelAndView mav = new ModelAndView("link_man");
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();

            // 没有查看该用户联系人的权限
            if(!loginEmployee.getCustomers().contains(customer)){
                return mav;
            }

            List<LinkMan> linkMen = customer.getLinkMen();
            mav.addObject("linkMans", linkMen);
            mav.addObject("customerName", customer.getName());
            mav.addObject("customerId", customerId);
            return mav;
        }
        return mav;
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String, String>> add(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        LinkMan linkMan = new LinkMan(jsonObject.getString("name"), jsonObject.getString("tel"));
        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customer_id"));
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            List<LinkMan> linkmen = customer.getLinkMen();
            linkmen.add(linkMan);
            customer.setLinkMen(linkmen);
            linkManRepository.save(linkMan);
            customerRepository.save(customer);

            responseMap.put("message", "添加成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        responseMap.put("message", "客户不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)){
            responseMap.put("message", "数据错误");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<LinkMan> optionalLinkMan = linkManRepository.findById(jsonObject.getInt("id"));
        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customer_id"));
        if(optionalLinkMan.isPresent() && optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();

            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            LinkMan lkm = optionalLinkMan.get();
            lkm.setName(jsonObject.getString("name"));
            lkm.setTel(jsonObject.getString("tel"));
            linkManRepository.save(lkm);

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
            jsonObject.getInt("customer_id");
        }
        catch (Exception e){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<LinkMan> optionalLinkMan = linkManRepository.findById(jsonObject.getInt("id"));
        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customer_id"));
        if(optionalLinkMan.isPresent() && optionalCustomer.isPresent()){

            Customer customer = optionalCustomer.get();
            if(!loginEmployee.getCustomers().contains(customer)){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            linkManRepository.delete(optionalLinkMan.get());

            responseMap.put("message", "删除成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        responseMap.put("message", "联系人不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            if(jsonObject.get("id")!=null && jsonObject.get("id")!="")
                jsonObject.getInt("id");
            jsonObject.getInt("customer_id");
            jsonObject.getString("name");
            jsonObject.getString("tel");
            if(jsonObject.getString("name").equals("")||jsonObject.getString("tel").equals(""))
                return false;
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

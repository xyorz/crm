package com.example.crm.controller.crm;

import com.example.crm.entity.Customer;
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
    public ModelAndView linkMen(@RequestParam Integer customerId){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        ModelAndView mav = new ModelAndView("link_man");
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            List<LinkMan> linkMen = customer.getLinkMen();
            mav.addObject("linkMans", linkMen);
            mav.addObject("customerId", customerId);
            return mav;
        }
        return mav;
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String, String>> add(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)){
            Map<String, String> map = new HashMap<>();
            map.put("message", "error data type");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        LinkMan linkMan = new LinkMan(jsonObject.getString("name"), jsonObject.getString("tel"));
        Optional<Customer> optionalCustomer = customerRepository.findById(jsonObject.getInt("customer_id"));
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            List<LinkMan> linkmen = customer.getLinkMen();
            linkmen.add(linkMan);
            customer.setLinkMen(linkmen);
            linkManRepository.save(linkMan);
            customerRepository.save(customer);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "linkman do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody JSONObject jsonObject) {
        if(!jsonDataCheck(jsonObject)){
                Map<String, String> map = new HashMap<>();
                map.put("message", "error data type");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<LinkMan> optionalLinkMan = linkManRepository.findById(jsonObject.getInt("id"));
        if(optionalLinkMan.isPresent()){
            LinkMan lkm = optionalLinkMan.get();
            lkm.setName(jsonObject.getString("name"));
            lkm.setTel(jsonObject.getString("tel"));
            linkManRepository.save(lkm);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "linkman do not exist");
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

        Optional<LinkMan> optionalLinkMan = linkManRepository.findById(jsonObject.getInt("id"));
        if(optionalLinkMan.isPresent()){
            linkManRepository.delete(optionalLinkMan.get());

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "linkman did not exist");
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

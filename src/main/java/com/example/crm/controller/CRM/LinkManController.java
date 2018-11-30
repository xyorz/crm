package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.LinkMan;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.LinkManRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            return mav;
        }
        return mav;
    }

    @PostMapping(path = "")
    public ModelAndView addLinkMan(@RequestParam String name, @RequestParam String tel, @RequestParam Integer customerId) {
        LinkMan linkMan = new LinkMan(name, tel);
        linkManRepository.save(linkMan);
        return linkMen(customerId);
    }

    @PostMapping(path = "update")
    public ModelAndView update(@RequestParam Integer id, @RequestParam String name, @RequestParam String tel,
                               @RequestParam Integer customerId) {
        Optional<LinkMan> optionalLinkMan = linkManRepository.findById(id);
        if(optionalLinkMan.isPresent()){
            LinkMan linkMan = optionalLinkMan.get();
            linkMan.setName(name);
            linkMan.setTel(tel);
            linkManRepository.save(linkMan);
        }
        return linkMen(customerId);
    }
}

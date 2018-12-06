package com.example.crm.controller.crm;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.FollowUpRecord;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.EmployeeRepository;
import com.example.crm.repository.FollowUpRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping(path = "followup")
public class FollowUpRecordController {
    @Autowired
    FollowUpRecordRepository followUpRecordRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView followUpRecord(@RequestParam Integer id){
        ModelAndView  mav = new ModelAndView("sale_record");
        mav.addObject("followUpRecords",followUpRecordRepository.findAll());
        return mav;
    }

    @PostMapping("")
    public ModelAndView addRecord(@RequestParam Integer customerId,
                                  @RequestParam Integer employeeId,
                                  @RequestParam String record,
                                  @RequestParam boolean isDeclare){
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(optionalCustomer.isPresent()&&optionalEmployee.isPresent()){
            Customer customer = optionalCustomer.get();
            Employee employee = optionalEmployee.get();
            FollowUpRecord followUpRecord = new FollowUpRecord(employee,customer,record,isDeclare);
            followUpRecordRepository.save(followUpRecord);

        }
        return followUpRecord(0);
    }
}

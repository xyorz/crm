package com.example.crm.controller.crm;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.FollowUpRecord;
import com.example.crm.entity.SaleOpportunity;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.EmployeeRepository;
import com.example.crm.repository.FollowUpRecordRepository;
import com.example.crm.repository.SaleOpportunityRepository;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.zip.DataFormatException;

@Controller
@RequestMapping(path = "followup")
public class FollowUpRecordController {
    @Autowired
    private FollowUpRecordRepository followUpRecordRepository;
    @Autowired
    private SaleOpportunityRepository saleOpportunityRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView followUpRecord(@RequestParam Integer saleOpportunityId){
        ModelAndView  mav = new ModelAndView("sale_record");
        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(saleOpportunityId);
        if(optionalSaleOpportunity.isPresent()){
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            List<FollowUpRecord> followUpRecords = followUpRecordRepository.findAllBySaleOpportunity(saleOpportunity);
            mav.addObject("saleOpportunityId", saleOpportunity.getId());
            mav.addObject("followUpRecord", followUpRecords);
        }
        return mav;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> add(@RequestBody JSONObject jsonObject) throws ParseException {
        if(!jsonDataCheck(jsonObject)){
            Map<String, String> map = new HashMap<>();
            map.put("message", "data type error");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("saleOpportunityId"));
        if(optionalSaleOpportunity.isPresent()){
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();
            String dateStr = jsonObject.getString("date");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            FollowUpRecord followUpRecord = new FollowUpRecord(saleOpportunity, jsonObject.getString("record"), date, false);
            followUpRecordRepository.save(followUpRecord);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "saleOpportunity do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("update")
    public ResponseEntity<Map<String, String>> update(@RequestBody FollowUpRecord followUpRecord){

        Optional<FollowUpRecord> optionalUpRecord = followUpRecordRepository.findById(followUpRecord.getId());
        if(optionalUpRecord.isPresent()){
            FollowUpRecord fur = optionalUpRecord.get();
            fur.setDate(followUpRecord.getDate());
            fur.setDeclare(false);
            fur.setRecord(followUpRecord.getRecord());
            followUpRecordRepository.save(fur);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "followup did not exist");
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

        Optional<FollowUpRecord> optionalUpRecord = followUpRecordRepository.findById(jsonObject.getInt("id"));
        if(optionalUpRecord.isPresent()){
            followUpRecordRepository.delete(optionalUpRecord.get());

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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.parse(jsonObject.getString("date"));
            jsonObject.getString("record");
            jsonObject.getInt("saleOpportunityId");
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

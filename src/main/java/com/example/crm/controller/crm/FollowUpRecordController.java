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
import java.util.*;

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
    public ResponseEntity<Map<String, String>> add(@RequestBody JSONObject jsonObject,
                                                   @SessionAttribute Employee loginEmployee) throws ParseException {
        Map<String, String> responseMap = new HashMap<>();
        if(!jsonDataCheck(jsonObject)){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<SaleOpportunity> optionalSaleOpportunity = saleOpportunityRepository.findById(jsonObject.getInt("saleOpportunityId"));
        if(optionalSaleOpportunity.isPresent()){
            SaleOpportunity saleOpportunity = optionalSaleOpportunity.get();

            if(!loginEmployee.getCustomers().contains(saleOpportunity.getCustomer())){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            String dateStr = jsonObject.getString("date");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            FollowUpRecord followUpRecord = new FollowUpRecord(saleOpportunity, jsonObject.getString("record"), date, false);
            followUpRecordRepository.save(followUpRecord);

            responseMap.put("message", "添加成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        responseMap.put("message", "销售机会编号已存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("update")
    public ResponseEntity<Map<String, String>> update(@RequestBody FollowUpRecord followUpRecord,
                                                      @SessionAttribute Employee loginEmployee){
        Map<String, String> responseMap = new HashMap<>();
        Optional<FollowUpRecord> optionalUpRecord = followUpRecordRepository.findById(followUpRecord.getId());
        if(optionalUpRecord.isPresent()){
            FollowUpRecord fur = optionalUpRecord.get();

            if(!loginEmployee.getCustomers().contains(fur.getSaleOpportunity().getCustomer())){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            fur.setDate(followUpRecord.getDate());
            fur.setDeclare(false);
            fur.setRecord(followUpRecord.getRecord());
            followUpRecordRepository.save(fur);

            responseMap.put("message", "修改成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "销售机会不存在");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @PostMapping(path = "declare")
    public ResponseEntity<Map<String, String>> delete(@RequestBody JSONObject jsonObject, @SessionAttribute Employee loginEmployee) {
        Map<String, String> responseMap = new HashMap<>();
        try{
            jsonObject.getInt("id");
        }
        catch (JSONException e){
            responseMap.put("message", "数据错误");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<FollowUpRecord> optionalUpRecord = followUpRecordRepository.findById(jsonObject.getInt("id"));
        if(optionalUpRecord.isPresent()){

            FollowUpRecord followUpRecord = optionalUpRecord.get();

            if(!loginEmployee.getCustomers().contains(followUpRecord.getSaleOpportunity().getCustomer())){
                responseMap.put("message", "没有权限");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }

            followUpRecord.setDeclare(true);

            followUpRecordRepository.save(followUpRecord);

            responseMap.put("message", "记录提交成功，等待审核");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }

        responseMap.put("message", "客户不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    private boolean jsonDataCheck(JSONObject jsonObject){
        try{
            if(jsonObject.get("id")!=null && jsonObject.get("id")!="")
                jsonObject.getInt("id");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.parse(jsonObject.getString("date"));
            jsonObject.getString("record");
            jsonObject.getInt("saleOpportunityId");
            if(jsonObject.getString("record").equals(""))
                return false;
        }catch (Exception e){
            return false;
        }
        return true;
    }

//    private boolean dataCheck(FollowUpRecord followUpRecord){
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////            sdf.parse(followUpRecord.getDate());
//        }catch (Exception e){
//
//        }
//
////        if(followUpRecord.)
//    }
}

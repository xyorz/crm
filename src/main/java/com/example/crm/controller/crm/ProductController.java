package com.example.crm.controller.crm;

import com.example.crm.entity.Product;
import com.example.crm.repository.OrderRepository;
import com.example.crm.repository.ProductRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
@RequestMapping(path = "product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("")
    public ModelAndView products(@SessionAttribute Integer level){
        ModelAndView mav = new ModelAndView();
        if(level == 0) mav.setViewName("product");
        else mav.setViewName("product_manage");
        mav.addObject("products", productRepository.findAll());
        return mav;
    }

    @GetMapping("list")
    public ResponseEntity<Map<String, Object>> list(){
        Iterable<Product> iterable = productRepository.findAll();
        List<Map<String, String>> cusInfoList = new ArrayList<>();
        for(Product cus : iterable){
            Map<String, String> proMap = new HashMap<>();
            proMap.put("id", cus.getId().toString());
            proMap.put("variety", cus.getVariety());
            proMap.put("price", Float.toString(cus.getPrice()));
            cusInfoList.add(proMap);
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("value", cusInfoList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @GetMapping("manage")
    public ModelAndView productManage(@RequestParam Integer id, @SessionAttribute Integer level){
        ModelAndView mav = new ModelAndView();
        if(level >= 1) {
            mav.setViewName("product_manage_2");
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(optionalProduct.isPresent()){
                Product product = optionalProduct.get();
                mav.addObject("product", product);
                mav.addObject("orders", orderRepository.findAllByProduct(product));
                return mav;
            }
        }
        else mav.setViewName("product");
        return mav;
    }

    @PostMapping(path = "")
    public ResponseEntity<Map<String, String>> add(@RequestBody Product product, @SessionAttribute Integer level) {
        Map<String, String> responseMap = new HashMap<>();
        if(level < 1){
            responseMap.put("message", "权限不足");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        productRepository.save(product);
        responseMap.put("message", "添加成功");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Product product, @SessionAttribute Integer level) {
        Map<String, String> responseMap = new HashMap<>();

        if(level < 1){
            responseMap.put("message", "权限不足");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        if(optionalProduct.isPresent()){
            Product prod = optionalProduct.get();
            prod.setName(product.getName());
            prod.setAmount(product.getAmount());
            prod.setPrice(product.getPrice());
            prod.setCost(product.getCost());
            prod.setAnalysis(product.getAnalysis());
            productRepository.save(prod);

            responseMap.put("message", "修改成功");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        responseMap.put("message", "产品不存在");
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }
}

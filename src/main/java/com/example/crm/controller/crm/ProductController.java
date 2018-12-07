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
    public ModelAndView products(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer level = (Integer) session.getAttribute("level");
        ModelAndView mav = new ModelAndView();
        if(level == 0) mav.setViewName("product");
        else mav.setViewName("product_manage");
        mav.addObject("products", productRepository.findAll());
        return mav;
    }

    @GetMapping("manage")
    public ModelAndView productManage(@RequestParam Integer id, HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer level = (Integer) session.getAttribute("level");
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
    public ResponseEntity<Map<String, String>> add(@RequestBody Product product) {
        productRepository.save(product);
        Map<String, String> map = new HashMap<>();
        map.put("message", "success");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Product product) {
        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        if(optionalProduct.isPresent()){
            Product prod = optionalProduct.get();
            prod.setName(product.getName());
            prod.setVariety(product.getVariety());
            prod.setAmount(product.getAmount());
            prod.setPrice(product.getPrice());
            prod.setCost(product.getCost());
            prod.setAnalysis(product.getAnalysis());
            productRepository.save(product);

            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "product do not exist");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}

package com.example.crm.controller.crm;

import com.example.crm.entity.Product;
import com.example.crm.repository.ProductRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ModelAndView productManage(@RequestParam String id, HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer level = (Integer) session.getAttribute("level");
        ModelAndView mav = new ModelAndView();
        if(level >= 1) mav.setViewName("product_manage_2");
        else mav.setViewName("product");
        return mav;
    }

//    @PostMapping(path = "")
//    public ModelAndView addProduct(@RequestBody Product product) {
//        productRepository.save(product);
//        return products();
//    }

//    @PostMapping(path = "update")
//    public ModelAndView update(@RequestBody Product product) {
//        Optional<Product> optionalProduct = productRepository.findById(product.getId());
//        if(optionalProduct.isPresent()){
//            Product prod = optionalProduct.get();
//            prod.setName(product.getName());
//            prod.setVariety(product.getVariety());
//            prod.setAmount(product.getAmount());
//            prod.setPrice(product.getPrice());
//            prod.setCost(product.getCost());
//            prod.setAnalysis(product.getAnalysis());
//            productRepository.save(product);
//        }
//        return products();
//    }
}

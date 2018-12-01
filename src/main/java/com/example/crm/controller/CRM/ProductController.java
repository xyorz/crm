package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Product;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(path = "product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView products(){
        ModelAndView mav = new ModelAndView("product");
        mav.addObject("products", productRepository.findAll());
        return mav;
    }

    @PostMapping(path = "")
    public ModelAndView addProduct(@RequestBody Product product) {
        productRepository.save(product);
        return products();
    }

    @PostMapping(path = "update")
    public ModelAndView update(@RequestBody Product product) {
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
        }
        return products();
    }
}

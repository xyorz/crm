package com.example.crm.controller.CRM;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Product;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(path = "/product")
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
    public ModelAndView addProduct(@RequestParam String variety, @RequestParam Integer amount, @RequestParam float price,
                                   @RequestParam float cost, @RequestParam String analysis) {
        Product product = new Product(variety, amount, cost, price, analysis);
        productRepository.save(product);
        return products();
    }

    @PostMapping(path = "update")
    public ModelAndView update(@RequestParam Integer id, @RequestParam String variety, @RequestParam Integer amount,
                               @RequestParam float price, @RequestParam float cost, @RequestParam String analysis) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setVariety(variety);
            product.setAmount(amount);
            product.setPrice(price);
            product.setCost(cost);
            product.setAnalysis(analysis);
            productRepository.save(product);
        }
        return products();
    }
}

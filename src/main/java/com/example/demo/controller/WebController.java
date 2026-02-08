package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {

    private final ProductService productService;

    // Constructor-based injection of the ProductService
    public WebController(ProductService productService) {
        this.productService = productService;
    }

    // Serve the index page with product data
    @GetMapping("/")
    public String homePage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";  // Returning the index.html template
    }
}

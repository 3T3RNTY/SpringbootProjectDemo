package com.example.demo.controller;

import java.util.List;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.service.ProductService;
import com.example.demo.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class WebController {

    private final ProductService productService;
    private final CustomerService customerService;

    // Constructor-based injection of the services
    public WebController(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    // Serve the index page with product data
    @GetMapping("/")
    public String homePage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";  // Returning the index.html template
    }

    // Serve the customer registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";  // Returning the register.html template
    }

    // Handle customer registration form submission
    @PostMapping("/register")
    public String registerCustomer(@ModelAttribute Customer customer, Model model) {
        try {
            customerService.createCustomer(customer);
            model.addAttribute("successMessage", "Customer registered successfully!");
            return "redirect:/";  // Redirect to home page after successful registration
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            model.addAttribute("customer", customer);
            return "register";
        }
    }
}

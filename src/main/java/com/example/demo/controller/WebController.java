package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.service.ProductService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.SellerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final SellerService sellerService;

    // Constructor-based injection of the services
    public WebController(ProductService productService, CustomerService customerService, SellerService sellerService) {
        this.productService = productService;
        this.customerService = customerService;
        this.sellerService = sellerService;
    }

    // Serve the index page with product data
    @GetMapping("/")
    public String homePage(Model model, HttpSession session) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        
        // Check if user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        String userType = (String) session.getAttribute("userType");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("userType", userType);
        }
        
        return "index";  // Returning the index.html template
    }

    // Serve the registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("seller", new Seller());
        return "register";  // Returning the register.html template
    }

    // Handle customer registration form submission
    @PostMapping("/register/customer")
    public String registerCustomer(@ModelAttribute Customer customer, Model model, HttpSession session) {
        try {
            if (customer.getName() == null || customer.getName().isEmpty() || 
                customer.getPassword() == null || customer.getPassword().isEmpty()) {
                model.addAttribute("errorMessage", "All fields are required!");
                model.addAttribute("customer", customer);
                model.addAttribute("seller", new Seller());
                return "register";
            }
            
            customerService.createCustomer(customer);
            // Set session attributes
            session.setAttribute("loggedInUser", customer.getName());
            session.setAttribute("userType", "Customer");
            model.addAttribute("successMessage", "Customer registered successfully! Welcome, " + customer.getName());
            return "redirect:/";  // Redirect to home page after successful registration
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            model.addAttribute("customer", customer);
            model.addAttribute("seller", new Seller());
            return "register";
        }
    }

    // Handle seller registration form submission
    @PostMapping("/register/seller")
    public String registerSeller(@ModelAttribute Seller seller, Model model, HttpSession session) {
        try {
            if (seller.getName() == null || seller.getName().isEmpty() || 
                seller.getPassword() == null || seller.getPassword().isEmpty()) {
                model.addAttribute("errorMessage", "All fields are required!");
                model.addAttribute("seller", seller);
                model.addAttribute("customer", new Customer());
                return "register";
            }
            
            sellerService.createSeller(seller);
            // Set session attributes
            session.setAttribute("loggedInUser", seller.getName());
            session.setAttribute("userType", "Seller");
            model.addAttribute("successMessage", "Seller registered successfully! Welcome, " + seller.getName());
            return "redirect:/";  // Redirect to home page after successful registration
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            model.addAttribute("seller", seller);
            model.addAttribute("customer", new Customer());
            return "register";
        }
    }

    // Serve the login page
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";  // Returning the login.html template
    }

    // Handle login form submission
    @PostMapping("/login")
    public String login(@RequestParam String userType, @RequestParam String name, @RequestParam String password, 
                       Model model, HttpSession session) {
        try {
            if (userType.equals("customer")) {
                Optional<Customer> customer = customerService.loginCustomer(name, password);
                if (customer.isPresent()) {
                    session.setAttribute("loggedInUser", customer.get().getName());
                    session.setAttribute("userType", "Customer");
                    model.addAttribute("successMessage", "Login successful! Welcome, " + customer.get().getName());
                    return "redirect:/";
                } else {
                    model.addAttribute("errorMessage", "Invalid username or password!");
                    return "login";
                }
            } else if (userType.equals("seller")) {
                Optional<Seller> seller = sellerService.loginSeller(name, password);
                if (seller.isPresent()) {
                    session.setAttribute("loggedInUser", seller.get().getName());
                    session.setAttribute("userType", "Seller");
                    model.addAttribute("successMessage", "Login successful! Welcome, " + seller.get().getName());
                    return "redirect:/";
                } else {
                    model.addAttribute("errorMessage", "Invalid username or password!");
                    return "login";
                }
            } else {
                model.addAttribute("errorMessage", "Please select a user type!");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Login failed. Please try again.");
            return "login";
        }
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

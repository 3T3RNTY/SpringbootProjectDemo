package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository userRepository;

    public CustomerService(CustomerRepository UserRepository) {
        this.userRepository = UserRepository;
    }

     public List<Customer> getAllCustomers() {
        return userRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer createCustomer(Customer customer) {
        return userRepository.save(customer);
    }
}

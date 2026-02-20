package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.model.Seller;
import com.example.demo.repository.SellerRepository;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Seller not found"));
    }

    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }
}

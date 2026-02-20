package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    List<Seller> findByNameContaining(String keyword);
    Optional<Seller> findByName(String name);
}

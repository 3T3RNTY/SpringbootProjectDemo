package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsForSeller(String sellerName) {
        return productRepository.findBySeller_Name(sellerName);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product createProductForSeller(Product product, String sellerName) {
        Seller seller = sellerRepository.findByName(sellerName)
            .orElseThrow(() -> new RuntimeException("Seller not found"));
        product.setSeller(seller);
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public void deleteProductForSeller(Long id, String sellerName) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getSeller() == null || product.getSeller().getName() == null ||
            !product.getSeller().getName().equals(sellerName)) {
            throw new RuntimeException("You can only remove your own products");
        }

        productRepository.deleteById(id);
    }
}

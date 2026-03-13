package com.example.demo.dto;

public class SellerDto {
    private Long id;
    private String name;

    public SellerDto() {}

    public SellerDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}


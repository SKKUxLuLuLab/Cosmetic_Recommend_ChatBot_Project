package com.example.lululab.Model;

import java.io.Serializable;

public class Product implements Serializable {

    private String productName;
    private String imageUrl;
    private boolean isScrab;

    public Product(String productName, String imageUrl, Object o) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.isScrab = false;
    }

    // Getter and Setter methods
    public void setProduct(String productName) {
        this.productName = productName;
    }


    public boolean isScrab() {
        return isScrab;
    }

    public void setScrab(boolean isScrab) {
        this.isScrab = isScrab;
    }

    public String getProduct() {
        return productName;
    }

}
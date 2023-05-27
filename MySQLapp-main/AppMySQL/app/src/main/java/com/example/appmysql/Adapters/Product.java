package com.example.appmysql.Adapters;

public class Product {
    private int id;
    private String name, description, image, amount, category;
    private float price;
    private int special_offer;

    public Product() {
    }

    //String category, Boolean special_offer,

    public Product(int id, String name, Float price, String description, int special_offer, String image,String category, String amount) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.special_offer = special_offer;
        this.image = image;
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
//
//    public void setPrice(float price) {
//        this.price = price;
//    }
//
    public int getSpecial_offer() {
        return special_offer;
    }

    public void setSpecial_offer(int special_offer) {
        this.special_offer = special_offer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

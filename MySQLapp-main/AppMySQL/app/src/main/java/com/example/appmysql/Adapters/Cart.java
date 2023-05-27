package com.example.appmysql.Adapters;


public class Cart {
    private int prod_id, user_id, amount;
    private String name;
    private float price;

    public Cart() {
    }

    public Cart(int prod_id, int user_id, String name, Float price, int amount) {
        this.prod_id = prod_id;
        this.user_id = user_id;
        this.price = price;
        this.name = name;
        this.amount = amount;
    }



    public int getId() {
        return prod_id;
    }

    public void setId(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getUsers_id() {
        return user_id;
    }

    public void setUsers_id(int user_id) {
        this.user_id = user_id;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

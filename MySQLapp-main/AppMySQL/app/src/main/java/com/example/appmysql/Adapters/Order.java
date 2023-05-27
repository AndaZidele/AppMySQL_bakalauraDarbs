package com.example.appmysql.Adapters;


public class Order {
    //1)Order: userId, userName, userEmail,
    // //userPhone, userAddress, productsName(String virkne),
    //productsId(StringVrkne),
    // orderPrice, datums, statuss(true/false=piegadats/nepiegadats)

    private int user_id;
    private String user_name, email, phone, address, products_names, prod_ids, datums;
    private float products_price;

    public Order(int user_id, String user_name, String email, String phone, String address, String products_names, String prod_ids, float products_price, String datums) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.products_names = products_names;
        this.prod_ids = prod_ids;
        this.products_price = products_price;
        this.datums = datums;
    }

    public String getDatums() {
        return datums;
    }

    public void setDatums(String datums) {
        this.datums = datums;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProd_names() {
        return products_names;
    }

    public void setProd_names(String products_names) {
        this.products_names = products_names;
    }

    public String getProd_ids() {
        return prod_ids;
    }

    public void setProd_ids(String prod_ids) {
        this.prod_ids = prod_ids;
    }

    public float getPrice() {
        return products_price;
    }

    public void setPrice(float products_price) {
        this.products_price = products_price;
    }
}

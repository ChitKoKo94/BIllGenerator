package com.example.billgenerator;


public class Customer {

    private String name, phone, address;
    private int receiptID;

    public  Customer(String name, String phone, String address){
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setReceiptID(int id){ receiptID = id; }

    public int getReceiptID() { return receiptID; }

}

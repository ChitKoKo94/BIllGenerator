package com.example.billgenerator;

public class Item {
    private String name, qty, price;

    public Item(String Name, String Price, String Qty){
        name = Name;
        price = Price;
        qty = Qty;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }

    public String getTotal(){
        int amt = Integer.parseInt(qty);
        int p = Integer.parseInt(price);
        int total = amt * p;
        return String.valueOf(total);
    }
}

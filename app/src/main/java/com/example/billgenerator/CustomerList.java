package com.example.billgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerList extends AppCompatActivity {

    List<Customer> customerList;
    List<String> list;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        customerList = new ArrayList<>();
        list = new ArrayList<>();
        listView = findViewById(R.id.listItems);

        //dummy data
//        Customer c1 = new Customer("Mg Mg", "0943112275", "21, Membina");
//        Customer c2 = new Customer("Hla Hla", "0943112288", "22, Yew Tee");
//        Customer c3 = new Customer("Soe Soe", "0943112266", "23, Yishun");
//        String data1 = c1.getName() + "  ( " + c1.getPhone() + " )";
//        String data2 = c2.getName() + "  ( " + c2.getPhone() + " )";
//        String data3 = c3.getName() + "  ( " + c3.getPhone() + " )";
//
//        list.add(data1);
//        list.add(data2);
//        list.add(data3);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String add = intent.getStringExtra("address");
        int edit = intent.getIntExtra("id", -1);
        customerList = loadData();

        if (!(name == null || add == null || phone == null)){
            Customer customer = new Customer(name, phone, add);
            if (edit != -1)
                customerList.set(edit, customer);
            else
                customerList.add(customer);

            saveData(customerList);
        }

        for (Customer c : customerList){
            String s = c.getName() + "  ( " + c.getPhone() + " )";
            list.add(s);
        }

        Intent intent1 = new Intent(this, CustomerProfile.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer c = customerList.get(position);
                intent1.putExtra("name", c.getName());
                intent1.putExtra("phone", c.getPhone());
                intent1.putExtra("address", c.getAddress());
                intent1.putExtra("id", position);
                startActivity(intent1);
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private List<Customer> loadData(){
        List<Customer> list;
        Gson gson = new Gson();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String content = pref.getString("list",null);
        Type type = new TypeToken<ArrayList<Customer>>() {}.getType();
        list = gson.fromJson(content, type);

        if (list == null)
            list = new ArrayList<>();

        return list;
    }

    public void saveData(List<Customer> list){
//        File path = getFilesDir();
//        try {
//            FileOutputStream stream = new FileOutputStream(new File(path, "list.txt"));
//            stream.write(list.toString().getBytes());
//            stream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("list",json);
        editor.apply();
    }
}






























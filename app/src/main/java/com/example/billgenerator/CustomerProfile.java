package com.example.billgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CustomerProfile extends AppCompatActivity {

    TextView customerName, phone, address;
    Button btn, generateBtn;
    String cName, cPhone, cAdd;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        customerName = findViewById(R.id.ProfileName);
        phone = findViewById(R.id.ProfilePhone);
        address = findViewById(R.id.ProfileAdd);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        cName = intent.getStringExtra("name");
        cPhone = intent.getStringExtra("phone");
        cAdd = intent.getStringExtra("address");
        customerName.setText(cName);
        phone.setText("Phone:     " + cPhone);
        address.setText("Address:  " + cAdd);

        btn = findViewById(R.id.edit);
        btn.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, AddCustomer.class);
            intent1.putExtra("name", cName);
            intent1.putExtra("phone", cPhone);
            intent1.putExtra("address", cAdd);
            intent1.putExtra("isEdit", true);
            intent1.putExtra("id", id);
            startActivity(intent1);
        });

        generateBtn = findViewById(R.id.generate);
        generateBtn.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, GeneratePDF.class);
            intent2.putExtra("name", cName);
            intent2.putExtra("phone", cPhone);
            intent2.putExtra("address", cAdd);
            intent2.putExtra("id", id);
            startActivity(intent2);
        });
    }
}
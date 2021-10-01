package com.example.billgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddCustomer extends AppCompatActivity {

    EditText name, phone, address;
    Button btn;
    TextView customerName;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        customerName = findViewById(R.id.cName);
        id = -1;
        btn = findViewById(R.id.submit);
        btn.setOnClickListener(v -> {
            name = findViewById(R.id.name);
            phone = findViewById(R.id.phone);
            address = findViewById(R.id.address);

            Intent intent = new Intent(AddCustomer.this, CustomerList.class);
            intent.putExtra("name",name.getText().toString());
            intent.putExtra("phone",phone.getText().toString());
            intent.putExtra("address",address.getText().toString());
            intent.putExtra("id", id);

            name.setText("");
            phone.setText("");
            address.setText("");
            finish();
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isEdit", false)){
            name = findViewById(R.id.name);
            phone = findViewById(R.id.phone);
            address = findViewById(R.id.address);

            customerName.setText("Edit Customer");
            name.setText(intent.getStringExtra("name"));
            phone.setText(intent.getStringExtra("phone"));
            address.setText(intent.getStringExtra("address"));
            id = intent.getIntExtra("id", -1);
        }
    }

}
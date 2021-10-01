package com.example.billgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.shadow.ShadowRenderer;

public class MainActivity extends AppCompatActivity {

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int PERMISSION_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences pre = getSharedPreferences("data", MODE_PRIVATE);
//        SharedPreferences.Editor ed = pre.edit();
//        ed.clear();
//        ed.apply();

        int permissionCode1 = checkSelfPermission(permissions[0]);
        int permissionCode2 = checkSelfPermission(permissions[1]);
        if ( permissionCode1 != PackageManager.PERMISSION_GRANTED ||
                permissionCode2 != PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissions, PERMISSION_REQ_CODE);
        }

        Button viewCustomer = findViewById(R.id.customer);
        viewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomerList.class);
                proceed(intent);
            }
        });

        Button addnew = findViewById(R.id.addnew);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCustomer.class);
                proceed(intent);
            }
        });
    }

    private void proceed(Intent intent){
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQ_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
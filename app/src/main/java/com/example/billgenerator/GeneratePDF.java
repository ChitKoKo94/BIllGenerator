package com.example.billgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GeneratePDF extends AppCompatActivity {

    List<Item> itemList = new ArrayList<>();

    Button btn, addItem;

    String name, custName, phone, address, date;

    EditText item, price, qty, pay1, detail1, pay2, detail2, Remark;

    Bitmap bmp, scaledbmp;

    Date c;

    TextView items;

    private Customer customer;
    private int AddedItem = 0, id;
    private final int pageHt = 1120, pageWt = 792, x = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);

        Intent intent = getIntent();
        custName = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        id = intent.getIntExtra("id", -1);

        c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");
        date = sdf.format(c);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.membina);

        //resize sample picture
        scaledbmp = Bitmap.createScaledBitmap(bmp, 220,100,true);

        //btn to add item to the bill
        addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(v -> addNewItem());

        //btn to generate pdf
        btn = findViewById(R.id.generatePdf);
        btn.setOnClickListener(v -> generatePdf());
    }

    private void addNewItem(){

        items = findViewById(R.id.itemAdded);
        item = findViewById(R.id.itemName);
        price = findViewById(R.id.itemPrice);
        qty = findViewById(R.id.itemQty);

        Item newItem = new Item(item.getText().toString(), price.getText().toString(), qty.getText().toString());
        itemList.add(newItem);
        items.setText("Item Added: " + ++AddedItem);
    }

    private void generatePdf(){
        //draw content on pdf
        drawContent();

        itemList.clear(); //clear the temporary list of number of items

        // clear the form
        AddedItem = 0;

        EditText[] AllText = { pay1, pay2, detail1, detail2, item, qty, price, Remark };
        for (EditText e: AllText)
            e.setText("");

        items = findViewById(R.id.itemAdded);
        items.setText("Item Added: ");
    }

    private void drawContent(){
        Remark = findViewById(R.id.remark);
        pay1 = findViewById(R.id.payment);
        pay2 = findViewById(R.id.payment2);
        detail1 = findViewById(R.id.paymentDetail);
        detail2 = findViewById(R.id.paymentDetail2);

        PdfDocument pdf = new PdfDocument();

        // to draw logo picture
        Paint paint = new Paint();

        // to draw bold text
        Paint title = new Paint();
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(16);
        title.setColor(ContextCompat.getColor(this, R.color.black));

        // tot draw normal text
        Paint text = new Paint();
        text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        text.setTextSize(14);
        text.setColor(ContextCompat.getColor(this, R.color.black));

        // to draw line
        Paint line = new Paint();
        line.setStyle(Paint.Style.STROKE);
        line.setColor(getColor(R.color.teal_700));

        //creating new pdf page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWt, pageHt, 1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // draw logo picture
        canvas.drawBitmap(scaledbmp, x, x, paint);

        // write Header Text
        canvas.drawText("MEMBINA [ Ygn +9595085126, Sg +6581569050 ]", 270, 112, title);
        canvas.drawText("Email : 21jlnmembina@gmail.com", 270, 136, title);

        canvas.drawLine(x, 170, 752, 170, line);

        // get list of customers from shared pref
        List<Customer> list = loadData();
        customer = list.get(id);
        customer.setReceiptID(customer.getReceiptID() + 1); // increment receipt id by 1 to be subsequent from previous id
        list.set(id, customer);
        saveData(list); // save the change

        // get customer name and receipt id
        name = "Bill To: " + customer.getName().toUpperCase();
        String Idx = custName.substring(0,2).toUpperCase() + "00" + customer.getReceiptID();

        // draw receipt id,date and customer info
        canvas.drawText("Receipt ID #" + Idx, 620, 200, title);
        canvas.drawText("Date: " + date, 40, 200, title);
        canvas.drawText(name, x, 224, title);
        canvas.drawText(customer.getAddress(), x, 244, text);
        canvas.drawText(customer.getPhone(), x, 264, text);

        canvas.drawLine(x, 284, 752, 284, line);

        // draw Header for items detail
        canvas.drawText("DESCRIPTION                                                                                  UNIT PRICE(KYATS)     QTY     AMOUNT", 40, 308, title);

        canvas.drawLine(x, 316, 752, 316, line);

        // draw detail of added items
        int Yincrement = 344;
        int Total = 0;
        for(Item i: itemList){
            Total += Integer.parseInt(i.getTotal());

            canvas.drawText(i.getName(), x, Yincrement, text);
            canvas.drawText(i.getPrice(), 500, Yincrement, text);
            canvas.drawText(i.getQty(), 640, Yincrement, text);
            canvas.drawText(i.getTotal(), 686, Yincrement, text);

            Yincrement += 22;
        }

        canvas.drawLine(x, Yincrement - 12, 752, Yincrement - 12, line);

        // draw Overall Total cost
        String total = String.valueOf(Total);
        int len = total.length();
        StringBuffer newStr = new StringBuffer(total);
        if (len > 3){
            newStr.insert(len - 3, ",");
            if (len > 6)
                newStr.insert(len - 6, ",");
        }

        canvas.drawText("TOTAL (KYATS)                       " + newStr, 474, Yincrement + 12, title);

        // Payment mode and detail
        canvas.drawText("PAYMENT MODE" , x, Yincrement + 42, title);
        canvas.drawText(pay1.getText().toString() , x, Yincrement + 62, text);
        canvas.drawText(detail1.getText().toString() , x, Yincrement + 80, text);
        canvas.drawText(pay2.getText().toString() , x, Yincrement + 98, text);
        canvas.drawText(detail2.getText().toString() , x, Yincrement + 116, text);

        // Remarks
        canvas.drawText("REMARK" , x, Yincrement + 136, title);
        String remark = Remark.getText().toString().toUpperCase();
        if (remark.length() < 1)
            remark = "N.A";
        canvas.drawText(remark, x, Yincrement + 156, text);

        pdf.finishPage(page);

        // specify file dir, name and save it to download folder
        savePdf(pdf);

    }

    private void savePdf(PdfDocument pdf){
        String pdfName = customer.getName() + "_" + customer.getReceiptID() + ".pdf";
        File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File dest = new File(file, pdfName);
        try {
            pdf.writeTo(new FileOutputStream(dest));
            Toast.makeText(this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        pdf.close();
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
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("list",json);
        editor.apply();
    }

}
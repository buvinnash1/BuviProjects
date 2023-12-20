package com.example.vinot.cardscanner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class carddetails extends AppCompatActivity {

    TextView dateofbirthView;
    TextView ageView;
    Button savebutton;
    Button viewdatabutton;
    DatabaseHelper myDb;
    int age;
    String dateofbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carddetails);
        dateofbirthView = (TextView) findViewById(R.id.date_of_birth);
        ageView = (TextView) findViewById(R.id.age_view);
        savebutton = (Button) findViewById(R.id.save_button);
        viewdatabutton = (Button) findViewById(R.id.viewdata_button);
        myDb = new DatabaseHelper(this);

        dateofbirth = getIntent().getStringExtra("Details");
        String month = dateofbirth.substring(0, dateofbirth.indexOf("/"));
        String day = dateofbirth.substring(dateofbirth.indexOf("/") + 1, dateofbirth.indexOf("/") + 3);
        String year = dateofbirth.substring(dateofbirth.indexOf("/") + 4, dateofbirth.indexOf("/") + 8);

       Calendar dob = Calendar.getInstance();
       Calendar today = Calendar.getInstance();

       dob.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
       age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        dateofbirthView.setText("DOB: " + dateofbirth);
        ageView.setText("Age: " + age);
        AddData();
        ViewAll();
    }

    public void AddData() {
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   boolean isInserted = myDb.insertData(dateofbirth, Integer.toString(age));
                   if(isInserted = true){
                       Toast.makeText(carddetails.this, "Data Inserted", Toast.LENGTH_LONG).show();
                   }else{
                       Toast.makeText(carddetails.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                   }
            }
        });
    }

    public void ViewAll(){
        viewdatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0){
                    // show message
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Id: " + res.getString(0) + "\n");
                    buffer.append("DOB: " + res.getString(1) + "\n");
                    buffer.append("Age: " + res.getString(2) + "\n\n");
                }
                //show all data
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage (String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

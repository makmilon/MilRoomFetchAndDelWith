package com.milon.paythroughproject;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText t1,t2,t3, t4;
    TextView lbl;
    Button b1,b2;
    Spinner spinner, spinner1;
    String selectedAge, slectGender;
    String inputNo;

    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);

        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
        lbl=findViewById(R.id.lbl);
        spinner=findViewById(R.id.spinner);
        spinner1=findViewById(R.id.spinner1);
        awesomeValidation = new AwesomeValidation(BASIC);

        awesomeValidation.addValidation(this,R.id.t1, "[0-9]", R.string.invalid_id);
        awesomeValidation.addValidation(this,R.id.t4, "[5-9]{1}[0-9]{9}$",R.string.invalid_phone);
        awesomeValidation.addValidation(this,R.id.t2, "[a-zA-Z\\s]+", R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.t3, Patterns.EMAIL_ADDRESS, R.string.invalid_email);


        //age spinner
        List<Integer> age= new ArrayList<>();

        for (int i=1; i<100; i++) {
            age.add(i);
        }

        //style and populate the spinner

        ArrayAdapter<Integer> agAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,age);
        agAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(agAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAge=spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // gender spinner
        List<String> gender= new ArrayList<>();
        gender.add(0,"Select Gender");
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");

        //style and populate the spinner

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slectGender=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAge!="Select Age" && slectGender!="Select Gender"){
                    if (awesomeValidation.validate()){
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                        AppDatabase.class, "room_db")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build();
                        UserDao userDao = db.userDao();
                        Boolean check=userDao.is_exist(Integer.parseInt(t1.getText().toString()));
                        if(check==false) {
                            userDao.insertrecord(new User(Integer.parseInt(t1.getText().toString()), t2.getText().toString(), t4.getText().toString(),t3.getText().toString(),selectedAge,slectGender));
                            t1.setText("");
                            t2.setText("");
                            t3.setText("");
                            t4.setText("");
                            lbl.setText("Data Saved Successfully");
                        }
                        else
                        {
                            t1.setText("");
                            t2.setText("");
                            t3.setText("");
                            t4.setText("");
                            lbl.setText("Record is existing");
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Validation failed", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please Selected Gender Or Age.c ", Toast.LENGTH_LONG).show();
                }



            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FetchData.class));
            }
        });
    }
}
package com.example.covid19trackerapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class userInfo_Activity extends AppCompatActivity {

    private EditText usernum;
    private EditText userdisrict;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);

        usernum=findViewById(R.id.usernum);
        save=findViewById(R.id.save);
        userdisrict=findViewById(R.id.userdistrict);

        userInfo_DB db=new userInfo_DB();

        save.setOnClickListener(v-> {
                String number = usernum.getText().toString().trim();
                String districtnum=userdisrict.getText().toString().trim();
                if (number.length() != 10) {
                    usernum.setError("Invalid number");

                    usernum.requestFocus();
                    return;
                }else if (districtnum.length()!=6){
                    userdisrict.setError("Invalid Pincode");

                    usernum.requestFocus();
                    return;
                }

                userInfo_Adapter user=new userInfo_Adapter(usernum.getText().toString(),
                        userdisrict.getText().toString());

                db.add(user).addOnSuccessListener(suc->{
                    Toast.makeText(this,"Details added",Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->{
                    Toast.makeText(this,""+er.getMessage(),Toast.LENGTH_SHORT).show();
                });
        });

    }}
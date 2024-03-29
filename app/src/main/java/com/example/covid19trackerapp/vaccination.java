package com.example.covid19trackerapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class vaccination extends AppCompatActivity {

    private RecyclerView CenterRecyclerView;
    private EditText PinCodeText;
    private ProgressBar LoadingPB;
    private Button SearchButton;
    private ArrayList<CenterModel> CenterList;
    private CenterRVAdapter centerRVAdapter;
   // private Button enable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);
        //configure recycler view
        CenterRecyclerView = findViewById(R.id.RVcenter);
        //configure form fields
        PinCodeText = findViewById(R.id.PincodeInput);
        //configure loading progress bar
        LoadingPB = findViewById(R.id.SearchProgressBar);
        //configure search button
        SearchButton = findViewById(R.id.ButtonSearch);

       /* enable=findViewById(R.id.enable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }

        });*/

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do validation
                String pincode = PinCodeText.getText().toString().trim();
                if (pincode.length() != 6) {
                    PinCodeText.setError("Invalid Pincode");

                    PinCodeText.requestFocus();
                    return;
                }

                //clear
                if (CenterList != null && CenterList.size() > 0) {
                    CenterList.clear();

                }

                //create date picker listener
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        vaccination.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String DateStr = day + "-" + (month + 1) + "-" + year;
                                GetAppointments(pincode, DateStr);
                            }
                        },
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

     /*   FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        TextView msg = findViewById(R.id.check);
                        msg.setText(token);
                        Log.d(TAG, token);
                        Toast.makeText(vaccination.this, token, Toast.LENGTH_SHORT).show();
                    }
                });*/

    }

    private void openActivity() {
        Intent intent=new Intent(vaccination.this, userInfo_Activity.class);
        startActivity(intent);
    }

    void GetAppointments(String pincode, String date) {
        LoadingPB.setVisibility(View.VISIBLE);
        CenterList = new ArrayList<>();
        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode + "&date=" + date;
        RequestQueue Queue = Volley.newRequestQueue(vaccination.this);
        Request request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray centerArray = response.getJSONArray("centers");
                    LoadingPB.setVisibility(View.GONE);

                    if (centerArray.length() == 0) {
                        Toast.makeText(vaccination.this, "No centers were found", Toast.LENGTH_LONG).show();
                    }

                    for (int i = 0; i < centerArray.length(); i++) {
                        JSONObject jsonObject = centerArray.getJSONObject(i);
                        String CenterName = jsonObject.getString("name");
                        String CenterFromTime = jsonObject.getString("from");
                        String CenterEndTime = jsonObject.getString("to");
                        String CenterAddress = jsonObject.getString("address") + " " + jsonObject.getString("district_name") + " " + jsonObject.getString("state_name");
                        String FeeType = jsonObject.getString("fee_type");

                        JSONObject SessionObj = jsonObject.getJSONArray("sessions").getJSONObject(0);

                        int AvailableCapacity = SessionObj.getInt("available_capacity");
                        String VaccineName = SessionObj.getString("vaccine");
                        int AgeLimit = SessionObj.getInt("min_age_limit");
                        int Dose1 = SessionObj.getInt("available_capacity_dose1");
                        int Dose2 = SessionObj.getInt("available_capacity_dose2");

                        CenterModel Center = new CenterModel(
                                CenterName,
                                CenterAddress,
                                CenterFromTime,
                                CenterEndTime,
                                AgeLimit,
                                FeeType,
                                VaccineName,
                                AvailableCapacity,
                                Dose1,
                                Dose2

                        );


                        //add center model to list

                        CenterList.add(Center);

                    }
                    //pass the list to adapter
                    centerRVAdapter = new CenterRVAdapter();
                    centerRVAdapter.setCenters(CenterList);

                    //add adapter to recycler view
                    CenterRecyclerView.setAdapter(centerRVAdapter);
                    CenterRecyclerView.setLayoutManager(new LinearLayoutManager(vaccination.this));


                } catch (Exception e) {
                    LoadingPB.setVisibility(View.GONE);
                    Toast.makeText(vaccination.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                LoadingPB.setVisibility(View.GONE);
            }
        });
        //add request to the queue
        Queue.add(request);
    }
}

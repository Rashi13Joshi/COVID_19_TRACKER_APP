package com.example.covid19trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19trackerapp.api.CountryData;
import com.example.covid19trackerapp.api.api_utilities;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalactive,totalconfirmed,totaldeaths, totalrecovered,totaltests;
    private TextView todayconfirmed,todaydeaths, todayrecovered, date, cname,population;
    private  TextView more_info,map,vaccine;
    private PieChart pieChart;

    private List<CountryData> list ;
    String country="India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list=new ArrayList<>();

        init();

        try{
        if(getIntent().getStringExtra("country") !=null){
            country = getIntent().getStringExtra("country");
            cname = setText(country);

        }
        }catch (NumberFormatException e){
            System.out.println("Error");
        }

        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Country_case_Activity.class)));


       more_info.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Newsactivity.class)));

        map.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Map.class)));

        vaccine.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, vaccination.class)));



        api_utilities.getapi_Interface().getCountryData()
            .enqueue(new Callback<List<CountryData>>() {
                @Override
                public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                 list.addAll(response.body());

                 for (int i=0;i<list.size();i++){
                     if(list.get(i).getCountry().equals(country)){
                         int confirm=Integer.parseInt(list.get(i).getCases());
                         int active=Integer.parseInt(list.get(i).getActive());
                         int recovered=Integer.parseInt(list.get(i).getRecovered());
                         int deaths=Integer.parseInt(list.get(i).getDeaths());

                         totalactive.setText(NumberFormat.getInstance().format(active));
                         totalconfirmed.setText(NumberFormat.getInstance().format(confirm));
                         totalrecovered.setText(NumberFormat.getInstance().format(recovered));
                         totaldeaths.setText(NumberFormat.getInstance().format(deaths));
                         cname.setText(country);

                         todayrecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                         todaydeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                         todayconfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                         totaltests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));
                         population.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getPopulation())));

                         setText(list.get(i).getUpdated());

                         pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                         pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.red)));
                         pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green_pie)));
                         pieChart.addPieSlice(new PieModel("Deaths", deaths, getResources().getColor(R.color.black)));

                         pieChart.startAnimation();
                     }
                 }
                }

                @Override
                public void onFailure(Call<List<CountryData>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private TextView setText(String updated) {

        DateFormat format=new SimpleDateFormat("dd/MM/yyyy");

        long millisec=Long.parseLong(updated);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millisec);

        date.setText("Updated at "+format.format(calendar.getTime()));
        return null;
    }

    private void init(){
        totalconfirmed=findViewById(R.id.totalconfirmed);
        totalrecovered=findViewById(R.id.totalrecovered);
        totaltests=findViewById(R.id.totaltests);
        totalactive=findViewById(R.id.totalactive);
        totaldeaths=findViewById(R.id.totaldeaths);
        todayrecovered=findViewById(R.id.todayrecovered);
        todayconfirmed=findViewById(R.id.todayconfirmed);
        todaydeaths=findViewById(R.id.todaydeaths);
        date=findViewById(R.id.date);
        pieChart=findViewById(R.id.pieChart);
        cname = findViewById(R.id.cname);
        population=findViewById(R.id.population);
        vaccine=findViewById(R.id.vaccine);
        map=findViewById(R.id.map);
        more_info=findViewById(R.id.more_info);
    }
}
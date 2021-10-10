package com.example.covid19trackerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covid19trackerapp.api.CountryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class County_Adapter extends RecyclerView.Adapter<County_Adapter.Country_View_Holder> {

    private Context context;
    private List<CountryData> list;

    public County_Adapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Country_View_Holder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.countries_layout, parent , false);

        return new Country_View_Holder(view);
    }

    public void filterList(List<CountryData> filterList){
        list=filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull County_Adapter.Country_View_Holder holder, int position) {

        CountryData data= list.get(position);

        holder.country_cases.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));

        holder.country_name.setText(data.getCountry());

        holder.serialno.setText(String.valueOf(position+1));

        Map<String,String> img =data.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.imageView);

        holder.itemView.setOnClickListener(v->{
            Intent intent=new Intent(context, MainActivity.class);
            intent.putExtra("country",data.getCountry());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Country_View_Holder extends RecyclerView.ViewHolder {

        private TextView serialno, country_name, country_cases;
        private ImageView imageView;

        public Country_View_Holder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            serialno=itemView.findViewById(R.id.serialno);
            country_name=itemView.findViewById(R.id.country_name);
            country_cases=itemView.findViewById(R.id.country_cases);
            imageView=itemView.findViewById(R.id.country_flag);
        }
    }
}

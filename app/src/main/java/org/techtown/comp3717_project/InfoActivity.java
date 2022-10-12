package org.techtown.comp3717_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.techtown.comp3717_project.adapters.ItemsMyRecyclerAdapter;

public class InfoActivity extends AppCompatActivity {
    RecyclerView items, services;
    String[] itemNames, serviceNames;
    int[] dutyFreeIcons = {R.drawable.ic_baseline_smoking_rooms_24,
            R.drawable.ic_baseline_local_drink_24, R.drawable.ic_baseline_shopping_cart_24};
    int[] servicesIcons = {R.drawable.ic_baseline_car_rental_24, R.drawable.ic_baseline_hotel_24,
            R.drawable.ic_baseline_local_taxi_24};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String airportName = bundle.getString("Airport");
        TextView name = findViewById(R.id.textViewAirportName);
        name.setText(airportName);
        items = findViewById(R.id.recyclerViewItems);
        services = findViewById(R.id.recyclerViewServices);
        itemNames = getResources().getStringArray(R.array.items);
        serviceNames = getResources().getStringArray(R.array.services);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter1 = new ItemsMyRecyclerAdapter(this,
                itemNames, dutyFreeIcons);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter2 = new ItemsMyRecyclerAdapter(this,
                serviceNames, servicesIcons);
        items.setAdapter(itemsMyRecyclerAdapter1);
        items.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        services.setAdapter(itemsMyRecyclerAdapter2);
        services.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
    }
}
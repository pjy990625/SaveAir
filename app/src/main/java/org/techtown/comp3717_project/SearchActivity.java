package org.techtown.comp3717_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Amadeus amadeus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        amadeus = Amadeus
                .builder(BuildConfig.API_KEY, BuildConfig.API_SECRET)
                .build();

        EditText input = findViewById(R.id.editTextAirportName);
        input.setTextColor(com.google.android.material.R.attr.colorOnSecondary);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        TextView keyword = findViewById(R.id.editTextAirportName);
                        keyword.setTextColor(com.google.android.material.R.attr.colorOnSecondary);
                        getAirports(keyword.getText().toString());
                    } catch (ResponseException e) {
                        Log.d("Amadeus", e.toString());
                    }
                }
            }
        });
    }

    void getAirports(String keyword) throws ResponseException {
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView list = findViewById(R.id.airportList);
        list.setAdapter(adapter);

        Location[] locations = amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", "AIRPORT"));

        for (Location location : locations) {
            Log.d("Android", location.getName());
            adapter.add(location.getName());
        }

        list.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(adapterView.getContext(), InfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Airport", locations[i].getName() + " ("
                    + locations[i].getAddress().getCityCode() + ")");
            bundle.putString("Location", locations[i].getAddress().getCityName() + ", " +
                    locations[i].getAddress().getCountryName());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
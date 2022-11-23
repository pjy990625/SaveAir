package org.techtown.comp3717_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.ItineraryPriceMetric;

import org.techtown.comp3717_project.ui.compare.EnterTicketFragment;
import org.techtown.comp3717_project.ui.compare.ViewTicketFragment;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

public class CompareActivity extends AppCompatActivity {

    private EnterTicketFragment fragmentEnterTicket;
    private ViewTicketFragment fragmentViewTicket;

    final int MAX_HISTORY = 10; // maximum number of records

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        fragmentEnterTicket = new EnterTicketFragment();
        fragmentViewTicket = new ViewTicketFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentEnterTicket)
                .commit();
    }


    public void submitTravelInfo(double price, ItineraryPriceMetric[] results) {
        Bundle bundle = new Bundle(); // create bundle to send result to the fragment
        if (results.length == 0) {
            bundle.putString("isCheaper", "unknown"); // if no result is retrieved then it means no data was found
        } else {
            double medium = Double.parseDouble(results[0].getPriceMetrics()[2].getAmount()); // get the medium average price from the result
            bundle.putString("price", String.valueOf(price));
            bundle.putString("medium", String.valueOf(medium));
            bundle.putString("isCheaper", price < medium ? "true" : "false");
            bundle.putString("currency", Currency.getInstance(results[0].getCurrencyCode()).getSymbol()); // extract and convert the currency code to currency symbol
            saveTicketResult(results[0]);
        }

        fragmentViewTicket.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentViewTicket)
                .commit();
    }

    public EnterTicketFragment getEnterTicketFragment() {
        return fragmentEnterTicket;
    }

    void saveTicketResult(ItineraryPriceMetric result) {
        SharedPreferences prefs = getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> historySet = prefs.getStringSet("history", new HashSet<>());
        ArrayList<String> historyList = new ArrayList<>(historySet); // convert Set to ArrayList
        historyList.add(result.toString());
        while (historyList.size() > MAX_HISTORY) historyList.remove(0); // if 10+ records, delete the oldest (first)
        editor.putStringSet("history", new HashSet<>(historyList)); // convert back to Set and save
        editor.apply();
    }
}
package com.example.galilinetsky.moneywatcher.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.galilinetsky.moneywatcher.SideActivities.AddOrRemove;
import com.example.galilinetsky.moneywatcher.Clients.TCPClient;
import com.example.galilinetsky.moneywatcher.Controllers.IController;
import com.example.galilinetsky.moneywatcher.Controllers.MenuController;
import com.example.galilinetsky.moneywatcher.R;
import com.example.galilinetsky.moneywatcher.SideActivities.Settings;
import com.example.galilinetsky.moneywatcher.SideActivities.createCategories;
import com.example.galilinetsky.moneywatcher.SideActivities.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private IController _controller;
    private TCPClient _client;
    private String OnGotMassege;
    private int monthlyLimit;
    private String coin;
    private boolean notifyOn;
    private Button exl;
    private Set<String> categories;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String LIMIT = "limit";
    private static final String COIN_TYPE = "coin";
    private static final String CATEGORY_ARRAY = "categories";
    private static final String[] items = new String[]{"Food", "Entertaiment", "Cleaning", "Transportation", "Electricity",
            "Water", "Phone", "Internet", "Car"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthlyLimit = loadData();
        categories = loadCategories();
        setLim();
        //from menu bar
        Intent incoming = getIntent();
        coin = incoming.getStringExtra("coin type");
        String tmp = incoming.getStringExtra("limit sum");
        if (tmp != null) {
            monthlyLimit = Integer.parseInt(tmp);
            setLim();
            ;
        }
        if (coin != null) {
            setLim();
        } else {
            coin = loadCoinnType();
        }
        _controller = new MenuController(this, MainActivity.this);
        float monthSum = ((MenuController) _controller).monthExpense();
        ((MenuController) _controller).dailyExpense(coin);
        //notify if spent above limit
        if (monthSum >= monthlyLimit) {
            ((MenuController) _controller).setNotifyOn(true);
            _controller.updateView();
        }
        exl = (Button) findViewById(R.id.csv);
        exl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuController) _controller).createXL();
            }
        });
        Intent getCtg = getIntent();
        ArrayList<String> checkArray = getCtg.getStringArrayListExtra("Categories array");
        if (checkArray != null) {
            if (checkArray.size() != 0) {
                categories = new HashSet<String>(checkArray);
                setLim();
            }
           /* else{
                categories.addAll(Arrays.asList(items));
                setLim();
            }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent setIntent;
        switch (item.getItemId()) {
            case R.id.notification:
                setIntent = new Intent(this, notification.class);
                setIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(setIntent);
                return true;
            case R.id.settings:
                setIntent = new Intent(this, Settings.class);
                setIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(setIntent);
                return true;
            case R.id.categorySet:
                setIntent = new Intent(this, createCategories.class);
                startActivity(setIntent);
                return true;
            case R.id.add_category:
                setIntent = new Intent(this, AddOrRemove.class);
                startActivity(setIntent);
                return true;
        }
        return true;
    }

    public void setLim() {
        Thread thread = new Thread() {
            public void run() {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(LIMIT, monthlyLimit);
                editor.putString(COIN_TYPE, coin);
                editor.putStringSet(CATEGORY_ARRAY, categories);
                editor.commit();
            }
        };
        thread.start();
    }

    public int loadData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        return sharedPref.getInt(LIMIT, 3000);
    }

    public String loadCoinnType() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        return sharedPref.getString(COIN_TYPE, "NIS");
    }

    public Set<String> loadCategories() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Set<String> defaultCtg = new HashSet<String>();
        defaultCtg.addAll(Arrays.asList(items));
        return sharedPref.getStringSet(CATEGORY_ARRAY, defaultCtg);
    }

    public void goToSubmition(View view) {
        Intent intent = new Intent(this, submition.class);
        startActivity(intent);
    }

    public void goToCharts(View view) {
        Intent intent = new Intent(this, charts.class);
        startActivity(intent);
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}
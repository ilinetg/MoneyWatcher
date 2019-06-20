package com.example.galilinetsky.moneywatcher.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.galilinetsky.moneywatcher.SideActivities.Calendar;
import com.example.galilinetsky.moneywatcher.Controllers.IController;
import com.example.galilinetsky.moneywatcher.Controllers.SubmitionController;
import com.example.galilinetsky.moneywatcher.Clients.ExpenseDataHelper;
import com.example.galilinetsky.moneywatcher.R;

import java.util.ArrayList;
import java.util.Set;

public class submition extends AppCompatActivity {
    private ImageButton btngocalendar;
    private String date;
    private ExpenseDataHelper myDB;
    private EditText thedate;
    private Spinner categTxt;
    private EditText prodTxt;
    private EditText pricTxt;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String CATEGORY_ARRAY = "categories";
    private IController _cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submition);
        _cont = new SubmitionController(this, submition.this);
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);

        //create a list of items for the spinner.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> defaultCtg = sharedPref.getStringSet(CATEGORY_ARRAY,null);
        ArrayList<String> tmpItems = new ArrayList<String >();
        tmpItems.addAll(defaultCtg);
        String[] items = tmpItems.toArray(new String[tmpItems.size()]);

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        thedate = (EditText) findViewById(R.id.editText8);
        pricTxt = (EditText) findViewById(R.id.editText6);
        prodTxt = (EditText) findViewById(R.id.editText7);
        categTxt = (Spinner) findViewById(R.id.spinner1);
        btngocalendar = (ImageButton) findViewById(R.id.btngocalendar);
        Intent incoming = getIntent();
        ArrayList<String> dataList = incoming.getStringArrayListExtra("array");
        if(dataList != null ){
            this.date = dataList.get(3);
            pricTxt.setText(dataList.get(1));
            prodTxt.setText(dataList.get(0));
            thedate.setText(date);
            for(int i = 0;i < items.length; i++){
                if(dataList.get(2).compareTo(items[i]) == 0){
                    categTxt.setSelection(i);
                }
            }
        }

        btngocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodStr = prodTxt.getText().toString();
                String priStr = pricTxt.getText().toString();
                String catStr = categTxt.getSelectedItem().toString();
                ArrayList<String> list = new ArrayList<String>();
                list.add(prodStr);
                list.add(priStr);
                list.add(catStr);
                Intent intent = new Intent(submition.this, Calendar.class);
                intent.putStringArrayListExtra("array",list);
                startActivity(intent);
            }
        });

    }

    //add to database
    public void addData(View view){
        _cont.insert(date);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(submition.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}


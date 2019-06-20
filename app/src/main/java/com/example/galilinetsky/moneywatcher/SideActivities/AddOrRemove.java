package com.example.galilinetsky.moneywatcher.SideActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.galilinetsky.moneywatcher.R;

import java.util.Iterator;
import java.util.Set;

public class AddOrRemove extends AppCompatActivity {
    private RadioGroup radioGroup1;
    private RadioButton addButton;
    private EditText category;
    private Button addBtn;
    private SharedPreferences sharedPref;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String CATEGORY_ARRAY = "categories";
    private Set<String> defaultCtg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_remove);
        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        addButton = (RadioButton)findViewById(R.id.add);
        RadioButton rb = (RadioButton) findViewById(R.id.add);
        RadioButton rbRemove = (RadioButton) findViewById(R.id.remove);
        rbRemove.setTextColor(Color.parseColor("#3F51B5"));
        rb.setTextColor(Color.parseColor("#111111"));
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setText("Add");
        //rb.setBackgroundColor(Color.parseColor("#123456"));
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                switch (checkedId)
                {
                    case R.id.add:
                        addBtn.setText("Add");
                        in=new Intent(getBaseContext(),AddOrRemove.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.remove:
                        in = new Intent(getBaseContext(), RemoveCtg.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        defaultCtg = sharedPref.getStringSet(CATEGORY_ARRAY,null);
        category = (EditText) findViewById(R.id.categoryToAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ctg = category.getText().toString();
                if(ctg != null){
                    if(!ctg.equals("")){
                        defaultCtg.add(ctg);
                        category.setText("");
                        //sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet(CATEGORY_ARRAY,defaultCtg);
                        editor.clear().apply();
                        CreateRadioButtons(defaultCtg);
                    }
                }
            }
        });


        this.CreateRadioButtons(defaultCtg);

    }

    public void CreateRadioButtons(Set<String> ctg){
        RadioGroup rdg = (RadioGroup) findViewById(R.id.categorySwitch);
        rdg.removeAllViews();
        Iterator<String> it = ctg.iterator();
        while (it.hasNext()){
            RadioButton rdb = new RadioButton(this);
            rdb.setText(it.next());
            rdb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton b = (RadioButton)v;
                    String btnCtg = b.getText().toString();
                    category.setText(btnCtg);
                }
            });
            rdg.addView(rdb);
        }
    }
}

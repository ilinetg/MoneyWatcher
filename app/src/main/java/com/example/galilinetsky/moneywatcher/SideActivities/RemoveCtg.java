package com.example.galilinetsky.moneywatcher.SideActivities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.galilinetsky.moneywatcher.R;
import com.example.galilinetsky.moneywatcher.SideActivities.AddOrRemove;

import java.util.Set;

public class RemoveCtg extends AddOrRemove {
    private EditText category;
    private Button addBtn;
    private Set<String> defaultCtg;
    private SharedPreferences sharedPref;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String CATEGORY_ARRAY = "categories";
    LinearLayout dynamicContent,bottonNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_remove_ctg, null);
        //dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.remove);
        RadioButton rbAdd = (RadioButton) findViewById(R.id.add);
        rb.setTextColor(Color.parseColor("#111111"));
        rbAdd.setTextColor(Color.parseColor("#3F51B5"));
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        category = (EditText)findViewById(R.id.categoryToAdd);
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setText("Remove");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ctg = category.getText().toString();
                if(ctg != null){
                    if(!ctg.equals("")){
                        defaultCtg = sharedPref.getStringSet(CATEGORY_ARRAY,null);
                        defaultCtg.remove(ctg);
                        category.setText("");
                        Thread thread = new Thread(){
                            public void run(){
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putStringSet(CATEGORY_ARRAY,defaultCtg);
                                editor.clear().apply();
                            }
                        };
                        thread.start();
                        defaultCtg = sharedPref.getStringSet(CATEGORY_ARRAY,null);
                        CreateRadioButtons(defaultCtg);
                        //finish();
                    }
                }
            }
        });
    }

}

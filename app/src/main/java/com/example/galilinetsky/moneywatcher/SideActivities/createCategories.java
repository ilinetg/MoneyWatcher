package com.example.galilinetsky.moneywatcher.SideActivities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.galilinetsky.moneywatcher.MainActivities.MainActivity;
import com.example.galilinetsky.moneywatcher.R;

import java.util.ArrayList;

public class createCategories extends AppCompatActivity {
    private Button setBtn;
    private Button finishBtn;
    private EditText categoryTxt;
    private ArrayList<String> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_categories);
        final LinearLayout view = (LinearLayout) findViewById(R.id.scrollLayout);
        setBtn = (Button) findViewById(R.id.set_button);
        categoryTxt =(EditText) findViewById(R.id.editText);
        categories = new ArrayList<String>();
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!categoryTxt.getText().toString().equals("")){
                    Button txt = new Button(createCategories.this);
                    String txtStr = categoryTxt.getText().toString();
                    SpannableString spanString = new SpannableString(txtStr);
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                    txt.setText(spanString);
                    txt.setTextSize(20);
                    txt.setWidth(700);
                    categories.add(txtStr);
                    view.addView(txt);
                }
            }
        });

        finishBtn = (Button) findViewById(R.id.finish_btn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(createCategories.this, MainActivity.class);
                intent.putStringArrayListExtra("Categories array",categories);
                startActivity(intent);
                finish();
            }
        });
    }
}

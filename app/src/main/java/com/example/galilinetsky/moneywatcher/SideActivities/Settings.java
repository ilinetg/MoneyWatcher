package com.example.galilinetsky.moneywatcher.SideActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.galilinetsky.moneywatcher.MainActivities.MainActivity;
import com.example.galilinetsky.moneywatcher.R;

public class Settings extends AppCompatActivity {
    private RadioButton gbp;
    private RadioButton nis;
    private RadioButton usd;
    private RadioButton euro;
    private RadioButton rub;
    private RadioButton[] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Chooose your coin");
        buttons = new RadioButton[5];
        for(int i = 0; i < buttons.length; i++){
            int id = getResources().getIdentifier("button_"+i, "id", getPackageName());
            buttons[i] = (RadioButton) findViewById(id);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton b = (RadioButton) v;
                    String buttonText = b.getText().toString();
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("coin type",buttonText);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

}

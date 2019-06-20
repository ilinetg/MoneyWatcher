package com.example.galilinetsky.moneywatcher.SideActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galilinetsky.moneywatcher.MainActivities.MainActivity;
import com.example.galilinetsky.moneywatcher.R;

public class notification extends AppCompatActivity {
    private TextView limitSum;
    private Button okButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Set your monthly expense limit");
        limitSum = (TextView) findViewById(R.id.lim_sum);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lim = limitSum.getText().toString();
                String tmp = lim.trim();
                String[] limitStr = tmp.split(" ");
                if (limitStr.length == 1 && limitStr[0].matches("[0-9]+")){
                    if (lim != ""){
                        Intent intent = new Intent(notification.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("limit sum",lim);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(notification.this,"Invalid number",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

package com.example.galilinetsky.moneywatcher.SideActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.example.galilinetsky.moneywatcher.MainActivities.submition;
import com.example.galilinetsky.moneywatcher.R;

import java.util.ArrayList;

public class Calendar extends AppCompatActivity {

    private  static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + "/" + month + "/"+ year ;
                Log.d(TAG, "onSelectedDayChange: mm/dd/yyyy:" + date);
                Intent incoming = getIntent();
                ArrayList<String> list = incoming.getStringArrayListExtra("array");
                list.add(date);
                Intent intent = new Intent(Calendar.this, submition.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("array",list);
                startActivity(intent);
                finish();
            }
        });
    }
}

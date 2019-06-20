package com.example.galilinetsky.moneywatcher.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.galilinetsky.moneywatcher.Controllers.ChartController;
import com.example.galilinetsky.moneywatcher.Controllers.IController;
import com.example.galilinetsky.moneywatcher.Utils.DecimalRemover;
import com.example.galilinetsky.moneywatcher.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

public class charts extends AppCompatActivity implements OnChartValueSelectedListener {
    private IController _controller;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String CATEGORY_ARRAY = "categories";
    private String[] category;
    private PieChart pieChart;
    final int[] colorsArray = {Color.GRAY,Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN, Color.MAGENTA,Color.CYAN,
            Color.rgb(30,60,100),Color.rgb(180,100,30),Color.rgb(246,230,196),
            Color.rgb(200,237,253), Color.rgb(252,199,202),
            Color.rgb(179,233,216), Color.rgb(247,233,174),
            Color.rgb(211,211,246), Color.rgb(108,59,57),
            Color.rgb(75,132,138), Color.rgb(124,96,66),
            Color.rgb(247,173,89), Color.rgb(235,108,63),
            Color.rgb(208,75,52), Color.rgb(255,202,39),
            Color.rgb(147,37,166), Color.rgb(22,158,250),
            Color.rgb(118,7,47), Color.rgb(44,183,80),
            Color.rgb(100,22,151), Color.rgb(88,42,71),
            Color.rgb(27,40,121), Color.rgb(29,112,74),
            Color.rgb(252,216,82), Color.rgb(247,99,64),
            Color.rgb(232,57,52)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //create a list of categories
        Set<String> defaultCtg = sharedPref.getStringSet(CATEGORY_ARRAY,null);
        ArrayList<String> tmpItems = new ArrayList<String >();
        tmpItems.addAll(defaultCtg);
        _controller = new ChartController(this,charts.this);
        category = tmpItems.toArray(new String[tmpItems.size()]);

        pieChart = (PieChart) findViewById(R.id.piechart);
        float vals[] = new float[category.length];
        pieChart.setUsePercentValues(true);
        for(int j = 0; j < category.length;j++ ){
           vals[j] = ((ChartController)_controller).monthExpense(category[j],null);
        }
        this.CreatePieChart(vals);
        this.CreateLegendView();

        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(1, 10, 5, 20);

        //Disable Hole in the Pie Chart
        pieChart.setDrawHoleEnabled(false);

        //pieChart.setClickable(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(charts.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void CreatePieChart(float[] vals){
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int i = 0;i< vals.length;i++){
            if(vals[i] != 0){
                yvalues.add(new PieEntry(vals[i], i));
                xVals.add(category[i]);
                colors.add(colorsArray[i]);
            }
        }
        PieDataSet dataSet = new PieDataSet(yvalues, "number");
        dataSet.setSliceSpace(1);
        PieData data = new PieData(dataSet);
        // In percentage Term
        data.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));
        pieChart.setData(data);
        //colors.add(Color.rgb(10,100,200));
        dataSet.setColors(colors);
        data.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12);
        pieChart.setOnChartValueSelectedListener(this);
    }

    private void CreateLegendView(){
        LegendEntry[] legendEntry = new LegendEntry[category.length];
        for(int i=0 ; i < category.length; i++){
            legendEntry[i] = new LegendEntry(category[i], Legend.LegendForm.SQUARE,15f,2f,null, colorsArray[i]);
        }

        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setCustom(legendEntry);
        legend.setTextSize(15);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String valStr = String.valueOf(e.getData());
        int index = Integer.parseInt(valStr);
        float sum = e.getY();
        String categor = this.category[index];
        Toast.makeText(charts.this,"You spent on " + categor +" "+String.valueOf(sum )
                ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(){
        // do stuff
    }
}



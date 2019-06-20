package com.example.galilinetsky.moneywatcher.Controllers;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.example.galilinetsky.moneywatcher.Clients.ExpenseDataHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChartController implements IController {
    private AppCompatActivity _view;
    private Context _context;
    private ExpenseDataHelper myDB;

    public ChartController(AppCompatActivity v, Context con){
        _view = v;
        _context = con;
        myDB = new ExpenseDataHelper(_view);
    }

    @Override
    public void insert(String data) {

    }

    public float monthExpense(String categoryType,String month){
        float sum = 0;
        if(month == null){
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String todayString = formatter.format(todayDate);
            String[] tmp = todayString.split("/");
            month = tmp[1];
        }
        Cursor cursor = myDB.getByCategory(categoryType);
        while (cursor.moveToNext()){
            String date = cursor.getString(1);
            String[] tmpDate = date.split("/");
            if(Integer.parseInt(tmpDate[1]) == Integer.parseInt(month)){
                String value = cursor.getString(4);
                sum += Float.parseFloat(value);
            }
        }
        return sum;
    }

    @Override
    public String get(String data) {
        return null;
    }

    @Override
    public boolean delete(String data) {
        return false;
    }

    @Override
    public void updateView() {

    }
}

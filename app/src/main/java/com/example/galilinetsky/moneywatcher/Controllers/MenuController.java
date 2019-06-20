package com.example.galilinetsky.moneywatcher.Controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.galilinetsky.moneywatcher.Clients.ExpenseDataHelper;
import com.example.galilinetsky.moneywatcher.Utils.LimitNotify;
import com.example.galilinetsky.moneywatcher.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MenuController implements IController {
    private AppCompatActivity _view;
    private Context _context;
    private ExpenseDataHelper myDB;
    private String _coin;
    private String todaySum;
    private boolean notification = false;

    public MenuController(AppCompatActivity v, Context con){
        _context = con;
        _view = v;
        myDB = new ExpenseDataHelper(_view);
    }
    @Override
    public void insert(String data) {

    }

    public float monthExpense() {
        ExpenseDataHelper myDB = new ExpenseDataHelper(_context);
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = formatter.format(todayDate);
        String[] tmpDate = todayString.split("/");
        for (int j = 0; j < 3; j++) {
            tmpDate[j] = Integer.toString(Integer.parseInt(tmpDate[j]));
        }
        todayString = String.join("/", tmpDate);
        float monthlySum = myDB.getDaily(todayString);
        int day = Integer.parseInt(tmpDate[0]);
        while (day != 0) {
            day--;
            tmpDate[0] = Integer.toString(day);
            todayString = String.join("/", tmpDate);
            monthlySum += myDB.getDaily(todayString);
        }
        return monthlySum;
    }

    public float dailyExpense(String coin) {
        //set daily expense
        String today = null;
        _coin = coin;
        ExpenseDataHelper myDB = new ExpenseDataHelper(_context);
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = formatter.format(todayDate);
        String[] tmp = todayString.split("/");
        for (int j = 0; j < 3; j++) {
            tmp[j] = Integer.toString(Integer.parseInt(tmp[j]));
        }
        today = String.join("/", tmp);
        float dailySum = myDB.getDaily(today);
        todaySum = Float.toString(dailySum);
        updateView();
        return dailySum;
    }

    @Override
    public String get(String data) {
        return null;
    }

    @Override
    public boolean delete(String data) {
        return false;
    }

    public void setNotifyOn(boolean b){
        this.notification = b;
    }
    @Override
    public void updateView() {
        if (notification){
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = _context.getSystemService(NotificationManager.class);
            LimitNotify notifier = new LimitNotify(_context, notificationManager);
            notifier.createNotificationChannel();
            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(_context, AlertDialog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0, intent, 0);
            notifier.setLimit(pendingIntent);
            notification = false;
            return;
        }
        else{
            EditText daily = (EditText) _view.findViewById(R.id.editText2);
            daily.setFocusable(false);
            daily.setTextSize(15);
            daily.setText("today's expenses: " + todaySum + " " + _coin, TextView.BufferType.NORMAL);
        }

    }

    public void createXL(){
        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        //File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "csvname.csv");
        try {
            /* Request user permissions in runtime */
            ActivityCompat.requestPermissions(_view,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    100);
            /* Request user permissions in runtime */
            int permissionCheck = ContextCompat.checkSelfPermission(_context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!file.createNewFile()) {
                    Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
                }
            }
            //boolean bool = file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = myDB.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM Expenses_talbe", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1),
                        curCSV.getString(2), curCSV.getString(3), curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            int x = 0;
        }

    }

}

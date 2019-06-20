package com.example.galilinetsky.moneywatcher.Controllers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.galilinetsky.moneywatcher.Clients.ExpenseDataHelper;
import com.example.galilinetsky.moneywatcher.MainActivities.MainActivity;
import com.example.galilinetsky.moneywatcher.R;
import com.example.galilinetsky.moneywatcher.Clients.TCPClient;

public class SubmitionController implements IController {
    private AppCompatActivity _view;
    private Context _context;
    private ExpenseDataHelper myDB;
    private Spinner categTxt;
    private EditText prodTxt;
    private EditText pricTxt;
    private EditText thedate;

    private String title;
    private String msgToShow;
    private TCPClient _client;

    public SubmitionController(AppCompatActivity v, Context con){
        _view = v;
        _context = con;
        myDB = new ExpenseDataHelper(_view);
        thedate = (EditText) _view.findViewById(R.id.editText8);
        pricTxt = (EditText) _view.findViewById(R.id.editText6);
        prodTxt = (EditText) _view.findViewById(R.id.editText7);
        categTxt = (Spinner) _view.findViewById(R.id.spinner1);

        _client = TCPClient.SingeltonClient();
    }


    //add to database
    @Override
    public void insert(String date){
        String categ_str = categTxt.getSelectedItem().toString();
        String product_str = prodTxt.getText().toString();
        String price_str = pricTxt.getText().toString();

        if(categ_str.equals("") || price_str.equals("") || product_str.equals("")){
            Toast.makeText(_context,"missing data",Toast.LENGTH_LONG).show();
            return;
        }
        boolean inserted = myDB.insertData(date,categ_str,product_str,price_str);
        _client.sendMessage("INSERT,"+ date + "," + categ_str+ "," +product_str+ "," +price_str);
        _client.receiveMessage();
        if(inserted){
            StringBuffer massege = myDB.getCurrentData();
            title = "Latest Expense info:";
            msgToShow = massege.toString();
            this.updateView();
        }
        else{
            Toast.makeText(_context,"Data not inserted",Toast.LENGTH_LONG).show();
        }
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
        //ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.MyDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(_context,R.style.AppCompatAlertDialogStyle);
        View v = _view.getLayoutInflater().inflate(R.layout.dialog_submittion,null);
        //builder.setView(v);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msgToShow);
        String [] tmp = msgToShow.split(" ");
        String [] getId = tmp[1].split("\n");
        final String id = getId[0];
        builder.setPositiveButton("Edit", new Dialog.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String id = _client.serverMessage;
                _client.sendMessage("DELETE," + id);
                _client.serverMessage = null;
                myDB.delete(id);
            }
        });
        builder.setNegativeButton("Add", new Dialog.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(_context, MainActivity.class);
                _view.startActivity(intent);
                _view.finish();
                return;
            }
        });

        builder.show();
    }

}

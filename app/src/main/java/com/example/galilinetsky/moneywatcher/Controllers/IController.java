package com.example.galilinetsky.moneywatcher.Controllers;

import android.support.v7.app.AppCompatActivity;

public interface IController {
    public void insert(String data);
    public String get(String data);
    public boolean delete(String data);
    public void updateView();
}

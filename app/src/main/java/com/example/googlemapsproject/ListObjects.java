package com.example.googlemapsproject;

import android.view.LayoutInflater;
import android.view.View;

public interface ListObjects{
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}

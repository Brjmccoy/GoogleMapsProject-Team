package com.example.googlemapsproject;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header implements ListObjects {

    private String name;

    public Header(String name)
    {
        this.name = name;
    }
    @Override
    public int getViewType()
    {
        return ListObjectsAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;

        if(convertView == null)
        {
            view = (View) inflater.inflate(R.layout.header, null);
        }
        else
        {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(name);

        return view;
    }
}

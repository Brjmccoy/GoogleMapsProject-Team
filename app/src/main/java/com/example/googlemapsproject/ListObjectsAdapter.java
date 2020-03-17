package com.example.googlemapsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class ListObjectsAdapter extends ArrayAdapter<ListObjects> {

    private LayoutInflater viewInflater;

    public ListObjectsAdapter(Context context, List<ListObjects> list)
    {
        super(context, 0, list);
        viewInflater = LayoutInflater.from(context);
    }

    public enum RowType
    {
        LIST_ITEM, HEADER_ITEM
    }

    @Override
    public int getViewTypeCount()
    {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position)
    {
        return getItem(position).getViewType();
    }

    private static final int TYPE_LIST_ITEM = 0;
    private static final int TYPE_HEADER = 1;

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        int rowType = getItemViewType(position);

        if(convertView == null)
        {
            holder = new ViewHolder();
            switch(rowType)
            {
                case TYPE_LIST_ITEM:
                    convertView = viewInflater.inflate(R.layout.list_item, null);
                    holder.View = getItem(position).getView(viewInflater, convertView);
                    break;

                case TYPE_HEADER:
                    convertView = viewInflater.inflate(R.layout.header, null);
                    holder.View = getItem(position).getView(viewInflater, convertView);
                    break;
            }

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;

    }

    public static class ViewHolder
    {
        public View View;
    }

}

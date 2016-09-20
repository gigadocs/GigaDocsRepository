package com.datappsinfotech.gigadocs.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datappsinfotech.gigadocs.R;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<String> {

public CustomAdapter(Context context, ArrayList<String> array) {
        super(context, R.layout.custom_row, array);
}

     class MyViewHolder
     {
         TextView textView;
         ImageView imageView;

         MyViewHolder(View v){
             imageView= (ImageView) v.findViewById(R.id.imageView);
             textView= (TextView) v.findViewById(R.id.textView);

         }
     }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        MyViewHolder holder;

        if(row==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.custom_row, parent, false);
            holder = new MyViewHolder(row);

            row.setTag(holder);
        }
        else{
            holder= (MyViewHolder) row.getTag();
        }

        String singleMobileNo= getItem(position);


        holder.imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
        holder.textView.setText(singleMobileNo);

        return row;
    }
}

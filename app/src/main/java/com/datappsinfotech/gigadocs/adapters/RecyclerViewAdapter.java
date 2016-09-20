package com.datappsinfotech.gigadocs.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.R;
import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.dtos.CalendarDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {
    private ArrayList<CalendarDTO> slotsList;
    CalendarDTO calendarDTO;
    Context context;
    View view;

    public RecyclerViewAdapter(Context context, ArrayList slotsList) {
        this.context= context;
        this.slotsList = slotsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calender_recycler_items, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            calendarDTO = slotsList.get(position);
            holder.textViewSlots.setText(calendarDTO.getSetCalendar());
            holder.textViewPatientName.setText(calendarDTO.getSetPatientName());
            AppointmentDataBaseHelper myAdb = new AppointmentDataBaseHelper(context.getApplicationContext());
            if (calendarDTO.isSaved()){
                holder.calendarRecyclerLinearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.md_red_100));
                holder.childLinearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.md_red_100));
                holder.recyclerViewCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.md_red_100));

            }else {
                holder.calendarRecyclerLinearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                holder.childLinearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                holder.recyclerViewCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return slotsList.size();
    }

    public void addItem() {
        slotsList.add(calendarDTO);
        notifyItemInserted(slotsList.size());
    }

    public void removeItem(int position,View view) {
       //        slotsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, slotsList.size());
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textt_viewSlots)
            TextView textViewSlots;
            @BindView(R.id.textt_viewPatientName)
            TextView textViewPatientName;
            @BindView(R.id.linear_layoutCalendarRecycler)
        LinearLayout calendarRecyclerLinearLayout;
        @BindView(R.id.linear_layoutChild)
        LinearLayout childLinearLayout;
        @BindView(R.id.card_viewRecyclerView)
        CardView recyclerViewCardView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(context, "Swipe left to call & Right to prescription", Toast.LENGTH_SHORT).show();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            try {
                recyclerViewCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.md_green_50));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }
}
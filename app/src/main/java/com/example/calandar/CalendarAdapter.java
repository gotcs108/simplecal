package com.example.calandar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.Signature;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {
    private ArrayList<LocalDateTime> months;


    CalendarAdapter(ArrayList<LocalDateTime> months){
        this.months = months;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Don't add right now. */
        SingleMonth view = new SingleMonth(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CalendarViewHolder) holder).onBind(months.get(position));
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    void addMonths() {
        LocalDateTime wantDateTime = months.get(getItemCount()-1).plusMonths(1);
        months.add(wantDateTime);
        notifyItemInserted(getItemCount());
    }
    void addPrevMonths() {
        LocalDateTime wantDateTime = months.get(0).minusMonths(1);
        months.add(0, wantDateTime);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<LocalDateTime> getMonths(){
        return months;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        SingleMonth calendar;
        CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            calendar = (SingleMonth)itemView;
        }
        void onBind(LocalDateTime want){
            calendar.constructCalendar(want);
            //set click listener + nested class
            calendar.setDayClickListener(new SingleMonth.OnDayClickListener() {
                @Override
                public void onDayClick(View v) {
                    Button b = (Button)v;
                    Toast.makeText(v.getContext(), b.getText().toString(),Toast.LENGTH_SHORT).show();
                    System.out.println(b.getParent());
                }
            });
            calendar.setHighlight(LocalDateTime.now());
        }

    }



}

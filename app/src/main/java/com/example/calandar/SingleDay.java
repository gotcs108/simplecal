package com.example.calandar;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SingleDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);
        setup();
    }
    public void setup(){
        RelativeLayout eventDraw = findViewById(R.id.eventDraw);
        gridLinesDraw();
        eventDraw.addView(createEvent("surprise", LocalTime.of(0,30), LocalTime.of(3,30), ContextCompat.getColor(this, R.color.colorAccent)));
    }
    public TextView createEvent(String eventName, LocalTime duration, LocalTime happenAt, int color){
        TextView event = new TextView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.gridLines);
        layoutParams.addRule(RelativeLayout.END_OF, R.id.timeline);
        event.setGravity(Gravity.CENTER);
        event.setElevation(0);

        event.setHeight(conversion(duration.getHour()*60+duration.getMinute()));
        layoutParams.setMargins(conversion(5),conversion(happenAt.getHour()*60+happenAt.getMinute()),0,0);
        event.setText(eventName);
        event.setBackgroundColor(color);
        event.setLayoutParams(layoutParams);
        return event;
    }
    public int conversion(int dps){
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dps * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public void gridLinesDraw(){
        LinearLayout gridLines = findViewById(R.id.gridLines);
        for(int i = 0; i<24; i++) {
            View emptyBlock = new View (this);
            View horLine = new View(this);

            RelativeLayout.LayoutParams layoutParamsEmpty = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, conversion(59));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, conversion(1));

            emptyBlock.setLayoutParams(layoutParamsEmpty);

            horLine.setLayoutParams(layoutParams);
            horLine.setBackgroundColor(Color.parseColor("#c0c0c0"));

            gridLines.addView(horLine);
            gridLines.addView(emptyBlock);
        }

        View emptyBlock = new View (this);
        View horLine = new View(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, conversion(1));
        horLine.setLayoutParams(layoutParams);
        horLine.setBackgroundColor(Color.parseColor("#c0c0c0"));
        gridLines.addView(horLine);
    }
}

package com.example.calandar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;

public class SingleMonth extends LinearLayout {
    LinearLayout month;
    LinearLayout weekOneLayout;
    LinearLayout weekTwoLayout;
    LinearLayout weekThreeLayout;
    LinearLayout weekFourLayout;
    LinearLayout weekFiveLayout;
    LinearLayout weekSixLayout;
    private LinearLayout[] weeks;
    private RelativeLayout[] dayWrappers;
    private Button[] days;
    private LocalDateTime calendar;
    private Context context;
    OnDayClickListener listener;
    View viewSpec;
    Month currMonth;

    public SingleMonth(Context context){
        super(context);
        init(context);
    }
    public SingleMonth(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public SingleMonth(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        viewSpec = LayoutInflater.from(context).inflate(R.layout.single_month, this, true);
        month = viewSpec.findViewById(R.id.month);
        weekOneLayout = viewSpec.findViewById(R.id.week_1);
        weekTwoLayout = viewSpec.findViewById(R.id.week_2);
        weekThreeLayout = viewSpec.findViewById(R.id.week_3);
        weekFourLayout = viewSpec.findViewById(R.id.week_4);
        weekFiveLayout = viewSpec.findViewById(R.id.week_5);
        weekSixLayout = viewSpec.findViewById(R.id.week_6);
        days = new Button[6*7];
        dayWrappers = new RelativeLayout[6*7];
        weeks = new LinearLayout[6];
        weeks[0] = weekOneLayout;
        weeks[1] = weekTwoLayout;
        weeks[2] = weekThreeLayout;
        weeks[3] = weekFourLayout;
        weeks[4] = weekFiveLayout;
        weeks[5] = weekSixLayout;
        this.context = context;
    }
    public void constructCalendar(LocalDateTime wantDateTime){
        currMonth = wantDateTime.getMonth();
        boolean instantiated = false;
        if (calendar!=null){
            instantiated = true;
        }
        //set to first date of the given month
        calendar = LocalDateTime.of(wantDateTime.getYear(), currMonth, 1, 0, 0);
        DayOfWeek startDayOfWeek = calendar.getDayOfWeek();
        int startDayOfWeekInt = startDayOfWeek.getValue();
        //The values are numbered following the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
        //shifting the ordinal days so sunday is 1 and saturday is 7
        for(int i=0; i<6; i++){
            weeks[i].removeAllViews();
            for(int d=0; d<7; d++){
                Button day = new Button(context);
                RelativeLayout dayWrapper = new RelativeLayout(context);
                LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                LayoutParams relParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                relParams.weight = 1;
                day.setLayoutParams(params);
                day.setBackgroundColor(Color.WHITE);
                //day inside dayWrapper inside weeks
                days[i*7+d] = day;
                dayWrappers[i*7+d] = dayWrapper;
                dayWrapper.addView(days[i*7+d]);
                dayWrapper.setLayoutParams(relParams);
                weeks[i].addView(dayWrappers[i*7+d]);
            }
        }
        for(int i=0; i<calendar.toLocalDate().lengthOfMonth(); i++) {
            days[i + startDayOfWeekInt].setText(String.valueOf(i + 1));
            days[i + startDayOfWeekInt].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (listener != null) {
                        listener.onDayClick(v);
                    }
                }
            });
        }

        TextView fillerText = new TextView(context);
        TextView monthText = new TextView(context);
        LayoutParams monthFillerTextP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        monthFillerTextP.weight = (startDayOfWeekInt-1);
        fillerText.setLayoutParams(monthFillerTextP);
        LayoutParams monthTextP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        monthTextP.weight = 7-(startDayOfWeekInt-1);
        monthText.setLayoutParams(monthTextP);
        monthText.setText(calendar.getMonth().toString());
        monthText.setTextSize(20);
        monthText.setGravity(Gravity.CENTER);
        month.removeAllViews();
        month.addView(fillerText);
        month.addView(monthText);
    }
    //TODO
    public void setHighlight(LocalDateTime dateTime) {
        //if the month of the highlight day is the referred month
        if(dateTime.getMonth() == currMonth) {
            DayOfWeek startDayOfWeek = calendar.getDayOfWeek();
            int startDayOfWeekInt = startDayOfWeek.getValue();
            int dayId = dateTime.getDayOfMonth() + startDayOfWeekInt - 1;

            int revI = (int) Math.floor(dayId / 7);

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            RelativeLayout highlightButton = (RelativeLayout) layoutInflater.inflate(R.layout.singleday_highlight, weeks[revI], false);
            Button dayButton = highlightButton.findViewById(R.id.day_button);
            dayButton.setText(String.valueOf(dateTime.getDayOfMonth()));
            int index = weeks[revI].indexOfChild(dayWrappers[dayId]);
            weeks[revI].removeViewAt(index);
            dayWrappers[dayId] = highlightButton;
            dayWrappers[dayId].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (listener != null) {
                        listener.onDayClick(v);
                    }
                }
            });
            weeks[revI].addView(highlightButton, index);
        }
    }
    public void setEventNotice() {

    }

    public void setDayClickListener(OnDayClickListener l) {
        listener = l;
    }

    /* custom click listener/callback */
    //usage: singleMonth.setDayClickListener(new OnDayClickListener{onDayClick}); 3 new custom parts
    //onClickListener is also just an interface
    //OnDayClickListener -> OnClickListener
    public interface OnDayClickListener{
        void onDayClick(View v);
    }
}


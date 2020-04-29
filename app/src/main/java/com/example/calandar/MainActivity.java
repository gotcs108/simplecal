package com.example.calandar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CalendarAdapter adapter;
    boolean loading = true;
    int firstVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<LocalDateTime> months = new ArrayList<LocalDateTime>();
        final TextView yearStatus = findViewById(R.id.yearStatus);

        LocalDateTime wantDateTime = LocalDateTime.now();




        for(int i=0; i<10; i++) {
            months.add(LocalDateTime.of(wantDateTime.getYear(), wantDateTime.getMonth().plus(i-6), 1, 0, 0));
        }
        adapter = new CalendarAdapter(months);
        adapter.setHasStableIds(true);
        final RecyclerView cRecycler = (RecyclerView)findViewById(R.id.cRecycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cRecycler.setLayoutManager(linearLayoutManager);
        cRecycler.setAdapter(adapter);


        cRecycler.scrollToPosition(6);
        yearStatus.setText(String.valueOf(months.get(5).getYear()));

        cRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //difference in x and y

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy!=0){
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    yearStatus.setText(String.valueOf(adapter.getMonths().get(firstVisiblesItems).getYear()));

                    if (dy > 0) { //check for scroll down
                        if (loading) {
                            if ((visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                                loading = false;
                                //load for pagination
                                loadNextData();
                                loading = true;
                            }
                        }
                    }
                    if (dy < 0) { //check for scroll up
                        if (loading) {
                            if (firstVisiblesItems <= 5) {
                                loading = false;
                                //load for pagination
                                loadPrevData();
                                cRecycler.scrollToPosition(6);
                                loading = true;
                            }
                        }
                    }
                }
            }
        });
    }
    public void transition(View v){startActivity(new Intent(this, SingleDay.class));}
    public void loadNextData() {
        adapter.addMonths();
    }
    public void loadPrevData() {
        adapter.addPrevMonths();
    }
}

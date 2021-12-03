package com.example.subway_dijsktra_algorithm;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class activity_result extends AppCompatActivity {

    private fragmentPageAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        adapter = new fragmentPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //뷰페이저 세팅
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.rgb(234, 234, 234), Color.rgb(0, 0, 0));
        tabLayout.setupWithViewPager(viewPager);
        fragment_least_transfer least_transfer_Fragment = new fragment_least_transfer();
        fragment_shortest_time shortest_time_Fragment = new fragment_shortest_time();

        //about get Data----------------------------------//
        Intent intent = getIntent();
        String startLineNum = intent.getExtras().getString("startLineSpinner");
        String destLineNum = intent.getExtras().getString("destLineSpinner");
        String startStationName = intent.getExtras().getString("startStationSpinner");
        String destStationName = intent.getExtras().getString("destStationSpinner");

        //send Data to program----------------------------------//
        informationTotal data = program(startLineNum, destLineNum, startStationName, destStationName);

        fragment_least_transfer.DataInput(data.least_Time, data.least_TransferCount, data.least_PassedCount, data.least_Path);
        fragment_shortest_time.DataInput(data.short_Time, data.short_TransferCount, data.short_PassedCount, data.short_Path);
        adapter.addItem(least_transfer_Fragment);
        adapter.addItem(shortest_time_Fragment);
        viewPager.setAdapter(adapter);
    }

    public informationTotal program(String line1, String line2, String station1, String station2) {

        informationTotal total = new informationTotal();

        int startLine = 0;
        int destLine = 0;
        switch (line1) {
            case "1호선":
                startLine = 1;
                break;
            case "2호선":
                startLine = 2;
                break;
            case "3호선":
                startLine = 3;
                break;
            case "4호선":
                startLine = 4;
                break;
            case "5호선":
                startLine = 5;
                break;
            case "6호선":
                startLine = 6;
                break;
            case "7호선":
                startLine = 7;
                break;
            case "8호선":
                startLine = 8;
                break;
            case "9호선":
                startLine = 9;
                break;
            case "수인분당선":
                startLine = 101;
                break;
        }
        switch (line2) {
            case "1호선":
                destLine = 1;
                break;
            case "2호선":
                destLine = 2;
                break;
            case "3호선":
                destLine = 3;
                break;
            case "4호선":
                destLine = 4;
                break;
            case "5호선":
                destLine = 5;
                break;
            case "6호선":
                destLine = 6;
                break;
            case "7호선":
                destLine = 7;
                break;
            case "8호선":
                destLine = 8;
                break;
            case "9호선":
                destLine = 9;
                break;
            case "수인분당선":
                destLine = 101;
                break;
        }
        String start = station1;
        String dest = station2;

        //최소시간
        Shortest_Time_Data.GraphData();
        Shortest_Time_Data.StationData();
        information shortdata = Shortest_Time_Data.program(startLine, destLine, start, dest);
        total.short_Time = shortdata.Time;
        total.short_TransferCount = shortdata.TransferCount;
        total.short_PassedCount = shortdata.PassedCount;
        total.short_Path = shortdata.Path;

        //최소환승
        Least_Transfer_Data.GraphData();
        Least_Transfer_Data.StationData();
        information leastdata = Least_Transfer_Data.program(startLine, destLine, start, dest);
        total.least_Time = leastdata.Time;
        total.least_TransferCount = leastdata.TransferCount;
        total.least_PassedCount = leastdata.PassedCount;
        total.least_Path = leastdata.Path;

        return total;
    }

}

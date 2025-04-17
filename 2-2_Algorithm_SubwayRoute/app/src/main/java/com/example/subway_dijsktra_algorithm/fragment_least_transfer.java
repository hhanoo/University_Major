package com.example.subway_dijsktra_algorithm;

import static java.lang.String.valueOf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import androidx.fragment.app.Fragment;

public class fragment_least_transfer extends Fragment {
    private static String time;
    private TextView time_TextView;
    private static String transfer;
    private TextView transfer_TextView;
    private static String passed;
    private TextView passed_TextView;
    private static String path;
    private TextView path_TextView;
    private int count = 0;

    public static fragment_least_transfer newInstance() {
        fragment_least_transfer fragment = new fragment_least_transfer();
        return fragment;
    }

    public static void DataInput(String timeData, String transferData, String passedData, StringBuilder pathData) {
        time = timeData;
        transfer = transferData;
        passed = passedData;
        path = valueOf(pathData);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_least_transfer, container, false);
        TextView time_textView = (TextView) view.findViewById(R.id.leastTime);
        TextView transfer_textView = (TextView) view.findViewById(R.id.leastTransfer);
        TextView passed_textView = (TextView) view.findViewById(R.id.leastPassed);
        TextView path_textView = (TextView) view.findViewById(R.id.leastPath);

        time_textView.setText(time + "분");
        transfer_textView.setText(transfer + "번");
        passed_textView.setText(passed + "개 역");

        String[] list = path.split(" - ");
        StringBuilder remake = new StringBuilder();
        for (int i = 0; i < list.length - 1; i++) {
            remake.append(list[i] + "\n↓\n");
        }
        remake.append(list[list.length - 1]);
        System.out.println(remake);
        path_textView.setText(remake);

        return view;
        //xml 레이아웃이 인플레이트 되고 자바소스 코드와 연결이된다.
    }

    @Override
    public void onStart() {
        super.onStart();

        System.out.println("시간: " + time);
        System.out.println("환승: " + transfer);
        System.out.println("지나는 역 수: " + passed);
        System.out.println("경로: \n" + path);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

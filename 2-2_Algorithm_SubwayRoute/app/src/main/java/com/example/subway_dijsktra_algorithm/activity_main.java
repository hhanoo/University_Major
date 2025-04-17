package com.example.subway_dijsktra_algorithm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class activity_main extends AppCompatActivity {

    private Spinner startLineSpinner, destLineSpinner, startStationSpinner, destStationSpinner;
    private ArrayAdapter<String> startArrayAdapter, destArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLineSpinner = (Spinner) findViewById(R.id.StartLine);
        destLineSpinner = (Spinner) findViewById(R.id.DestLine);
        startStationSpinner = (Spinner) findViewById(R.id.StartStation);
        destStationSpinner = (Spinner) findViewById(R.id.DestStation);

        ArrayAdapter<CharSequence> lineNumAdapter = ArrayAdapter.createFromResource(this,
                R.array.lines_number, android.R.layout.simple_spinner_item);
        lineNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startLineSpinner.setAdapter(lineNumAdapter);
        destLineSpinner.setAdapter(lineNumAdapter);
        startLineSpinner.setSelection(0);
        destLineSpinner.setSelection(0);

        startLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_1);
                        startStationSpinner.setSelection(0);
                        break;
                    case 1:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_2);
                        startStationSpinner.setSelection(0);
                        break;
                    case 2:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_3);
                        startStationSpinner.setSelection(0);
                        break;
                    case 3:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_4);
                        startStationSpinner.setSelection(0);
                        break;
                    case 4:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_5);
                        startStationSpinner.setSelection(0);
                        break;
                    case 5:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_6);
                        startStationSpinner.setSelection(0);
                        break;
                    case 6:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_7);
                        startStationSpinner.setSelection(0);
                        break;
                    case 7:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_8);
                        startStationSpinner.setSelection(0);
                        break;
                    case 8:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_9);
                        startStationSpinner.setSelection(0);
                        break;
                    case 9:
                        setStart_Station_Spinner_Adapter_Item(R.array.line_101);
                        startStationSpinner.setSelection(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        destLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_1);
                        destStationSpinner.setSelection(0);
                        break;
                    case 1:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_2);
                        destStationSpinner.setSelection(0);
                        break;
                    case 2:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_3);
                        destStationSpinner.setSelection(0);
                        break;
                    case 3:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_4);
                        destStationSpinner.setSelection(0);
                        break;
                    case 4:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_5);
                        destStationSpinner.setSelection(0);
                        break;
                    case 5:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_6);
                        destStationSpinner.setSelection(0);
                        break;
                    case 6:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_7);
                        destStationSpinner.setSelection(0);
                        break;
                    case 7:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_8);
                        destStationSpinner.setSelection(0);
                        break;
                    case 8:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_9);
                        destStationSpinner.setSelection(0);
                        break;
                    case 9:
                        setDest_Station_Spinner_Adapter_Item(R.array.line_101);
                        destStationSpinner.setSelection(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button showResultButton = (Button) findViewById(R.id.resultButton);
        showResultButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_result.class);
                intent.putExtra("startLineSpinner", startLineSpinner.getSelectedItem().toString());
                intent.putExtra("destLineSpinner", destLineSpinner.getSelectedItem().toString());
                intent.putExtra("startStationSpinner", startStationSpinner.getSelectedItem().toString());
                intent.putExtra("destStationSpinner", destStationSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

    }

    //출발역에 대한 호선 선택시 해당 호선역들 연결
    private void setStart_Station_Spinner_Adapter_Item(int line_number) {
        if (startArrayAdapter != null) {
            startStationSpinner.setAdapter(null);
            startArrayAdapter = null;
        }
        startArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (String[]) getResources().getStringArray(line_number));
        startArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startStationSpinner.setAdapter(startArrayAdapter);
    }

    //도착역에 대한 호선 선택시 해당 호선역들 연결
    private void setDest_Station_Spinner_Adapter_Item(int line_number) {
        if (destArrayAdapter != null) {
            destStationSpinner.setAdapter(null);
            destArrayAdapter = null;
        }
        destArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (String[]) getResources().getStringArray(line_number));
        destArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destStationSpinner.setAdapter(destArrayAdapter);
    }
}

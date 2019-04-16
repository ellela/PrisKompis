package com.example.piechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piechart.R;

public class MainActivity extends AppCompatActivity {

    private int calsBurned = 0;
    private int calsConsumed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addBurned(View v) {
        // Get the new value from a user input:
        EditText burnedEditText = findViewById(R.id.burned);

        // Update the old value:
        calsBurned = Integer.parseInt(burnedEditText.getText().toString());
        updateChart();
    }

    public void addConsumed(View v) {
        // Get the new value from a user input:
        EditText consumedEditText = findViewById(R.id.consumed);

        // Update the old value:
        calsConsumed = Integer.parseInt(consumedEditText.getText().toString());
        updateChart();
    }

    private void updateChart(){
        // Update the text in a center of the chart:
        TextView numberOfCals = findViewById(R.id.number_of_calories);
        numberOfCals.setText(String.valueOf(calsBurned) + " / " + calsConsumed);

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d = (double) calsBurned / (double) calsConsumed;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
    }
}
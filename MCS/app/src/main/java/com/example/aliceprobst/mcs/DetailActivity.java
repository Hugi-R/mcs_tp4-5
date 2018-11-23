package com.example.aliceprobst.mcs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView name = findViewById(R.id.name);
        TextView matrix = findViewById(R.id.matrix);
        TextView rate = findViewById(R.id.rate);

        Bundle extras = getIntent().getExtras();
        name.setText(extras.getString("name"));
        matrix.setText(extras.getString("matrix"));
        rate.setText(extras.getString("rate"));
    }
}

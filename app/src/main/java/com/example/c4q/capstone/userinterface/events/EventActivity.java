package com.example.c4q.capstone.userinterface.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.c4q.capstone.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Event Activity");
    }
}

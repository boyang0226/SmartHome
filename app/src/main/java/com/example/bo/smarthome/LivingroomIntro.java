package com.example.bo.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LivingroomIntro extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LivingroomIntro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livingroom_intro);

        Button LRStartButton = (Button) findViewById(R.id.lr_intro_start_button);
        LRStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent LRList = new Intent(LivingroomIntro.this, LivingroomList.class);
                startActivityForResult(LRList, 1);
                Log.i(ACTIVITY_NAME, "Start button on clicked");
            }
        });
    }
}

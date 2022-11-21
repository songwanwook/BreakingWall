package com.example.breakwalldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class HowToPlay extends Activity {
    Button[] button = new Button[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howtoplay);
        button[0] = (Button)findViewById(R.id.Back);
        button[1] = (Button)findViewById(R.id.GameStart);
        button[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backintent = new Intent(getApplicationContext(),Main.class);
                startActivity(backintent);
            }
        });
        button[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameintent = new Intent(getApplicationContext(),Game.class);
                startActivity(gameintent);
            }
        });
    }
}

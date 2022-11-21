package com.example.breakwalldemo;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
public class Main extends AppCompatActivity {
    Button[] button = new Button[2];
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button[0] = (Button)findViewById(R.id.HowToPlay);//게임방법 버튼
        button[1] = (Button)findViewById(R.id.GameStart);//게임시작 버튼
        button[0].setOnClickListener(new View.OnClickListener() {// 게임방법 버튼을 누르면
            @Override
            public void onClick(View view) {//게임방법으로 넘어간다.
                Intent intent = new Intent(getApplicationContext(), HowToPlay.class);
                startActivity(intent);}});
        button[1].setOnClickListener(new View.OnClickListener() {//게임시작 버튼을 누르면
            @Override
            public void onClick(View view) {//게임으로 넘어간다.
                Intent intent = new Intent(getApplicationContext(), Game.class);
                startActivity(intent);
            }
        });
    }
}



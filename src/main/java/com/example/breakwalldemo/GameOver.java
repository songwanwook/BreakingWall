package com.example.breakwalldemo;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.IOException;

public class GameOver extends Activity {
    Button button[] = new Button[2];
    MediaPlayer ending;
    TextView MyScore;
    int score;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", score);
        MyScore = (TextView)findViewById(R.id.score);
        MyScore.setText("나의 점수는 : " + score + "점");
        button[0] = (Button)findViewById(R.id.Back);//처음으로 버튼
        button[1] = (Button)findViewById(R.id.GameStart);//다시시작 버튼
        button[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {//처음으로 버튼 누를 경우
                Intent backintent = new Intent(getApplicationContext(),Main.class);
                startActivity(backintent);//처음 화면으로 돌아감
            }

        });
        button[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//다시시작 버튼을 누를 경우
                Intent gameintent = new Intent(getApplicationContext(),Game.class);
                startActivity(gameintent);//게임 시작
            }
        });

        /*try {
            ending.start();
            sleep(15000);
        } catch (InterruptedException e) {
            ending.stop();
            e.printStackTrace();
        }*/
    }
}

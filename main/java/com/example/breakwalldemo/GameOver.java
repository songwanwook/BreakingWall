package com.example.breakwalldemo;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class GameOver extends Activity {
    RankingDB rd;
    SQLiteDatabase sqldb;
    Button button[] = new Button[3];
    TextView MyScore, cong, text;
    EditText et;
    String str;
    int score;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", score);
        MyScore = (TextView)findViewById(R.id.score);
        MyScore.setText("나의 점수는 : " + score + "점");

        cong = (TextView)findViewById(R.id.congratulations);
        text = (TextView)findViewById(R.id.text);
        et = (EditText)findViewById(R.id.nickname);

        button[0] = (Button)findViewById(R.id.Back);//처음으로 버튼
        button[1] = (Button)findViewById(R.id.GameStart);//다시시작 버튼
        button[2] = (Button)findViewById(R.id.Ranking);
        if(score >= 10000){
            cong.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            et.setVisibility(View.VISIBLE);
            button[2].setVisibility(View.VISIBLE);
        }
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
        rd = new RankingDB(this);
        button[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                str = String.valueOf(et.getText());
                if(str.equals(null)||str.equals("")){
                    Toast.makeText(getApplicationContext(), "닉네임을 입력하세요",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    sqldb = rd.getWritableDatabase();
                    rd.onUpgrade(sqldb,1,2);
                    Toast.makeText(getApplicationContext(), "정상 등록",
                            Toast.LENGTH_SHORT).show();
                    Intent rankingintent = new Intent(getApplicationContext(), Ranking.class);
                    startActivity(rankingintent);//랭킹보기
                }
            }
        });
    }
    public class RankingDB extends SQLiteOpenHelper {

        public RankingDB(@Nullable Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            //db.execSQL("INSERT INTO RANKING VALUES('SON',10000);");
            db.execSQL("INSERT INTO RANKING VALUES('" + str
                    + "', " + score + ");");
        }
    }
}

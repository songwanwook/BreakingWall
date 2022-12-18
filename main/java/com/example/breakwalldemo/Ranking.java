package com.example.breakwalldemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;

public class Ranking extends Activity {
    RankingDB rd;
    SQLiteDatabase sqldb;
    EditText et[] = new EditText[2];
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        Button b = (Button)findViewById(R.id.Back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backintent = new Intent(getApplicationContext(),Main.class);
                startActivity(backintent);
            }
        });
        et[0] = (EditText)findViewById(R.id.Name);//이름 리스트 텍스트뷰
        et[1] = (EditText)findViewById(R.id.Score);//점수 리스트 텍스트뷰
        et[0].setEnabled(false);
        et[1].setEnabled(false);//직접 입력 못하게 비활성화
        rd = new RankingDB(this);
        sqldb = rd.getWritableDatabase();//RankingDB클래스를 쓰기용으로 생성
        //rd.onCreate(sqldb);
        //rd.onUpgrade(sqldb, 1, 2);
        String name = "PLAYER" + "\r\n" + "\r\n";
        String score = "SCORE" + "\r\n" + "\r\n";
        Cursor c = sqldb.rawQuery("SELECT * FROM RANKING ORDER BY score DESC;",null);
        //커서를 선언한 뒤, RANKING에 있는 테이블 정보를 모두 출력
        while(c.moveToNext()){
            name += c.getString(0) + "\r\n";//커서에서 이름 추출
            score += c.getString(1) + "\r\n";//커서에서 점수 추출
        }
        et[0].setText(name);//에디트텍스트에 모든 이름 출력
        et[1].setText(score);//에디트텍스트에 모든 점수 출력
        c.close();
        sqldb.close();
    }
    public class RankingDB extends SQLiteOpenHelper {
        public RankingDB(@Nullable Context context) {
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS RANKING(name char(10), score INTEGER);");
            //RANKING 테이블이 생성되지 않은 상태에서 테이블을 생성하여 쿼리문 오류가 날 확률이 없다.
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            //db.execSQL("INSERT INTO RANKING VALUES('SON',10000);");
        }
    }
};

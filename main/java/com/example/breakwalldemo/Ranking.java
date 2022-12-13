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
        et[0] = (EditText)findViewById(R.id.Name);
        et[1] = (EditText)findViewById(R.id.Score);
        et[0].setEnabled(false);
        et[1].setEnabled(false);
        rd = new RankingDB(this);
        sqldb = rd.getWritableDatabase();
        rd.onCreate(sqldb);
        //rd.onUpgrade(sqldb, 1, 2);
        String name = "PLAYER" + "\r\n" + "\r\n";
        String score = "SCORE" + "\r\n" + "\r\n";
        Cursor c = sqldb.rawQuery("SELECT * FROM RANKING ORDER BY score DESC;",null);
        while(c.moveToNext()){
            name += c.getString(0) + "\r\n";
            score += c.getString(1) + "\r\n";
        }
        et[0].setText(name);
        et[1].setText(score);
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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            //db.execSQL("INSERT INTO RANKING VALUES('SON',10000);");
        }
    }
};

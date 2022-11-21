package com.example.breakwalldemo;

import android.graphics.Rect;
import android.graphics.RectF;

public class Block extends RectF {//벽돌 객체(이번주 주말 부터 만들 예정)
    //크기 높이
    int blockw, blockh, blockx, blocky;
    Rect blockRect;
    public Block(int w, int h, int x, int y){
        blockw = w;blockh = h;blockx = x;blocky = y;
        blockRect = new Rect(blockw, blockh, blockx, blocky);
    }
}


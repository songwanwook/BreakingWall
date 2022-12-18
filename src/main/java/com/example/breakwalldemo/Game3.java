package com.example.breakwalldemo;

import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;

import androidx.annotation.Nullable;

public class Game3 extends Activity {
    Bitmap paddle;//패들
    Bitmap ball;//공
    Bitmap lifeball;//목숨 갯수
    Bitmap bga;//배경
    int paddlegetWidth = 250;
    int clear = 0;
    int x = 300;
    int ballx = 435, bally = 1260;
    boolean playing = false;
    int xspeed, yspeed;
    int score, life, combo;
    view v;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view v = new view(this);
        setContentView(v);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", score);
        life = intent.getIntExtra("life", life);
        combo = intent.getIntExtra("combo", combo);
    }
    Block [][] block;
    public class view extends View {
        int xstart = 30, ystart = 50;//x, y축 시작위치
        int bx, by, bwidth, bheight;
        public view(Context context) {

            super(context);
            block = new Block[8][8];//벽돌 객체 생성
            int start = 3;
            int end = 4;
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    bx = xstart + 130*i; by =ystart + 60*j;
                    bwidth = xstart + 120+(i*130); bheight = ystart + 50+(60*j);//벽돌 그리는 위치
                    if(j >= start && j <= end) {
                        clear++;
                        block[i][j] = new Block(bx, by, bwidth, bheight);
                    }
                }
                if(i < 3) {
                    start--; end++;
                }
                else if(i == 3){
                    start -= 0; end += 0;
                }
                else {
                    start++; end--;
                }
            }
        }
        public void onDraw(Canvas canvas){//벽돌깨기 UI 그리기
            Paint pnt = new Paint();//페인트 객체 설정
            bga = BitmapFactory.decodeResource(getResources(),R.drawable.breakwallsrc);//배경 설정
            canvas.drawBitmap(bga,0,0,null);
            paddle = BitmapFactory.decodeResource(getResources(),R.drawable.button1);//패들 설정
            canvas.drawBitmap(paddle,x,1300,null);
            int start = 3;
            int end = 4;
            for( int i = 0 ; i < 8 ; i++ ) {//다이아몬드 모양 배열
                for (int j = 0; j < 8; j++) {
                    pnt.setColor(Color.RED);
                    if(j >= start && j <= end) {
                        if(j >= start + 1 && j <= end - 1){
                            pnt.setColor(Color.YELLOW);
                        }
                        if(j >= start + 2 && j <= end - 2){
                            pnt.setColor(Color.BLACK);
                        }
                        if(j >= start + 3 && j <= end - 3){
                            pnt.setColor(Color.WHITE);
                        }
                        canvas.drawRect(block[i][j].blockRect, pnt);//벽돌 그리기
                    }
                }
                if(i < 3) {
                    start--; end++;
                }
                else if(i == 3){
                    start -= 0; end += 0;
                }
                else {
                    start++; end--;
                }
            }
            ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);//벽돌 깨는 공 그래픽 생성
            canvas.drawBitmap(ball,ballx,bally,null);//벽돌 깨는 공 그리기
            invalidate();//화면 갱신
            ballHandler();//핸들러호출

            pnt.setColor(Color.BLACK);//검은 색상 설정
            pnt.setTextSize(50);//텍스트 사이즈 50
            canvas.drawText("SCORE : " + score,0,40,pnt);//점수 창
            canvas.drawText("LIFE",500,40,pnt);//목숨 창
            pnt.setTextSize(100);
            pnt.setColor(Color.WHITE);//흰 색상 설정
            canvas.drawText("REMAIN " + clear,0,1540,pnt);//남은벽돌 창
            lifeball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);

            for(int i = 0; i < life; i++){
                canvas.drawBitmap(lifeball,650 + (50*i),0,pnt);//목숨 -1 만큼 볼 그림
            }
            losinglife();//목숨을 잃음에 따라 상단 볼 갯수도 사라짐
        }
    }
    private void ballHandler(){
        if(playing){//플레이 중일때
            ballx += xspeed;
            bally += yspeed;//공의 위치는 x축, y축 만큼 이동
            if(ballx<= 0 || ballx > 1000){//x축 벽에 닿을 경우
                xspeed *= -1;//x축 역방향 이동
            }
            else if(bally <= 40 || (bally > 1260 &&(ballx >= x-30 && ballx <= x+paddlegetWidth+30))){
                //천장에 닿거나, 패들 범위 내에 공이 닿을 경우
                if(bally <= 40) {//천장에 닿으면
                    yspeed = 5;//y축 역방향 이동
                }
                else if(bally > 1260 &&(ballx >= x-30 && ballx <= x+paddlegetWidth+30)) {
                    yspeed = -5;//y축 역방향 이동
                    getSpeed();//공이 닿은 방향에 따라 볼 속도 조절
                }
            }
            collision();//벽돌 깨지는 함수 호출
        }
    }
    public boolean onTouchEvent(MotionEvent event){//터치했을때(가장 중요한 기능)
        int getx = (int) event.getX();//터치 한 위치
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                if (playing) {//플레이를 하는 중일때
                    if (getx < x) {//패들의 x좌표 보다 왼쪽을 터치했을때
                        if (x < 0) {//패들의 x좌표가 0보다 작으면
                            x = 0;//패들의 x좌표 = 0
                        } else {
                            x -= 20;//패들의 x좌표는 왼쪽으로 20만큼 이동
                        }
                    } else {
                        if (x > 1000 - paddlegetWidth) {//패들의 x좌표 + 패들의 크기가 1000보다 클 경우
                            x = 1000 - paddlegetWidth;//패들의 x좌표 = 1000 - 패들의 크기
                        } else {
                            x += 20;//패들의 x좌표는 오른쪽으로 20만큼 이동
                        }
                    }
                } else {//플레이 하고 있지 않을 때
                    playing = true;//플레이 시작
                    if (getx < x) {//첫 터치 위치가 패들 보다 왼쪽일때
                        xspeed = -5;//왼쪽으로 공이 날아감
                        yspeed = -5;
                    } else {//첫 터치 위치가 오른쪽일때
                        xspeed = 5;//오른쪽으로 공이 날아감
                        yspeed = -5;
                    }
                }
                break;
        }
        return true;
    }
    private void getSpeed(){//패들에 닿을 시에 공 속도 변화
        if(ballx >= x + 135 && ballx <= x + 145){
            xspeed = 0;
        }
        else if(ballx >= x + 95 && ballx <= x + 134){
            xspeed = -2;
        }
        else if(ballx >= x + 55 && ballx <= x + 94){
            xspeed = -5;
        }
        else if(ballx >= x + 15 && ballx <= x + 54){
            xspeed = -8;
        }
        else if(ballx >= x - 30 && ballx <= x + 14){
            xspeed = -11;
        }
        else if(ballx >= x + 146 && ballx <= x + 185){
            xspeed = 2;
        }
        else if(ballx >= x + 186 && ballx <= x + 225){
            xspeed = 5;
        }
        else if(ballx >= x + 226 && ballx <= x + 265){
            xspeed = 8;
        }
        else if(ballx >= x + 266 && ballx <= x+paddlegetWidth+30){
            xspeed = 11;
        }
    }

    private void collision(){//벽돌 깨지는 함수(가장 중요한 기능!!)
        int start = 3;
        int end = 4;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(j >= start && j <= end) {
                    int blockleft = block[i][j].blockRect.left;
                    int blockright = block[i][j].blockRect.right;
                    int blockbottom = block[i][j].blockRect.bottom;
                    int blocktop = block[i][j].blockRect.top;
                    if (ballx + 40 >= blockleft && ballx <= blockright && bally <= blockbottom && bally + 40 >= blocktop) {
                        block[i][j].blockRect.set(2140000000, 2140000000,
                                2140000000, 2140000000);//벽돌 안드로메다 보내기 ㅋㅋㅋㅋ
                        score += combo + 100;//콤보수에 따른 점수 추가
                        if(bally<=blockbottom&&ballx+40>=blockleft&&ballx<=blockright){
                            yspeed = -5;
                        }
                        yspeed *= -1;//y축 역방향 이동
                        combo++;//콤보수 올리기
                        clear--;//클리어까지 남은 벽돌수
                        if (clear == 0) {
                            playing = false;
                            score = (life + 1) * score;
                            Intent intent = new Intent(getApplicationContext(), GameClear.class);
                            intent.putExtra("score", score);
                            intent.putExtra("life", life);
                            startActivity(intent);
                        }
                    }
                }
            }
            if(i < 3) {
                start--; end++;
            }
            else if(i == 3){
                start -= 0; end += 0;
            }
            else {
                start++; end--;
            }
        }
    }
    private void play(long time){//플레잉 핸들러
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            public void run(){
                v.invalidate();//화면 갱신
                play(30);
            }
        }, time);
    }
    private void losinglife(){//목숨 사라짐
        if(bally > 1300) {//공이 아래로 떨어질 시
            playing = false; //게임 중단
            ballx = x + paddlegetWidth/2;
            bally = 1260;//위치 초기화
            xspeed = 0;//속도 초기화
            yspeed = 0;
            combo = 0;//콤보수 초기화
            if (life <= 0) {//라이프가 -1이 될 시에 게임오버
                Intent intent = new Intent(getApplicationContext(),GameOver.class);
                intent.putExtra("score", score);
                startActivity(intent);//게임오버 창 띄우기
            } else {
                life -= 1;//1목숨 잃음
            }
        }
    }
};

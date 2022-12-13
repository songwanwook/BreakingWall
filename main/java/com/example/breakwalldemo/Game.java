package com.example.breakwalldemo;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.*;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Game extends Activity {
    int life = 3;//목숨
    int score = 0;//점수
    int combo = 0;//벽돌 깨진 콤보수(아직 구현 안됨)
    Bitmap paddle;//패들
    Bitmap ball;//공
    Bitmap lifeball;//목숨 갯수
    Bitmap bga;//배경
    Bitmap randomItem;//아이템
    Bitmap pistol;//총알
    int paddlegetWidth = 250;
    int clear = 0;
    int x = 300;
    int ballx = 435, bally = 1260;
    boolean playing = false;
    boolean ItemDown= false;//아이템이 떨어지게 하는 불리언 변수
    boolean pistolShot = false;//총알이 발사되게 하는 불리언 변수
    view v;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view v = new view(this);
        setContentView(v);
    }
    int itemx, itemy, pistolx, pistoly;
    int a = (int)(Math.random()*7), b = (int)(Math.random()*7);
    Block item;//아이템벽돌
    Block [][] block;//벽돌을 따로 클래스로 분리하였다.(객체지향)
    public class view extends View {
        int xstart = 30, ystart = 50;//x, y축 시작위치
        int bx, by, bwidth, bheight;
        public view(Context context) {
            super(context);
            block = new Block[8][8];//벽돌 객체 생성
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    bx = xstart + 130*i; by =ystart + 60*j;
                    bwidth = xstart + 120+(i*130); bheight = ystart + 50+(60*j);//벽돌 그리는 위치
                    block[i][j] = new Block(bx, by, bwidth, bheight);
                    if(i == a && j == b){
                        item = new Block(bx, by, bwidth, bheight);//랜덤한 위치에 아이템 벽돌 생성
                    }
                    clear++;
                }
            }//벽돌 추가
            itemx = item.blockRect.left+20; itemy = item.blockRect.top;
            pistolx = 2140000000;
            pistoly = 2140000000;
            GameClear();
        }
        public void onDraw(Canvas canvas){//벽돌깨기 UI 그리기
            Paint pnt = new Paint();//페인트 객체 설정
            bga = BitmapFactory.decodeResource(getResources(),R.drawable.breakwallsrc);//배경 설정
            canvas.drawBitmap(bga,0,0,null);
            paddle = BitmapFactory.decodeResource(getResources(),R.drawable.button1);//패들 설정
            canvas.drawBitmap(paddle,x,1300,null);
            ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);//벽돌 깨는 공 그래픽 생성
            canvas.drawBitmap(ball,ballx,bally,null);//벽돌 깨는 공 그리기
            pistol = BitmapFactory.decodeResource(getResources(),R.drawable.pistol);
            canvas.drawBitmap(pistol,pistolx,pistoly,null);//아이템 먹기 전 까지 총알 비활성화
            randomItem = BitmapFactory.decodeResource(getResources(),R.drawable.b);
            canvas.drawBitmap(randomItem,itemx,itemy,null);
            int[] color = {Color.RED,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,Color.MAGENTA,Color.BLACK,Color.GRAY};
            for( int i = 0 ; i < 8 ; i++ ) {
                pnt.setColor(color[i]);//색깔 설정
                for (int j = 0; j < 8; j++) {
                    canvas.drawRect(block[i][j].blockRect,pnt);//벽돌 그리기
                }
            }
            invalidate();//화면 갱신
            ballHandler();//핸들러호출
            itemHandler();
            gunHandler();
            pnt.setColor(color[6]);//검은 색상 설정
            pnt.setTextSize(50);//텍스트 사이즈 50
            canvas.drawText("SCORE : " + score,0,40,pnt);//점수 창
            canvas.drawText("LIFE",500,40,pnt);//목숨 창
            lifeball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
            for(int i = 0; i < life; i++){
                canvas.drawBitmap(lifeball,650 + (50*i),0,pnt);//목숨 -1 만큼 볼 그림
            }
            pnt.setTextSize(100);
            pnt.setColor(Color.WHITE);//흰 색상 설정
            canvas.drawText("REMAIN " + clear,0,1540,pnt);//남은벽돌 창
            losinglife();//목숨을 잃음에 따라 상단 볼 갯수도 사라짐
        }
    }
    int xspeed, yspeed;
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

    private void itemHandler(){
        int itembottom = item.blockRect.bottom;
        int itemleft = item.blockRect.left;
        int itemright = item.blockRect.right;
        int itemtop = item.blockRect.top;
        if(ballx+40>=itemleft&&ballx<=itemright&&bally<=itembottom&&bally+40>=itemtop){//아이템 벽돌 깰 시
            ItemDown = true;//아이템 떨구기 허락
            item.set(2140000000,2140000000,
                    2140000000,2140000000);//아이템 벽돌 삭제
        }
        if(ItemDown){//아이템 떨어지면
            if(itemy>=1280 && itemy <= 1320 && itemx > x-20 && itemx < x + paddlegetWidth + 20){
                itemx = 2140000000;itemy = 2140000000;//아이템 삭제
                pistolx = x + 150; pistoly = 1280;//총알 생성
                score += 100;
                pistolCount =10;
                pistolShot = true;
            }//아이템이 패들에 닿을 경우 또는 바닥에 떨어질 경우
            else{
                itemy += 5;
            }
        }
    }
    int pistolCount;
    private void gunHandler(){
        if(pistolShot){//총알 발사 boolean 변수가 true일때
            if(pistolCount > 0) {//총알이 0개 이상일때
                pistoly -= 10;//총알 발사
                collision();//총알 충돌 함수
                if (pistoly <= 0) {//총알이 화면을 넘어가면
                    pistolCount--;//총알 카운드 감소
                    pistolx = x + 150;//총알 위치 패들 위로 갱신
                    pistoly = 1280;
                }
            }
            else{//총알이 0개
                pistolShot = false;//총알 발사 중지
                pistolx = 2140000000;
                pistoly = 2140000000;//총알 제거
            }
        }
    }
    private void collision(){//벽돌 깨지는 함수(가장 중요한 기능!!)
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int blockleft = block[i][j].blockRect.left;
                int blockright = block[i][j].blockRect.right;
                int blockbottom = block[i][j].blockRect.bottom;
                int blocktop = block[i][j].blockRect.top;
                if(ballx+40>=blockleft&&ballx<=blockright&&bally<=blockbottom&&bally+40>=blocktop){
                    block[i][j].blockRect.set(2140000000,2140000000,
                            2140000000,2140000000);//벽돌 안드로메다 보내기 ㅋㅋㅋㅋ
                    score += combo + 100;//콤보수에 따른 점수 추가
                    if(bally<=blockbottom&&ballx+40>=blockleft&&ballx<=blockright){
                        yspeed = -5;
                    }
                    yspeed *= -1;//y축 역방향 이동
                    combo++;//콤보수 올리기
                    clear--;//클리어까지 남은 벽돌수
                    GameClear();
                }
                if(pistoly<=blockbottom&&pistolx>=blockleft-5&&pistolx<=blockright+5) {
                    block[i][j].blockRect.set(2140000000, 2140000000,
                            2140000000, 2140000000);//벽돌 안드로메다 보내기 ㅋㅋㅋㅋ
                    if (pistolCount > 0) {//총알이 남아있을 경우
                        pistolx = x + 150;
                        pistoly = 1280;//총알 위치 초기화
                        score += combo + 100;//콤보수에 따른 점수 추가
                        pistolCount--;//총알 1개 소비
                        combo++;//콤보수 올리기
                        clear--;//클리어까지 남은 벽돌수
                        GameClear();
                    } else {//총알이 0개
                        pistolx = 2140000000;//총알 제거
                        pistoly = 2140000000;
                        pistolShot = false;//총알 발사 중지
                    }
                }//총알이 벽돌에 닿을때 벽돌을 깨는 코드
            }
        }
    }
    private void GameClear(){
        if(clear < 1) {
            playing = false;
            pistolShot = false;
            score = (life + 1) * score;
            Intent intent = new Intent(getApplicationContext(), Game2.class);
            intent.putExtra("score", score);
            intent.putExtra("life", life);
            intent.putExtra("combo", combo);
            startActivity(intent);
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
    private void getSpeed(){//패들에 닿을 시에 공 속도 변화
        if(ballx >= x + 135 && ballx <= x + 175){
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
        else if(ballx >= x + 176 && ballx <= x + 215){
            xspeed = 2;
        }
        else if(ballx >= x + 216 && ballx <= x + 255){
            xspeed = 5;
        }
        else if(ballx >= x + 256 && ballx <= x + 295){
            xspeed = 8;
        }
        else if(ballx >= x + 296 && ballx <= x+paddlegetWidth+30){
            xspeed = 11;
        }
    }
    private void losinglife(){//목숨 사라짐
        if(bally > 1330) {//공이 아래로 떨어질 시
            playing = false; //게임 중단
            pistolShot = false;
            ballx = x + paddlegetWidth/2;
            bally = 1260;//위치 초기화
            xspeed = 0;//속도 초기화
            yspeed = 0;
            combo = 0;//콤보수 초기화
            if (life <= 0) {//라이프가 -1이 될 시에 게임오버
                Intent intent = new Intent(getApplicationContext(),GameOver.class);
                intent.putExtra("score",score);
                startActivity(intent);//게임오버 창 띄우기
            } else {
                life -= 1;//1목숨 잃음
            }
        }
    }
}

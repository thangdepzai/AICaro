package vn.edu.hust.thangtb.aicaro;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by thang on 3/17/2018.
 */

public class AsyncTaskGame extends android.os.AsyncTask<String, String, Point> {
    private TextView txt;
    private ImageButton[][] Cells;
    private Board caroBoard;
    private int data[];
    private AlphaBetaPrunning ai;
    private int[][] matrix;
    private Activity acc;
    private int currsize=0;


    public AsyncTaskGame(TextView txt, ImageButton[][] cells, Board caroBoard, int[] data, AlphaBetaPrunning ai, int[][] matrix, Activity acc) {
        this.txt = txt;
        Cells = cells;
        this.caroBoard = caroBoard;
        this.data = data;
        this.ai = ai;
        this.matrix = matrix;
        this.acc = acc;
        this.currsize = MainActivity.stAI.size();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        txt.setText("Ai is thinking .....");

    }

    @Override
    protected Point doInBackground(String... strings) {
        Point p= ai.search(caroBoard);
        return  p;

    }
     @Override
    protected void onPostExecute(Point p) {
        super.onPostExecute(p);
        if( MainActivity.turn !=1) MainActivity.stAI.push(new cell(p.x,p.y));
        Cells[p.x][p.y].setBackgroundResource(data[0]);
        Cells[p.x][p.y].setClickable(false);
         Cells[p.x][p.y].getBackground().setColorFilter(Color.parseColor("#303F9F"), android.graphics.PorterDuff.Mode.MULTIPLY);
        caroBoard.set(p.x,p.y,2);
        matrix[p.x][p.y]=2;
        MainActivity.m=p.x;
        MainActivity.n=p.y;
        if(MainActivity.stAI.size() > currsize)  MainActivity.turn =1;
         Log.d("may push",p.x+" "+p.y);
        MainActivity.r =2;
        Log.d("m",MainActivity.m+"");
        Log.d("n",MainActivity.n+"");
        if(checkWin(matrix,p.x,p.y,2)){
            txt.setText("You are loser");
            MainActivity.activeGame =false;
            Toast.makeText(acc.getApplicationContext(),"Loser",Toast.LENGTH_SHORT).show();
            // TODO
        }
        else txt.setText("Your Turn");

    }



    private boolean checkWin(int[][] matrix, int x,int y,int c) {
        if(checkHangNgang(matrix,x,y,c) || checkHangDoc(matrix,x,y,c)
                || checkCheoPhai(matrix,x,y,c)|| checkCheoTrai(matrix,x,y,c)) return true;
        return false;
    }
    private boolean checkCheoPhai(int[][] matrix, int x, int y, int c) {
        for(int i=-4;i<=0;i++){
            int dem=0;
            for(int j =i;j<i+5;j++){
                if(x+j>=0&&x+j<MainActivity.TABLE_HEIGHT&&y-j>=0&&y-j<MainActivity.TABLE_HEIGHT) {
                    if (matrix[x + j][y - j] == c) dem++;
                }
            }
             if(dem==5){
                if(x+i-1>=0&&x+i-1<MainActivity.TABLE_HEIGHT&&y-i+1>=0&&y-i+1<MainActivity.TABLE_HEIGHT&& matrix[x+i-1][y-i+1]!=0&&matrix[x+i-1][y-i+1]!=c)
                {
                    if(x+i+5>=0&&x+i+5<MainActivity.TABLE_HEIGHT&&y-i-5>=0&&y-i-5<MainActivity.TABLE_HEIGHT&& (matrix[x+i+5][y-i-5]!=0&&matrix[x+i+5][y-i-5]!=c))
                    ;
                    else return true;
                }else return true;
            }
        }
        return false;
    }
    private boolean checkCheoTrai(int[][] matrix, int x, int y, int c) {
        for(int i=-4;i<=0;i++){
            int dem=0;
            for(int j =i;j<i+5;j++){
                if(x+j>=0&&x+j<MainActivity.TABLE_HEIGHT&&y+j>=0&&y+j<MainActivity.TABLE_HEIGHT) {
                    if (matrix[x + j][y + j] == c) dem++;
                }
            }
            if(dem==5){
                if(x+i-1>=0&&x+i-1<MainActivity.TABLE_HEIGHT&&y+i-1>=0&&y+i-1<MainActivity.TABLE_HEIGHT&& (matrix[x+i-1][y+i-1]!=0&&matrix[x+i-1][y+i-1]!=c))
                {
                    if(x+i+5>=0&&x+i+5<MainActivity.TABLE_HEIGHT&&y+i+5>=0&&y+i+5<MainActivity.TABLE_HEIGHT&& (matrix[x+i+5][y+i+5]!=0&&matrix[x+i+5][y+i+5]!=c))
                    ;
                    else return true;
                }else return true;
            }
        }
        return false;
    }
     private boolean checkHangDoc(int[][] matrix, int x, int y, int c) {
       for(int k= x-4;k<=x;k++){
            if(k>=0) {
                int dem = 0;
                for (int j = k; j < k + 5; j++) {
                    if(j<MainActivity.TABLE_HEIGHT) {
                        if (matrix[j][y] == c) dem++;
                    }
                }
                if (dem == 5&& (k-1<0|| k+5>MainActivity.TABLE_HEIGHT||(matrix[k-1][y]==0||matrix[k-1][y]==c)||(matrix[k+5][y]==0||matrix[k+5][y]==c))) return true;
            }
        }


        return false;
    }
    private boolean checkHangNgang(int[][] matrix, int x, int y, int c) {
        for(int k= y-4;k<=y;k++){
            if(k>=0) {
                int dem = 0;
                for (int j = k; j < k + 5; j++) {
                    if(j<MainActivity.TABLE_HEIGHT) {
                        if (matrix[x][j] == c) dem++;
                    }
                }
                if (dem ==5 &&(k-1<0|| k+5>MainActivity.TABLE_HEIGHT||(matrix[x][k-1]==0||matrix[x][k-1]==c)||(matrix[x][k+5]==0||matrix[x][k+5]==c))) return true;
            }
        }
        return false;
    }

}

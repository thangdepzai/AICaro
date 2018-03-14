package vn.edu.hust.thangtb.aicaro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {
    Button newG;
    int m,n;
    TableLayout table;
    private int first=0; // 0 Ai, 1 Nguoi
    private int turn=0;
    private int shape =3;
    private int r;
    private int[] dataImage = new int[2];
    private static final int TABLE_WIDTH = 15;
    private static final int TABLE_HEIGHT = 15;
    TextView txtTurn;
    private ImageButton[][] Cells = new ImageButton[TABLE_HEIGHT][TABLE_WIDTH];
    private int[][] MAXTRIX = new int[TABLE_HEIGHT][TABLE_WIDTH];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        txtTurn = findViewById(R.id.txtTurn);
        newG = findViewById(R.id.btnNewGame);
        Setup();
         loadResource();
        desginBoardGame();
        newG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        if(turn ==1){
            txtTurn.setText("Your Turn");
        }else txtTurn.setText("Ai 's Turn");

    }


public void newGame(){
        turn =first;
        for(int i1=0;i1<TABLE_HEIGHT;i1++) {
            for (int j1 = 0; j1 < TABLE_WIDTH; j1++) {
                Cells[i1][j1].setClickable(true);
                Cells[i1][j1].setBackgroundResource(R.drawable.shape_cell);
                MAXTRIX[i1][j1] = 0;
                final int i = i1;
                final int j = j1;

                Cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (turn == 1) {
                            Cells[i][j].setBackgroundResource(dataImage[turn]);
                            Cells[i][j].setClickable(false);


                            m=i;
                            n=j;
                            r=1;
                            MAXTRIX[i][j] = 1;
                            ChangeTurn();
                        } else if (turn == 0) BotPlay();
                    }
                });
            }
        }
         if(first ==0) BotPlay(7,7);

}
    private void BotPlay() {
        make_a_move();
        ChangeTurn();
    }
    private void BotPlay(int x, int y) {
        make_a_move(x,y);
        ChangeTurn();
    }

    private void make_a_move() {
        Random random = new Random();
        int x;
        int y;
        do {
            x = random.nextInt(TABLE_HEIGHT);
            y = random.nextInt(TABLE_HEIGHT);
        }while(MAXTRIX[x][y]!=0);
        Cells[x][y].setBackgroundResource(dataImage[turn]);
        Cells[x][y].setClickable(false);
        m=x;
        n=y;
        r=2;
        MAXTRIX[x][y]=2;
    }
    private void make_a_move(int x,int y) {
        Cells[x][y].setBackgroundResource(dataImage[turn]);
        Cells[x][y].setClickable(false);
        m=x;n=y;
        r=2;
        MAXTRIX[x][y]=2;
    }

    public void loadResource() {
        if (shape == 3) { // nguoi chon X
            dataImage[1] = R.drawable.cross; //X
            dataImage[0] = R.drawable.nought; // O
        }else if(shape ==4){
            dataImage[0] = R.drawable.cross;
            dataImage[1] = R.drawable.nought;
        }
    }
    public void ChangeTurn(){
        if(!checkWin(MAXTRIX,m,n,r)) {
            Log.d("Win","Thua");
            turn = (turn + 1) % 2;
            if (turn == 1) {
                txtTurn.setText("Your Turn");
            } else if(turn ==0) {
                txtTurn.setText("Ai 's Turn");
                BotPlay();
            }
        }else {
            if(turn ==0) Toast.makeText(this,"You Lose !!",Toast.LENGTH_LONG).show();
            else Toast.makeText(this,"You Win !!",Toast.LENGTH_LONG).show();

        }
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
                if(x+j>=0&&x+j<TABLE_HEIGHT&&y-j>=0&&y-j<TABLE_HEIGHT) {
                    if (matrix[x + j][y - j] == c) dem++;
                }
            }
            if(dem==5) return true;
        }
        return false;
    }



    private boolean checkCheoTrai(int[][] matrix, int x, int y, int c) {
        for(int i=-4;i<=0;i++){
            int dem=0;
            for(int j =i;j<i+5;j++){
                if(x+j>=0&&x+j<TABLE_HEIGHT&&y+j>=0&&y+j<TABLE_HEIGHT) {
                    if (matrix[x + j][y + j] == c) dem++;
                }
            }
            if(dem==5) return true;
        }
        return false;
    }
     private boolean checkHangDoc(int[][] matrix, int x, int y, int c) {
       for(int k= x-4;k<=x;k++){
            if(k>=0) {
                int dem = 0;
                for (int j = k; j < k + 5; j++) {
                    if(j<TABLE_HEIGHT) {
                        if (matrix[j][y] == c) dem++;
                    }
                }
                if (dem == 5) return true;
            }
        }


        return false;
    }
    private boolean checkHangNgang(int[][] matrix, int x, int y, int c) {
        for(int k= y-4;k<=y;k++){
            if(k>=0) {
                int dem = 0;
                for (int j = k; j < k + 5; j++) {
                    if(j<TABLE_HEIGHT) {
                        if (matrix[x][j] == c) dem++;
                    }
                }
                if (dem == 5) return true;
            }
        }
        return false;
    }

    public void desginBoardGame(){
        table = findViewById(R.id.GameBoard);
        // Populate the table with stuff
        for (int y = 0; y < TABLE_HEIGHT; y++) {
            final int row = y;
            TableRow r1 = new TableRow(this);

            for (int x = 0; x < TABLE_WIDTH; x++) {
                final int col = x;
                Cells[y][x] = new ImageButton(this);
                Cells[y][x].setBackgroundResource(R.drawable.shape_cell);
                final int finalY = y;
                final int finalX = x;
                Cells[y][x].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(turn==1){
                            Log.d("Load Image ","Chuan bi");
                            Cells[finalY][finalX].setBackgroundResource(dataImage[turn]);
                            Log.d("Load Image ","Xong");
                            Cells[finalY][finalX].setClickable(false);
                            MAXTRIX[finalY][finalX]=1;
                            m = finalY;
                            n =finalX;
                            r=1;
                            ChangeTurn();

                        }else if ( turn ==0) BotPlay();
                    }
                });
                r1.addView(Cells[y][x],100,100);
            }
            table.addView(r1,100*TABLE_HEIGHT,100);
        }
        if(first ==0) BotPlay(7,7);
    }
    public void Setup(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("SetUp");
        shape = Integer.parseInt(bundle.getString("shape"));
        turn = first = Integer.parseInt(bundle.getString("fistPlayer"));

    }



}

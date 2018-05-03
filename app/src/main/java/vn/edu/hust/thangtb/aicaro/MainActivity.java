package vn.edu.hust.thangtb.aicaro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button newG;
    public  static int m,n;
    private TableLayout table;
    private int first=0; // 0 Ai, 1 Nguoi
    public static int turn = 0;
    private int shape =3;
    private Board caro;
    public  static int r;
    private  int maxDepth=3;
    private int[] dataImage = new int[2];
    public static final int TABLE_WIDTH = 15;
    public static final int TABLE_HEIGHT = 15;
    private TextView txtTurn;
    private ImageButton[][] Cells = new ImageButton[TABLE_HEIGHT][TABLE_WIDTH];
    private int[][] MAXTRIX = new int[TABLE_HEIGHT][TABLE_WIDTH];
    private AlphaBetaPrunning ai;
    public static boolean activeGame = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        txtTurn = findViewById(R.id.txtTurn);
         ai = new AlphaBetaPrunning(TABLE_HEIGHT,maxDepth);
        newG = findViewById(R.id.btnNewGame);
        Setup();
         loadResource();
         caro = new Board(TABLE_HEIGHT);
        desginBoardGame();
        newG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        if(turn ==1){
            txtTurn.setText("Your Turn");
        }else txtTurn.setText("Ai 's thinking .....");

    }


public void newGame(){
        activeGame =true;
        turn =first;
        if(turn ==1){
            txtTurn.setText("Your Turn");
        }else txtTurn.setText("Ai 's thinking .....");
        for(int i1=0;i1<TABLE_HEIGHT;i1++) {
            for (int j1 = 0; j1 < TABLE_WIDTH; j1++) {
                Cells[i1][j1].setClickable(true);
                Cells[i1][j1].setBackgroundResource(R.drawable.shape_cell);
                MAXTRIX[i1][j1] = 0;
                caro.set(i1,j1,0);
                final int i = i1;
                final int j = j1;
                Cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (turn == 1&& activeGame) {
                            Cells[i][j].setBackgroundResource(dataImage[turn]);
                            Cells[i][j].setClickable(false);
                            m=i;
                            n=j;
                            r=1;
                            MAXTRIX[i][j] = 1;
                            caro.set(i,j,1);
                            turn =0;
                            if(checkWin(MAXTRIX,m,n,r)){
                                // TODO
                                activeGame = false;
                                txtTurn.setText("You are Winner");
                            }
                            else if(turn ==0 && activeGame && MAXTRIX[i][j] == 1) BotPlay();
                        }
                    }
                });
            }
        }
         if(first ==0 && activeGame) BotPlay(7,7);

}
    private void BotPlay() {
        make_a_move();

    }
    private void BotPlay(int x, int y) {
        make_a_move(x,y);
    }

    private void make_a_move() {
        //Random random = new Random();
        new AsyncTaskGame(txtTurn,Cells,caro,dataImage,ai,MAXTRIX,MainActivity.this).execute();

    }
    private void make_a_move(int x,int y) {
        Cells[x][y].setBackgroundResource(dataImage[turn]);
        Cells[x][y].setClickable(false);
        m=x;n=y;
        r=2;
        MAXTRIX[x][y]=2;
        caro.set(x,y,2);
        turn = 1;
        txtTurn.setText("Your Turn");
    }

    public void loadResource() {
        if (shape == 3) { // nguoi chon X
            dataImage[1] = R.drawable.cross; //X
            dataImage[0] = R.drawable.nought; // O
//            dataImage[1] = R.drawable.x; //X
//            dataImage[0] = R.drawable.o; // O
        }else if(shape ==4){
            dataImage[0] = R.drawable.cross;
            dataImage[1] = R.drawable.nought;
        }
    }
//    public void ChangeTurn(){
//        if(!checkWin(MAXTRIX,m,n,r)) {
//            Log.d("Win","Thua");
//            turn = (turn + 1) % 2;
//            if (turn == 1) {
//                txtTurn.setText("Your Turn");
//            } else if(turn ==0) {
//                txtTurn.setText("Ai 's thinking .....");
//                BotPlay();
//            }
//        }else {
//            if(turn ==0) Toast.makeText(this,"You Lose !!",Toast.LENGTH_LONG).show();
//            else Toast.makeText(this,"You Win !!",Toast.LENGTH_LONG).show();
//
//        }
//    }

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
             if(dem==5){
                if(x+i-1>=0&&x+i-1<TABLE_HEIGHT&&y-i+1>=0&&y-i+1<TABLE_HEIGHT&& matrix[x+i-1][y-i+1]!=0&&matrix[x+i-1][y-i+1]!=c)
                {
                    if(x+i+5>=0&&x+i+5<TABLE_HEIGHT&&y-i-5>=0&&y-i-5<TABLE_HEIGHT&& (matrix[x+i+5][y-i-5]!=0&&matrix[x+i+5][y-i-5]!=c))
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
                if(x+j>=0&&x+j<TABLE_HEIGHT&&y+j>=0&&y+j<TABLE_HEIGHT) {
                    if (matrix[x + j][y + j] == c) dem++;
                }
            }
            if(dem==5){
                if(x+i-1>=0&&x+i-1<TABLE_HEIGHT&&y+i-1>=0&&y+i-1<TABLE_HEIGHT&& (matrix[x+i-1][y+i-1]!=0&&matrix[x+i-1][y+i-1]!=c))
                {
                    if(x+i+5>=0&&x+i+5<TABLE_HEIGHT&&y+i+5>=0&&y+i+5<TABLE_HEIGHT&& (matrix[x+i+5][y+i+5]!=0&&matrix[x+i+5][y+i+5]!=c))
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
                    if(j<TABLE_HEIGHT) {
                        if (matrix[j][y] == c) dem++;
                    }
                }
                if (dem == 5&& (k-1<0|| k+5>TABLE_HEIGHT||(matrix[k-1][y]==0||matrix[k-1][y]==c)||(matrix[k+5][y]==0||matrix[k+5][y]==c))) return true;
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
                if (dem ==5 &&(k-1<0|| k+5>TABLE_HEIGHT||(matrix[x][k-1]==0||matrix[x][k-1]==c)||(matrix[x][k+5]==0||matrix[x][k+5]==c))) return true;
            }
        }
        return false;
    }

    public void desginBoardGame(){
         activeGame = true;
       // int inPixels=(int) getResources().getDimension(R.dimen.imageview_height);
       // int inPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
       int inPixels =100;
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
                        if(turn==1 && activeGame){
                            Cells[finalY][finalX].setBackgroundResource(dataImage[turn]);
                            Cells[finalY][finalX].setClickable(false);
                            MAXTRIX[finalY][finalX]=1;
                            caro.set(finalY,finalX,1);
                            m = finalY;
                            n =finalX;
                            r=1;
                            turn =0;
                             if(checkWin(MAXTRIX,m,n,r)){
                                // TODO
                                 activeGame =false;
                                 txtTurn.setText("You are Winner");
                                 Toast.makeText(MainActivity.this,"Winner",Toast.LENGTH_SHORT).show();
                            }
                            else if(turn ==0 && activeGame && MAXTRIX[finalY][finalX] == 1) BotPlay();

                        }
                    }
                });

                r1.addView(Cells[y][x],inPixels,inPixels);
            }
            table.addView(r1,inPixels*TABLE_HEIGHT,inPixels);
        }
        if(first ==0 && activeGame) BotPlay(7,7);
    }
    public void Setup(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("SetUp");
        shape = Integer.parseInt(bundle.getString("shape"));
        turn = first = Integer.parseInt(bundle.getString("fistPlayer"));
        maxDepth = Integer.parseInt(bundle.getString("level"));

    }



}

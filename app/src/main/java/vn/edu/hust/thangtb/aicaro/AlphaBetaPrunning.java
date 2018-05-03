package vn.edu.hust.thangtb.aicaro;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by thang on 3/15/2018.
 */

public class AlphaBetaPrunning {
        /**
     * X = 1: Human, O = 2: AI
     */
    private String[] caseHuman = {"0110", "01112", "0110102", "21110", "010110",
        "011010", "01110", "011112", "211110", "2111010", "011110", "11111",
        "0111012", "10101011", "0101110", "0111010", "0111102", "110110",
        "011011", "0101112", "11110",";11110","01111;"};
    private String[] caseAI = {"0220", "02221", "0220201", "12220", "020220",
        "022020", "02220", "022221", "122220", "1222020", "022220", "22222",
        "0222021", "20202022", "0202220", "0222020", "0222201", "220220",
        "022022", "0202221", "22220",";22220","02222;"};
    private int[] point = {6, 4, 4, 4, 12, 12, 12, 1000, 1000, 1000, 3000, 10000,
        1000, 3000, 1000, 1000, 1000, 1000, 1000,1000, 1000,1000,1000};
    int n;                               //12
    Random rand;
    int[] defenseScore = {0, 1, 9, 85, 769};
    int[] attackScore = {0, 4, 28, 256, 2308};
    int[][] evaluateSquare;
    private int maxDepth;
    int ai = 2;
    int human = 1;
    int maxSquare;

    /**
     *
     * @param size is size of board
     * @param maxDepth is the search depth
     */
    public AlphaBetaPrunning(int size, int maxDepth) {
        n = size;
        rand = new Random();
        evaluateSquare = new int[n][n];
        maxSquare = 4;
        this.maxDepth = maxDepth;
    }

    /**
     * Set all value of square = 0
     */
    void resetValue() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                evaluateSquare[i][j] = 0;
            }
        }
    }

    /**
     * This method to evaluate each square on board
     * 1 human, 2 AI
     *
     */
    private void tinhDiem_HangNgang(Board b, int Player, int currDong, int currCot){
        int countAI=0;
        int countHuman = 0;
        for (int dem = 0; dem < 5 && currCot + dem <n; dem++) {
            if (b.getSquare()[currDong][currCot+dem] == 2) {
                countAI++;
            }
            else if (b.getSquare()[currDong][currCot+dem] == 1) {
                countHuman++;
            }
        }
        if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (int i = 0; i < 5; i++) {
                        if (b.getSquare()[currDong][currCot + i] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[currDong][currCot + i] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[currDong][currCot + i] += attackScore[countHuman];
                                }
                                // chan 2 dau boi AI
                                if (CheckPoint(currDong, currCot - 1) && CheckPoint(currDong, currCot + 5) && b.getSquare()[currDong][currCot - 1] == 2 && b.getSquare()[currDong][currCot + 5] == 2) {
                                   evaluateSquare[currDong][currCot + i] =0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                   evaluateSquare[currDong][currCot + i] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[currDong][currCot + i] += attackScore[countAI];
                                }
                                // chan 2 dau boi ng
                                if (CheckPoint(currDong, currCot - 1) && CheckPoint(currDong, currCot + 5) && b.getSquare()[currDong][currCot - 1] == 1 && b.getSquare()[currDong][currCot + 5] == 1) {
                                   evaluateSquare[currDong][currCot + i] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(currDong, currCot + i - 1) && b.getSquare()[currDong][currCot + i - 1] == 0) || (CheckPoint(currDong, currCot + i + 1) && b.getSquare()[currDong][currCot + i + 1] == 0))) {
                                evaluateSquare[currDong][currCot + i] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[currDong][currCot + i] *= 2;
                            }
                        }
                    }
                }
            }
    public void tinhDiem_HangDoc(Board b, int Player, int currDong, int currCot){
         int countAI=0;
        int countHuman = 0;
        for (int dem = 0; dem < 5 && currDong + dem <n; dem++) {
            if (b.getSquare()[currDong+dem][currCot] == 2) {
                countAI++;
            }
            else if (b.getSquare()[currDong+dem][currCot] == 1) {
                countHuman++;
            }
        }
        if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (int i = 0; i < 5; i++) {
                        if (b.getSquare()[currDong+i][currCot ] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[currDong+i][currCot] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[currDong+i][currCot ] += attackScore[countHuman];
                                }
                                // chan 2 dau boi AI
                                if (CheckPoint(currDong-1, currCot) && CheckPoint(currDong+5, currCot) && b.getSquare()[currDong-1][currCot] == 2 && b.getSquare()[currDong+5][currCot] == 2) {
                                   evaluateSquare[currDong][currCot + i] =0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                   evaluateSquare[currDong+i][currCot] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[currDong+i][currCot] += attackScore[countAI];
                                }
                                // chan 2 dau boi ng
                                if (CheckPoint(currDong-1, currCot) && CheckPoint(currDong+5, currCot) && b.getSquare()[currDong-1][currCot] == 1 && b.getSquare()[currDong+5][currCot] == 1) {
                                   evaluateSquare[currDong+i][currCot] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(currDong+ i - 1, currCot ) && b.getSquare()[currDong+ i - 1][currCot ] == 0) || (CheckPoint(currDong+ i + 1, currCot ) && b.getSquare()[currDong+ i + 1][currCot ] == 0))) {
                                evaluateSquare[currDong+i][currCot] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[currDong+i][currCot] *= 2;
                            }
                        }
                    }
                }

    }
    private void tinhDiem_HangCheoThuan(Board b, int Player, int currDong, int currCot){
         int countAI=0;
        int countHuman = 0;
        for (int dem = 0; dem < 5 && currDong + dem <n && currCot +dem<n; dem++) {
            if (b.getSquare()[currDong+dem][currCot +dem] == 2) {
                countAI++;
            }
            else if (b.getSquare()[currDong+dem][currCot+dem] == 1) {
                countHuman++;
            }
        }
        if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (int i = 0; i < 5; i++) {
                        if (b.getSquare()[currDong+i][currCot+i ] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[currDong+i][currCot+i] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[currDong+i][currCot +i] += attackScore[countHuman];
                                }
                                // chan 2 dau boi AI
                                if (CheckPoint(currDong-1, currCot-1) && CheckPoint(currDong+5, currCot+5) && b.getSquare()[currDong-1][currCot-1] == 2 && b.getSquare()[currDong+5][currCot+5] == 2) {
                                   evaluateSquare[currDong+i][currCot + i] =0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                   evaluateSquare[currDong+i][currCot+i] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[currDong+i][currCot+i] += attackScore[countAI];
                                }
                                // chan 2 dau boi ng
                                if (CheckPoint(currDong-1, currCot-1) && CheckPoint(currDong+5, currCot+5) && b.getSquare()[currDong-1][currCot-1] == 1 && b.getSquare()[currDong+5][currCot+5] == 1) {
                                   evaluateSquare[currDong+i][currCot+i] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(currDong+ i - 1, currCot+ i - 1 ) && b.getSquare()[currDong+ i - 1][currCot + i - 1] == 0) || (CheckPoint(currDong+ i + 1, currCot+ i + 1 ) && b.getSquare()[currDong+ i + 1][currCot+ i + 1 ] == 0))) {
                                evaluateSquare[currDong+i][currCot+i] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[currDong+i][currCot+i] *= 2;
                            }
                        }
                    }
                }

    }
    private void tinhDiem_CheoNguoc(Board b, int Player, int currDong, int currCot){
          int countAI=0;
        int countHuman = 0;
        for (int dem = 0; dem < 5 && currDong -dem >=0 && currCot +dem<n; dem++) {
            if (b.getSquare()[currDong-dem][currCot +dem] == 2) {
                countAI++;
            }
            else if (b.getSquare()[currDong-dem][currCot+dem] == 1) {
                countHuman++;
            }
        }
        if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (int i = 0; i < 5; i++) {
                        if (b.getSquare()[currDong-i][currCot+i ] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[currDong-i][currCot+i] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[currDong-i][currCot +i] += attackScore[countHuman];
                                }
                                // chan 2 dau boi AI
                               if (CheckPoint(currDong + 1, currCot - 1) && CheckPoint(currDong - 5, currCot + 5) && b.getSquare()[currDong + 1][currCot - 1] == 2 && b.getSquare()[currDong - 5][currCot + 5] == 2) {
                                    evaluateSquare[currDong - i][currCot + i] = 0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                   evaluateSquare[currDong-i][currCot+i] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[currDong-i][currCot+i] += attackScore[countAI];
                                }
                               if (CheckPoint(currDong + 1, currCot - 1) && CheckPoint(currDong - 5, currCot + 5) && b.getSquare()[currDong + 1][currCot - 1] == 1 && b.getSquare()[currDong - 5][currCot + 5] == 1) {
                                    evaluateSquare[currDong - i][currCot + i] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(currDong- i + 1, currCot+ i - 1 ) && b.getSquare()[currDong- i + 1][currCot + i - 1] == 0) || (CheckPoint(currDong- i - 1, currCot+ i + 1 ) && b.getSquare()[currDong- i - 1][currCot+ i + 1 ] == 0))) {
                                evaluateSquare[currDong-i][currCot+i] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[currDong-i][currCot+i] *= 2;
                            }
                        }
                    }
                }
    }
    public void evaluateEachSquare(Board b, int Player) {
        n = b.getSize();
        resetValue();
        int row, colum, i;
        int countAI, countHuman;
        /**
         * Check in rows
         */
        for (row = 0; row < n; row++) {
            for (colum = 0; colum < n; colum++) {
                if(row<n && colum<n-4) tinhDiem_HangNgang(b, Player, row, colum);
                if ( row<n-4 && colum<n) tinhDiem_HangDoc(b, Player, row, colum);
                if(row<n-4 && colum <n-4 ) tinhDiem_HangCheoThuan(b, Player, row, colum);
                if(row<n && colum<n-4) tinhDiem_CheoNguoc(b, Player, row, colum);
            }
        }

        }
    /**
     * This method return a State with the highest value in Board
     *
     * @return a State
     */
    public State getMaxSquare() {
        Point p = new Point(0, 0);
        ArrayList<State> list = new ArrayList();
        int t = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t < evaluateSquare[i][j]) {
                    t = evaluateSquare[i][j];
                    p.set(i,j);
                    list.clear();
                    list.add(new State(p, t));
                } else if (t == evaluateSquare[i][j]) {
                    p.set(i,j);
                    list.add(new State(p, t));
                }
            }
        }

        int x = rand.nextInt(list.size());
        evaluateSquare[list.get(x).getP().x][list.get(x).getP().y] = 0;
        return list.get(x);
    }

    /**
     * Evaluation of the board
     */
    private int evaluationBoard(Board b) {
        String s = ";";
        //check in row and colum (|,__)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s += b.getSquare()[i][j];
            }
            s += ";";
            for (int j = 0; j < n; j++) {
                s += b.getSquare()[j][i];
            }
            s += ";";
        }
        // check on diagonally ( \ )
        for (int i = 0; i < n - 4; i++) {
            for (int j = 0; j < n - i; j++) {
                s += b.getSquare()[j][i + j];
            }
            s += ";";
        }
        // check bottom diagonally ( \ )
        for (int i = n - 5; i > 0; i--) {
            for (int j = 0; j < n - i; j++) {
                s += b.getSquare()[i + j][j];
            }
            s += ";";
        }
        // check on diagonally ( / )
        for (int i = 4; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                s += b.getSquare()[i - j][j];
            }
            s += ";";
        }
        // check bottom diagonally ( / )
        for (int i = n - 5; i > 0; i--) {
            for (int j = n - 1; j >= i; j--) {
                s += b.getSquare()[j][i + n - j - 1];
            }
            s += ";\n";
        }
        //X = 1 human, O = 2 AI;
        String find1, find2;
        int diem = 0;
        for (int i = 0; i < caseHuman.length; i++) {
            find1 = caseAI[i];
            find2 = caseHuman[i];
            diem += point[i] * count(s, find1);
            diem -= point[i] * count(s, find2);
        }
        return diem;
    }

    /**
     * Count the number of "find" in "s"
     */
    public int count(String s, String find) {
        Pattern pattern = Pattern.compile(find);
        Matcher matcher = pattern.matcher(s);
        int i = 0;
        while (matcher.find()) {
            i++;
        }
        return i;
    }

    /**
     * Search moves
     */
    public Point search(Board bb) {
        Board b = new Board(bb.getSize());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b.getSquare()[i][j] = bb.getSquare()[i][j];
            }
        }
        evaluateEachSquare(b, 2);
        ArrayList<State> list = new ArrayList();
        for (int i = 0; i < maxSquare; i++) {
            list.add(getMaxSquare());
        }
        int maxp = Integer.MIN_VALUE;
        ArrayList<State> ListChoose = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 2;
            int t = MinVal(b, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            if (maxp < t) {
                maxp = t;
                ListChoose.clear();
                ListChoose.add(list.get(i));
            } else if (maxp == t) {
                ListChoose.add(list.get(i));
            }
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 0;
        }
        int x = rand.nextInt(ListChoose.size());
        return ListChoose.get(x).getP();
    }

    /**
     * Evaluation for MAX(AI)
     */
    private int MaxVal(Board b, int alpha, int beta, int depth) {
        int val = evaluationBoard(b);
        if (depth >= maxDepth || Math.abs(val) > 3000) {
            return val;
        }
        evaluateEachSquare(b, 2);
        ArrayList<State> list = new ArrayList();
        for (int i = 0; i < maxSquare; i++) {
            list.add(getMaxSquare());
        }
        for (int i = 0; i < list.size(); i++) {
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 2;
            alpha = Math.max(alpha, MinVal(b, alpha, beta, depth + 1));
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 0;
            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }

    /**
     * Evaluation for MIN(Human)
     */
    private int MinVal(Board b, int alpha, int beta, int depth) {
        int val = evaluationBoard(b);
        if (depth >= maxDepth || Math.abs(val) > 3000) {
            return val;
        }
        evaluateEachSquare(b, 1);
        ArrayList<State> list = new ArrayList();
        for (int i = 0; i < maxSquare; i++) {
            list.add(getMaxSquare());
        }
        for (int i = 0; i < list.size(); i++) {
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 1;
            beta = Math.min(beta, MaxVal(b, alpha, beta, depth + 1));
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 0;
            if (alpha >= beta) {
                break;
            }
        }
        return beta;
    }

    /**
     * Check valid
     */
    public boolean CheckPoint(int x, int y) {
        return (x >= 0 && y >= 0 && x < MainActivity.TABLE_HEIGHT && y < MainActivity.TABLE_HEIGHT);
    }

}

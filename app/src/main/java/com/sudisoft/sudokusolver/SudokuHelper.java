package com.sudisoft.sudokusolver;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.DateTimeKeyListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/12.
 */

public class SudokuHelper
{
    public ImageView gameBoard = null;
    public  int boardWidth = 0;
    public  int boardHeight = 0;
    public  int gridWidth = 0;

    public SudokuHelper()
    {
        ResetGameValue();
    }

    public  int gridHeight = 0;
    private Bitmap bitmap = null;

    private String [][] game = new String [9][9];

    public String tag = "";
    public void ResetGameValue()
    {
        for (int i = 0; i <9; i++)
            for (int j =0; j<9; j++)
            {
                game[i][j] = "0";
            }
    }
    public void RandGame()
    {
        ResetGameValue();
        Random r = new Random();
        for (int i=0;i<9;i++)
            for (int j=0;j<9;j++)
            {
                int s = r.nextInt(3);
                int v = r.nextInt(9);
                if (s >1)
                {
                    game[i][j] = String.valueOf(v);
                }
                else
                    game[i][j] = "0";
            }
    }
    public  void SetBoardSize(int h,int w)
    {
        gridHeight = h / 9;
        boardHeight = gridHeight * 9;
        gridWidth = w / 9;
        boardWidth = gridWidth * 9;
    }
    public void SetGird(int x, int y, String value)
    {
        game[x][y] = value;
    }
    public String GetGrid(int row, int col)
    {
        return game[row][col];
    }
    public SudokuHelper CloneSudo()
    {
        SudokuHelper clone = new SudokuHelper();
        clone.gameBoard = this.gameBoard;
        clone.boardHeight = this.boardHeight;
        clone.boardWidth = this.boardWidth;
        clone.gridHeight = this.gridHeight;
        clone.gridWidth = this.gridWidth;
        for (int i=0;i<9; i++)
            for (int j=0; j<9; j++)
                clone.game[i][j] = this.game[i][j];

        return clone;
    }

    //获取某格所在的组的起止坐标
    public SudokuGroup getGridGroup(int row,int col)
    {
        SudokuGroup group = new SudokuGroup();
        group.startRow = (row / 3) * 3 ;
        group.stopRow = group.startRow + 2;
        group.startCol = (col / 3) * 3 ;
        group.stopCol = group.startCol + 2;
        return group;
    }
    public int getGridX(int x)
    {
        return  x / gridWidth ;
    }
    public int getGridY(int y)
    {
        return  y / gridHeight ;
    }

    public Bitmap GroupBitmap(int row, int col)
    {
        Bitmap bitmap = boardBitmap();
        SudokuGroup group  = getGridGroup(row,col);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(Color.RED);
        for (int xx = group.startRow; xx <group.stopRow; xx++)
            for (int yy = group.startCol; yy <group.stopCol; yy++)
            {
                canvas.drawRect(xx * gridWidth,yy * gridHeight, (xx+1) * gridWidth,(yy+1) * gridHeight,paint);
            }
        return bitmap;
    }
    public Bitmap gridBitmap(int x, int y)
    {
        if ((gameBoard == null) || (boardHeight ==0 ) || (boardWidth == 0))
            return null;

        int px = x / gridWidth;
        int py = y / gridHeight;
        Bitmap bitmap = boardBitmap();
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(px * gridWidth,py * gridHeight, (px + 1) * gridWidth  ,(py + 1) * gridHeight,paint);
        paint.setTextSize(gridHeight /3);
        paint.setColor(Color.WHITE);
        canvas.drawText("(" + px + "," + py + ")",px * gridWidth,(int)((py + 0.7) * gridHeight)  , paint);
        return bitmap;
    }
    public Bitmap boardBitmap()
    {
        if ((gameBoard == null) || (boardHeight ==0 ) || (boardWidth == 0))
            return null;
        Bitmap baseBitmap = Bitmap.createBitmap(boardWidth, boardHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.rgb(232,131,13));
        Paint paint = new Paint();
        //画数字格
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        for (int y = 0; y< boardHeight; y = y + gridHeight)
        {
            canvas.drawLine(0,y,boardWidth,y,paint);
        }
        for (int x = 0; x< boardWidth; x= x + gridWidth)
        {
            canvas.drawLine(x,0,x,boardHeight,paint);
        }
        //画组格
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        for (int y = 0; y< boardHeight; y = y + 3 * gridHeight)
        {
            canvas.drawLine(0,y,boardWidth,y,paint);
        }

        for (int x = 0; x< boardWidth; x= x + 3 * gridWidth)
        {
            canvas.drawLine(x,0,x,boardHeight,paint);
        }

        //输出格子内数字

        int px, py;
        for (int x=0; x<9; x++)
            for (int y=0; y<9; y++)
            {
                if (game[x][y].equals("0"))
                {
                    List<String> list = CalcGrid(x,y);

                    int ph = gridHeight /5;
                    px = (int) x * gridWidth + ph/2;
                    py = (int) y * gridHeight +ph;
                    paint.setTextSize(ph);
                    paint.setColor(Color.DKGRAY);
                    for(int i=0; i< list.size(); i++)
                    {
                        canvas.drawText(list.get(i),px + (i / 3) * ph, py + ((i%3) +1) * ph, paint);
                    }
                }
                else
                {
                    px =(int) ((x + 0.3) * gridWidth);
                    py = (int) ((y + 0.7) * gridHeight);
                    String txt = game[x][y];
                    paint.setTextSize(gridHeight /2);
                    paint.setColor(Color.BLACK);
                    canvas.drawText(txt,px +5, py+5, paint);
                    paint.setColor(Color.GREEN);
                    canvas.drawText(txt,px ,py , paint);
                }
            }
        return baseBitmap;
    }
    public ArrayList<String> List19()
    {
        ArrayList<String> list = new ArrayList<String>();
        for (Byte i=1; i <=9; i++)
        {
            list.add(String.valueOf(i));
        }
        return list;
    }
    //检查所有列，行固定为row
    public void CalcColum(List list,int row,int col)
    {
        for (int i = 0; i<9; i++)
        {
            //col列就是本位置，所以跳过row行
            if (i != col)
            {
                String value = game[row][i];
                if (list.contains(value))
                {
                    list.remove(value);
                }
            }
        }
    }
    public Bitmap TestCalcColum(List list,int r,int c)
    {
        Bitmap bitmap = boardBitmap();
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLUE);
        for (int i = 0; i<9; i++)
        {
            if (i != c)
            {
                int px = r * gridWidth;
                int py = i * gridHeight;
                canvas.drawRect(px,py, px + gridWidth, py+gridHeight,paint);
                String value = game[r][i];
                if (list.contains(value))
                {
                    list.remove(value);
                }
            }
        }
        return bitmap;
    }
    //检查所有行，列固定为col
    public void CalcRow(List list,int row,int col)
    {
        for (int i = 0; i<9; i++)
        {
            //行为row即为本位置，跳过
            if (i != row)
            {
                String value = game[i][col];
                if (list.contains(value))
                {
                    list.remove(value);
                }
            }
        }
    }
    public void CalcGroup(List list,int row,int col)
    {
        SudokuGroup group = getGridGroup(row,col);
        for (int i = group.startRow; i<=group.stopRow; i++)
        {
            for (int j = group.startCol; j <= group.stopCol; j++) {
                if (!(i == row && j == col))
                {
                    String value = game[i][j];
                    if (list.contains(value)) {
                        list.remove(value);
                    }
                }
            }
        }
    }
    //自动推算所有可100%推算出来的空格
    public int AutoCalc()
    {
        int nCount = 0;
        boolean isBinggo = false;
        do
        {
            isBinggo = false;
            for(int i=0; i<9; i++)
                for (int j=0; j<9; j++)
                {
                    if (GetGrid(i,j).equals("0")) {
                        List<String> list = CalcGrid(i, j);
                        if (list.size() == 1)
                        {
                            SetGird(i,j,list.get(0));
                            isBinggo = true;
                            nCount ++;
                        }
                    }
                }
        }while(isBinggo);
        return nCount;
    }
    public  List<String> CalcGrid(int r,int c)
    {
        List<String> list = List19();
        CalcRow(list,r,c);
        CalcColum(list,r,c);
        CalcGroup(list,r,c);
        return list;
    }
    public String SaveGame(File path)
    {
        String fileName = "game";
        try
        {
            FileOutputStream os = new FileOutputStream(new File(path, fileName));
            for(int i=0; i<9;i++)
                for (int j=0; j<9;j++)
                {
                    os.write(Integer.parseInt(game[i][j]));
                }
            os.close();
            return "";
        }
        catch (Exception e)
        {
            return e.getMessage().toString();
        }
    }
    public String LoadGame(File path)
    {
        String fileName = "game";
        try
        {
            FileInputStream is = new FileInputStream(new File(path, fileName));
            for(int i=0;i<9; i++)
                for (int j=0; j<9; j++)
                {
                    int v = is.read();
                    game[i][j] = String.valueOf(v);
                }
            is.close();
            return "";
        }
        catch (Exception e)
        {
            return e.getMessage().toString();
        }
    }
    public void test(List<Byte> list)
    {
        Random rand = new Random();
        int size = list.size();
        int p = rand.nextInt(size);
        Byte b = list.get(p);
        if (list.contains(b))
        {
            list.remove(b);
        }
    }
    //拷贝游戏数据
    public GridMap GetMap()
    {
        GridMap amap = new GridMap();
        for(int i=0;i<9;i++)
            for (int j=0;j<9;j++)
            {
                amap.map[i][j] = game[i][j];
            }
        return amap;
    }
    //拷贝游戏数据
    public void SetMap(GridMap map)
    {
        for(int i=0;i<9;i++)
            for (int j=0;j<9;j++)
            {
                game[i][j]  = map.map[i][j];
            }
    }
    //获取一个空白位及其可选数字，可选数字少者优先
    public GridChoice GetBlankGrid()
    {
        List<GridChoice> choices = new ArrayList<>();
        for (int i=0;i<9;i++)
            for (int j=0;j<9;j++)
            {
                if (game[i][j].equals("0"))
                {
                    List list = CalcGrid(i,j);
                    choices.add(new GridChoice(i,j,list));
                }
            }
        for (int i=2; i <= 9;i++)
        {
            for(GridChoice c : choices)
            {
                if (c.choice.size() == i)
                    return c;
            }
        }
        return null;
    }
    public boolean IsParadox()
    {
        for(int i =0; i< 9; i++)
            for (int j=0;j<9;j++)
            {
                if (game[i][j].equals( "0"))
                {
                    List choice = CalcGrid(i,j);
                    if (choice == null || choice.size() == 0)
                        return true;
                }
            }
        return false;
    }
    public boolean IsOK()
    {
        for(int i =0; i< 9; i++)
            for (int j=0;j<9;j++)
            {
                if (game[i][j].equals( "0"))
                {
                    return false;
                }
            }
        return true;
    }

    //试探解
    public static SudokuHelper TryGame(SudokuHelper startGame)
    {
        SudokuHelper nextGame = startGame.CloneSudo();

        GridChoice choice = choice =  nextGame.GetBlankGrid();
        if (choice ==null )
        {
            return nextGame;
        }
        while (choice.choice.size() > 0)
        {
            Random r = new Random();
            int p = r.nextInt(choice.choice.size());
            nextGame.game[choice.row][choice.col] = choice.choice.get(p);
            choice.choice.remove(p);

            nextGame.AutoCalc();
            if (nextGame.IsOK())
                return nextGame;
            if (choice.choice.size() == 0)
                return null;
            if (nextGame.IsParadox())
            {
                nextGame = startGame.CloneSudo();
                nextGame.game[choice.row][choice.col] = choice.choice.get(0);
                return TryGame(nextGame);
            }
            else
            {
                return TryGame(nextGame);
            }
        }
        return null;
    }
    //试探解
    public static SudokuHelper AutoTry(SudokuHelper startGame)
    {
        int tryCount = 0;
        Long startTime = System.currentTimeMillis();
        while (tryCount < 10000 && !startGame.IsOK())
        {
            SudokuHelper oneTry = SudokuHelper.TryGame(startGame);
            tryCount ++;
            if (oneTry.IsOK())
            {
                long time = System.currentTimeMillis() - startTime;
                oneTry.tag = "试探了：" + String.valueOf(tryCount) + "次，耗时(ms):" + String.valueOf(time);
                return  oneTry;
            }
        }
        return null;
    }
}

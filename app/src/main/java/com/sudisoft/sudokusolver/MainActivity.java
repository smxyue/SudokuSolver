package com.sudisoft.sudokusolver;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,View.OnTouchListener ,View.OnLongClickListener
{

    ImageView gameBoard = null;
    int boardWidth;
    int boardHeight;
    boolean isGrid = true;
    SudokuHelper helper;
    int CurrentX;
    int CurrentY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku);
        Button bt = (Button)findViewById(R.id.buttonCacl);
        bt.setOnClickListener(this);
        bt = (Button)findViewById(R.id.buttonTry);
        bt.setOnClickListener(this);
        bt = (Button)findViewById(R.id.buttonLoad);
        bt.setOnClickListener(this);
        bt.setOnLongClickListener(this);
        bt = (Button)findViewById(R.id.buttonSave);
        bt.setOnClickListener(this);

        gameBoard = (ImageView)findViewById(R.id.imageView);
        gameBoard.setOnTouchListener(this);
        helper = new SudokuHelper();
        helper.gameBoard = gameBoard;

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        gameBoard = (ImageView)findViewById(R.id.imageView);
        boardHeight = gameBoard.getHeight();
        boardWidth = gameBoard.getWidth();
        helper.SetBoardSize(boardHeight,boardWidth);
        DrawNewBoard();
        super.onWindowFocusChanged(hasFocus);
    }
    @Override
    public void onClick(View v)
    {
        String msg;
        switch (v.getId()) {
            case R.id.buttonSave:
                msg = helper.SaveGame(getExternalFilesDir("sudisoft"));
                if (msg.equals(""))
                {
                    msg = "游戏已经保存！";
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonLoad:
                msg = helper.LoadGame(getExternalFilesDir("sudisoft"));
                if (msg.equals(""))
                {
                    Bitmap bitmap = helper.boardBitmap();
                    gameBoard.setImageBitmap(bitmap);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"载入游戏失败:" + msg,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonCacl:
                int nCount = helper.AutoCalc();
                Bitmap bitmap = helper.boardBitmap();
                gameBoard.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this,"计算完成:" + nCount,Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonTry:
                SudokuHelper result = SudokuHelper.AutoTry(helper);
                if (result != null)
                {
                    helper = result;
                    Bitmap bm = helper.boardBitmap();
                    gameBoard.setImageBitmap(bm);
                    Toast.makeText(MainActivity.this,"试探成功！" + result.tag,Toast.LENGTH_LONG).show();
                    TextView txt = findViewById(R.id.textViewMsg);
                    txt.setText(result.tag);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"试探失败了！:",Toast.LENGTH_LONG).show();
                }
        }
    }
    @Override
    public boolean onLongClick(View v)
    {
        int id = v.getId();
        switch ( id)
        {
            case R.id.buttonLoad:
                helper.ResetGameValue();
                DrawNewBoard();
        }
        return true;
    }

    public void DrawNewBoard()
    {
        ImageView gameBoard = (ImageView)findViewById(R.id.imageView);

        Bitmap baseBitmap = helper.boardBitmap();
        gameBoard.setImageBitmap(baseBitmap);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);//提示框
        final View view = factory.inflate(R.layout.dialog_input_layout, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editTextNum);//获得输入框对象

        CurrentX = helper.getGridX((int)event.getX());
        CurrentY = helper.getGridY((int)event.getY());

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("请输入当前格数字")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String str = edit.getText().toString();
                                List<String> list = helper.CalcGrid(CurrentX,CurrentY);
                                if (list.contains(str))
                                {
                                    helper.SetGird(CurrentX,CurrentY,str);
                                    Bitmap bitmap = helper.boardBitmap();
                                    gameBoard.setImageBitmap(bitmap);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"此格不能放入" + str,Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton("取消", null).create().show();

        return false;
    }


}

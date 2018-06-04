package com.sudisoft.sudokusolver;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class GridChoice
{
    int row ;
    int col;
    List<String> choice;

    public GridChoice(int r, int c, List<String> lst)
    {
        row = r;
        col = c;
        choice = lst;
    }
}

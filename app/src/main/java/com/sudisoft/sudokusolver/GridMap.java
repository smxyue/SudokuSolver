package com.sudisoft.sudokusolver;

/**
 * Created by Administrator on 2018/4/2.
 */

public class GridMap
{
    String [][] map = new String[9][9];
    public GridMap CopyMap()
    {
        GridMap amap = new GridMap();
        for (int i=0;i<9;i++)
            for (int j=0;j<9;j++)
            {
                amap.map[i][j] = map[i][j];
            }
        return amap;
    }
}

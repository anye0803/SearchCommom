package com.anye.lsearchview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库用来保存历史数据
 * Created by anye on 18-1-31.
 */

public class SHistorySQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "search_demo.db";
    private static int version = 1;


    public SHistorySQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建名为records的表，name为存放搜索历史记录的字段
        sqLiteDatabase.execSQL("create table records(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

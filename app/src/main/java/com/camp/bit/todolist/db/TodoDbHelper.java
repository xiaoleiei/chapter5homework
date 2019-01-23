package com.camp.bit.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.AccessControlContext;

import static com.camp.bit.todolist.db.TodoContract.SQL_CREATE_ENTRIES;
import static com.camp.bit.todolist.db.TodoContract.SQL_DELETE_ENTRIES;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库
    public static final  int DATABASE_VERSION = 2;
    public  static final String DATABASE_NAME = "TODOReader.db";

    public TodoDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

//    public TodoDbHelper(Context context) {
//        super(context, "todo", null, 0);
////    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.SQL_CREATE_ENTRIES);

    }//执行创建语句

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }//执行删除操作

//    public void onDowngrade(SQLiteDatabase db,int oldVersion, int newVersion){
//        onUpgrade(db,oldVersion,newVersion);
//}


}

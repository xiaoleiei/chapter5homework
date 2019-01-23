package com.camp.bit.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {


    private TodoContract() {

    }//构造函数

    //建库
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "note";
        //   public static final String _ID = "id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_STATE = "state";
        // public static final String COLUMN_PRIORITY = "priority";
    }

    // TODO 定义表结构和 SQL 语句常量

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID +" INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_DATE +" INTEGER," +
                    FeedEntry.COLUMN_CONTENT +" TEXT," +
                    FeedEntry.COLUMN_STATE +" INTEGER)";
                //    FeedEntry.COLUMN_NAME_TITLE +"TEXT," +
                 //   FeedEntry.COLUMN_NAME_SUBTITLE +"TEXT)";




    public static final String SQL_DELETE_ENTRIES =
            "DROP　TABLE IF　EXISTS " + FeedEntry.TABLE_NAME;


}

package xyz.mfbrain.puzzle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Chris Young on 2017/7/11.
 */

public class MySQLHelper extends SQLiteOpenHelper {
    public static  final String CREATE_USER="create table User ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "password text)  ";

    public static  final String CREATE_PLAYERINFO="create table PlayerInfo("
            + "id integer primary key autoincrement, "
            + "playername text, "
            + "money integer, "
            + "best_record integer, "
            + "last_record integer) ";

    public static final String CREATE_RANKINGLIST="create table RankingList("
            + "id integer primary key autoincrement, "
            + "playername text"
            + "record int)";



    Context context;

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_PLAYERINFO);
        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table User");
        onCreate(sqLiteDatabase);
        Toast.makeText(context,"Update Success",Toast.LENGTH_SHORT).show();

    }
}
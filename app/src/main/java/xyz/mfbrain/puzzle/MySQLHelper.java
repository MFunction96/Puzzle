package xyz.mfbrain.puzzle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Chris Young on 2017/7/11.
 */

public class MySQLHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "password text)  ";

    public static final String CREATE_PLAYERINFO = "create table PlayerInfo("
            + "id integer primary key autoincrement, "
            + "playername text, "
            + "money integer, "
            + "best_record integer, "
            + "last_record integer) ";

    public static final String CREATE_RANKINGLIST = "create table RankingList("
            + "id integer primary key autoincrement, "
            + "imageid text, "
            + "playername text, "
            + "record1 integer,"
            + "record2 integer,"
            + "record3 integer) ";

    public static final String CREATE_BESTRECORD = "create table BestRecord("
            + "id integer primary key autoincrement, "
            + "imageid text, "
            + "keeper1 text, "
            + "keeper2 text, "
            + "keeper3 text, "
            + "record1 integer,"
            + "record2 integer,"
            + "record3 integer) ";


    private Context context;

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_PLAYERINFO);
        sqLiteDatabase.execSQL(CREATE_RANKINGLIST);
        sqLiteDatabase.execSQL(CREATE_BESTRECORD);
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table User");
        sqLiteDatabase.execSQL("drop table PlayerInfo");
        sqLiteDatabase.execSQL("drop table RankingList");
        sqLiteDatabase.execSQL("drop table BestRecord");
        onCreate(sqLiteDatabase);
        Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();

    }
}

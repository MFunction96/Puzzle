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
            + "password text, "
            + "money integer, "
            + "best_record integer, "
            + "last_record integer) ";
    Context context;

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table User");
        onCreate(sqLiteDatabase);
    }
}

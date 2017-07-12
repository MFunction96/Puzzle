package xyz.mfbrain.puzzle;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Chris Young on 2017/7/11.
 */

public class GameData {
    public static SQLiteDatabase _db;
    public static Users _curuser =new Users();
    public static void set_db(SQLiteDatabase d){
        _db =d;}
    public static void set_curuser(Users u){
        _curuser =u;}
    public static SQLiteDatabase get_db(){return _db;}
    public static Users get_curuser(){return _curuser;}
}

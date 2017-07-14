package xyz.mfbrain.puzzle;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Chris Young on 2017/7/11.
 */

public class GameData {
    private static SQLiteDatabase _db;
    private static Users _curuser = new Users();
    private static String _imageid;
    private static int _gametype=1;
    private static int _gamedifficulty;
    private static String _recordkeeper="noBody";
    private static String _bestrecord="10000";

    public static void set_recordkeeper(String _recordkeeper) {
        GameData._recordkeeper = _recordkeeper;
    }

    public static void set_bestrecord(String _bestrecord) {
        GameData._bestrecord = _bestrecord;
    }

    public static void set_gamedifficulty(int _gamedifficulty) {
        GameData._gamedifficulty = _gamedifficulty;
    }

    public static void set_db(SQLiteDatabase d) {
        _db = d;
    }

    public static void set_curuser(Users u) {
        _curuser = u;
    }

    public static void set_imageid(String id) {
        _imageid = id;
    }

    public static void set_gametype(int type) {
        _gametype = type;
    }

    public static SQLiteDatabase get_db() {
        return _db;
    }

    public static Users get_curuser() {
        return _curuser;
    }

    public static String get_imageid() {
        return _imageid;
    }

    public static int get_gametype() {
        return _gametype;
    }

    public static int get_gamedifficulty() {
        return _gamedifficulty;
    }

    public static String get_recordkeeper() {
        return _recordkeeper;
    }

    public static String get_bestrecord() {
        return _bestrecord;
    }
}

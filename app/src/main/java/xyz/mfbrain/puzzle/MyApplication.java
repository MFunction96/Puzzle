package xyz.mfbrain.puzzle;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Chris Young on 2017/7/19.
 */

public class MyApplication extends Application {
    private  SQLiteDatabase _db;
    private  Users _curuser = new Users();
    private  String _imageid = "0";
    private  int _gametype = 1;
    private  int _gamedifficulty = 2;
    private  String _recordkeeper = "-----";
    private  String _bestrecord = "-----";
    private boolean _islogin = false;
    private ImageAdapter _imageAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        _db=null;
        _curuser = new Users();
        _imageid = "0";
        _gametype = 1;
        _gamedifficulty = 2;
        _recordkeeper = "-----";
        _bestrecord = "-----";
        _islogin = false;
        _imageAdapter=null;
    }

    public ImageAdapter get_imageAdapter() {
        return _imageAdapter;
    }

    public void set_imageAdapter(ImageAdapter _imageAdapter) {
        this._imageAdapter = _imageAdapter;
    }

    public  boolean get_islogin() {
        return _islogin;
    }

    public  void set_islogin(boolean _islogin) {
        this._islogin = _islogin;
    }

    public  void set_recordkeeper(String _recordkeeper) {
        this._recordkeeper = _recordkeeper;
    }

    public void set_bestrecord(String _bestrecord) {
        this._bestrecord = _bestrecord;
    }

    public  void set_gamedifficulty(int _gamedifficulty) {
        this._gamedifficulty = _gamedifficulty;
    }

    public  void set_db(SQLiteDatabase d) {
        _db = d;
    }

    public  void set_curuser(Users u) {
        _curuser = u;
    }

    public void set_imageid(String id) {
        _imageid = id;
    }

    public void set_gametype(int type) {
        _gametype = type;
    }

    public  SQLiteDatabase get_db() {
        return _db;
    }

    public  Users get_curuser() {
        return _curuser;
    }

    public  String get_imageid() {
        return _imageid;
    }

    public int get_gametype() {
        return _gametype;
    }

    public int get_gamedifficulty() {
        return _gamedifficulty;
    }

    public  String get_recordkeeper() {
        return _recordkeeper;
    }

    public  String get_bestrecord() {
        return _bestrecord;
    }
}

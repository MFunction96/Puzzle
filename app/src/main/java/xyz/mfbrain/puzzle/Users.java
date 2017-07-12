package xyz.mfbrain.puzzle;

/**
 * Created by Chris Young on 2017/7/8.
 */

public class Users {
    private int _id;
    private String _username;
    private String _password;
    private int _best_record;
    private int _last_record;
    private int _money;

    Users(){
        _username="?";
        _password="0";
    }

    public void set_id(int i){
        _id =i;
    }
    public void set_username(String n){
        _username =n;
    }
    public void set_password(String p){
        _password =p;
    }
    public void setRecord(int c){
        _last_record =c;
        if(_last_record <= _best_record)
            _best_record = _last_record;
    }
    public void set_money(int m){_money =m;}

    public int get_id(){return _id;}
    public String get_username(){return _username;}
    public String get_password(){return _password;}
    public int get_best_record(){return _best_record;}
    public int get_last_record(){return _last_record;}
    public int get_money(){return _money;}
}

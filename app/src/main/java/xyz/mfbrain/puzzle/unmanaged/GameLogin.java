package xyz.mfbrain.puzzle.unmanaged;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import xyz.mfbrain.puzzle.R;

public class GameLogin extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText _username;
    private EditText _password;
    private Button _btn_sign_in;
    private Button _btn_sign_up;
    private CheckBox _remember;
    private SQLiteDatabase _db;
    private boolean _isValidated = false;
    private Users _user = new Users();
    private SharedPreferences _pref;
    private SharedPreferences.Editor _editor;
    private MyApplication _map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelogin);
       Init();

    }

    public void Init(){
        _map=(MyApplication)this.getApplication();
        _username = (EditText) findViewById(R.id.text_username);
        _username.setOnFocusChangeListener(this);
        _password = (EditText) findViewById(R.id.text_password);
        _password.setOnFocusChangeListener(this);
        _btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        _btn_sign_in.setOnClickListener(this);
        _btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        _btn_sign_up.setOnClickListener(this);
        _remember = (CheckBox) findViewById(R.id.cb_remember);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        _username.setTypeface(typeFace);
        _password.setTypeface(typeFace);
        _btn_sign_in.setTypeface(typeFace);
        _btn_sign_up.setTypeface(typeFace);
        _remember.setTypeface(typeFace);
        _pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = _pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = _pref.getString("username", "");
            String password1 = _pref.getString("password", "");
            _username.setText(account);
            _password.setText(password1);
            _remember.setChecked(true);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        _username.setText(_map.get_curuser().get_username());
        _password.setText(_map.get_curuser().get_password());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                if (_username.getText().toString().equals("")) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT);
                } else {
                    ValidateUser();
                    if (_isValidated) {
                        Cursor cursor = _db.query("PlayerInfo", null, "playername = ?",
                                new String[]{_user.get_username()}, null, null, null);
                        if (cursor.moveToFirst()) {
                            do {
                                _user.set_money(cursor.getInt(cursor.getColumnIndex("money")));
                                _user.setRecord(cursor.getInt(cursor.getColumnIndex("last_record")));
                                _user.set_best_record(cursor.getInt(cursor.getColumnIndex("best_record")));
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        _user.set_isvalidate(true);
                        _map.set_curuser(_user);
                        _editor = _pref.edit();
                        if (_remember.isChecked()) {
                            _editor.putBoolean("remember_password", true);
                            _editor.putString("username", _username.getText().toString());
                            _editor.putString("password", _password.getText().toString());
                        } else {
                            _editor.clear();
                            _username.setText("");
                            _password.setText("");
                        }
                        _editor.apply();
                        _map.set_islogin(true);
                        Intent intent1 = new Intent(this, PreloadActivity.class);
                        startActivity(intent1);
                    } else {
                        Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_sign_up:
                Intent intent2 = new Intent(this, SignUp.class);
                startActivity(intent2);
        }

    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view.getId() == R.id.text_username) {
            if (!view.hasFocus()) {
                if (TextUtils.isEmpty(_username.getText().toString()))
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.text_password) {
            if (!_password.hasFocus()) {
                if (TextUtils.isEmpty(_password.getText().toString())) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void ValidateUser() {
        _db = _map.get_db();
        Cursor cursor = _db.query("User", new String[]{"username", "password"}, "username = ?",
                new String[]{_username.getText().toString()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                _user.set_username(_username.getText().toString());
                _user.set_password(cursor.getString(cursor.getColumnIndex("password")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        _isValidated = _user.get_password().equals(_password.getText().toString());
    }

}

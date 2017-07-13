package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class GameLogin extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText username;
    private EditText password;
    private Button btn_sign_in;
    private Button btn_sign_up;
    private CheckBox remember;
    private SQLiteDatabase _db;
    private boolean isValidated = false;
    private Users _user = new Users();
    private SharedPreferences _pref;
    private SharedPreferences.Editor _editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelogin);
        username = (EditText) findViewById(R.id.text_username);
        username.setOnFocusChangeListener(this);
        password = (EditText) findViewById(R.id.text_password);
        password.setOnFocusChangeListener(this);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(this);
        remember = (CheckBox) findViewById(R.id.cb_remember);

        _pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = _pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = _pref.getString("username", "");
            String password1 = _pref.getString("password", "");
            username.setText(account);
            password.setText(password1);
            remember.setChecked(true);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                ValidateUser();
                if (isValidated) {
                    Cursor cursor = _db.query("PlayerInfo", null, "playername = ?",
                            new String[]{_user.get_username()}, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            _user.set_money(cursor.getInt(cursor.getColumnIndex("money")));
                            _user.setRecord(cursor.getInt(cursor.getColumnIndex("last_record")));
                            _user.set_best_record(cursor.getInt(cursor.getColumnIndex("last_record")));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    _user.set_isvalidate(true);
                    GameData.set_curuser(_user);
                    _editor = _pref.edit();
                    if (remember.isChecked()) {
                        _editor.putBoolean("remember_password", true);
                        _editor.putString("username", username.getText().toString());
                        _editor.putString("password", password.getText().toString());
                    } else {
                        _editor.clear();
                        username.setText("");
                        password.setText("");
                    }
                    _editor.apply();
                    Intent intent1 = new Intent(this, PreloadActivity.class);
                    intent1.putExtra("isLogin", 1);
                    startActivity(intent1);
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
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
                if (TextUtils.isEmpty(username.getText().toString()))
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.text_password) {
            if (!password.hasFocus()) {
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void ValidateUser() {
        _db = GameData.get_db();
        Cursor cursor = _db.query("User", new String[]{"username", "password"}, "username = ?",
                new String[]{username.getText().toString()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                _user.set_username(username.getText().toString());
                _user.set_password(cursor.getString(cursor.getColumnIndex("password")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        isValidated = _user.get_password().equals(password.getText().toString());
    }
}

package xyz.mfbrain.puzzle.unmanaged;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xyz.mfbrain.puzzle.R;

public class SignUp extends AppCompatActivity implements OnClickListener, View.OnFocusChangeListener {
    private EditText _text_name;
    private EditText _text_password;
    private EditText _text_confirm_password;
    private Button _btn_reg;
    private String _username;
    private String _password;
    private String _confirm_password;
    private SQLiteDatabase _db;
    private MyApplication _map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Init();

    }

    public void Init(){
        _map=(MyApplication)this.getApplication();
        _text_name = (EditText) findViewById(R.id.text_reg_name);
        _text_name.setOnFocusChangeListener(this);
        _text_password = (EditText) findViewById(R.id.text_reg_password);
        _text_password.setOnFocusChangeListener(this);
        _text_confirm_password = (EditText) findViewById(R.id.text_reg_password_confirm);
        _text_confirm_password.setOnFocusChangeListener(this);
        _btn_reg = (Button) findViewById(R.id.btn_register);
        _btn_reg.setOnClickListener(this);
        _db = _map.get_db();
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        _text_confirm_password.setTypeface(typeFace);
        _text_password.setTypeface(typeFace);
        _text_name.setTypeface(typeFace);
        TextView textview = (TextView) findViewById(R.id.username);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.password);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.confirm);
        textview.setTypeface(typeFace);
    }

    @Override
    public void onClick(View view) {
        boolean regsucces = true;
        _username = _text_name.getText().toString();
        _password = _text_password.getText().toString();
        _confirm_password = _text_confirm_password.getText().toString();

        //查询数据库
        Cursor cursor = _db.query("User", new String[]{"username"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String n = cursor.getString(cursor.getColumnIndex("username"));
                if (n.equals(_username)) {
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    regsucces = false;
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (regsucces) {
            if (_password.equals(_confirm_password)) {
                RegisterUser();
                _map.get_curuser().set_username(_username);
                _map.get_curuser().set_password(_password);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, GameLogin.class);
                intent.putExtra("justsign", 1);
                startActivity(intent);

            } else {
                Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            _text_name.setText("");
            _text_password.setText("");
            _text_confirm_password.setText("");
        }

    }


    private void RegisterUser() {
        //添加数据
        ContentValues values = new ContentValues();
        values.put("username", _username);
        values.put("password", _password);
        _db.insert("User", null, values);
        values.clear();

        values.put("playername", _username);
        values.put("money", 20);
        values.put("best_record", 0);
        values.put("last_record", 0);
        _db.insert("PlayerInfo", null, values);
        values.clear();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.text_reg_name:
                if (!_text_name.hasFocus())
                    if (TextUtils.isEmpty(_text_name.getText().toString())) {
                        Toast.makeText(SignUp.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (_text_name.getText().toString().contains(" ")) {
                        Toast.makeText(SignUp.this, "用户名中不能包含空格", Toast.LENGTH_SHORT).show();
                        _text_name.setText("");
                    }
                break;
            case R.id.text_reg_password:
                if (!_text_password.hasFocus())
                    if (TextUtils.isEmpty(_text_password.getText().toString())) {
                        Toast.makeText(SignUp.this, "密码不可以为空", Toast.LENGTH_SHORT).show();
                    } else if (_text_password.getText().toString().contains(" ")) {
                        Toast.makeText(SignUp.this, "密码不可以包含空格", Toast.LENGTH_SHORT).show();
                        _text_password.setText("");
                    } else {
                        String p = _text_password.getText().toString();
                        if (p.length() < 6 || p.length() > 8) {
                            Toast.makeText(SignUp.this, "密码应该为6—8位", Toast.LENGTH_SHORT).show();
                            _text_password.setText("");
                        }
                    }
                break;
            case R.id.text_reg_password_confirm:
                if (!_text_confirm_password.hasFocus())
                    if (TextUtils.isEmpty(_text_password.getText().toString())) {
                        Toast.makeText(SignUp.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    } else {
                        String p = _text_confirm_password.getText().toString();
                        if (p.length() < 6 || p.length() > 8) {
                            Toast.makeText(SignUp.this, "密码不一致", Toast.LENGTH_SHORT).show();

                        }
                    }
                _text_confirm_password.setText("");
                break;
        }

    }


}

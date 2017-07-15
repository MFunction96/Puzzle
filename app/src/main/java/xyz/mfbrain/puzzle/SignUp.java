package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements OnClickListener, View.OnFocusChangeListener {
    private EditText text_name;
    private EditText text_password;
    private EditText text_confirm_password;
    private Button btn_reg;
    private String username;
    private String password;
    private String confirm_password;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        text_name = (EditText) findViewById(R.id.text_reg_name);
        text_name.setOnFocusChangeListener(this);
        text_password = (EditText) findViewById(R.id.text_reg_password);
        text_password.setOnFocusChangeListener(this);
        text_confirm_password = (EditText) findViewById(R.id.text_reg_password_confirm);
        btn_reg = (Button) findViewById(R.id.btn_register);
        btn_reg.setOnClickListener(this);
        db = GameData.get_db();
    }

    @Override
    public void onClick(View view) {
        boolean regsucces = true;
        username = text_name.getText().toString();
        password = text_password.getText().toString();
        confirm_password = text_confirm_password.getText().toString();

        //查询数据库
        Cursor cursor = db.query("User", new String[]{"username"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String n = cursor.getString(cursor.getColumnIndex("username"));
                if (n.equals(username)) {
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    regsucces = false;
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (regsucces) {
            if (password.equals(confirm_password)) {
                registerUser();
                GameData.get_curuser().set_username(username);
                GameData.get_curuser().set_password(password);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, GameLogin.class);
                intent.getIntExtra("justsign",1);
                startActivity(intent);

            } else {
                Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            text_name.setText("");
            text_password.setText("");
            text_confirm_password.setText("");
        }

    }


    private void registerUser() {
        //添加数据
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("User", null, values);
        values.clear();

        values.put("playername", username);
        values.put("money", 20);
        values.put("best_record", 0);
        values.put("last_record", 0);
        db.insert("PlayerInfo", null, values);
        values.clear();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.text_reg_name:
                if (!text_name.hasFocus())
                    if (TextUtils.isEmpty(text_name.getText().toString())) {
                        Toast.makeText(SignUp.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (text_name.getText().toString().contains(" ")) {
                        Toast.makeText(SignUp.this, "用户名中不能包含空格", Toast.LENGTH_SHORT).show();
                        text_name.setText("");
                    }
                break;
            case R.id.text_reg_password:
                if (!text_password.hasFocus())
                    if (TextUtils.isEmpty(text_password.getText().toString())) {
                        Toast.makeText(SignUp.this, "密码不可以为空", Toast.LENGTH_SHORT).show();
                    } else if (text_password.getText().toString().contains(" ")) {
                        Toast.makeText(SignUp.this, "密码不可以包含空格", Toast.LENGTH_SHORT).show();
                        text_password.setText("");
                    } else {
                        String p = text_password.getText().toString();
                        if (p.length() < 6 || p.length() > 8) {
                            Toast.makeText(SignUp.this, "密码应该为6—8位", Toast.LENGTH_SHORT).show();
                            text_password.setText("");
                        }
                    }
                break;
            case R.id.text_reg_password_confirm:
                if(!text_confirm_password.hasFocus())
                    if (TextUtils.isEmpty(text_confirm_password.getText().toString())) {
                        Toast.makeText(SignUp.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    } else {
                        String p = text_confirm_password.getText().toString();
                        if (p.length() < 6 || p.length() > 8) {
                            Toast.makeText(SignUp.this, "密码不一致", Toast.LENGTH_SHORT).show();
                            text_confirm_password.setText("");
                        }
                    }
                break;
        }

    }


}

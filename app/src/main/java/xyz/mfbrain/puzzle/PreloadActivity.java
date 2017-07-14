package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by MFunction on 2017/7/7.
 * @author MFunction
 */

public class PreloadActivity extends AppCompatActivity {
    private MySQLHelper _mySQLHelper;
    private int _isLogin=0;
    private TextView _name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题栏
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preload);

        _mySQLHelper = new MySQLHelper(this, "Puzzle._db", null, 5);
        GameData.set_db(_mySQLHelper.getWritableDatabase());
//        ContentValues values = new ContentValues();
//        values.put("username", "aa");
//        values.put("password", "11");
//        GameData.get_db().insert("User", null, values);
//        values.clear();
//
//        for(int i=0;i<9;i++){
//            values.put("imageid",i+"");
//            values.put("keeper1","nobody");
//            values.put("keeper2","nobody");
//            values.put("keeper3","nobody");
//            values.put("record1",100000);
//            values.put("record2",100000);
//            values.put("record3",100000);
//            GameData.get_db().insert("BestRecord", null, values);
//            values.clear();
//        }


        Button _startgame = (Button) findViewById(R.id.start);
        _startgame.setOnClickListener(new StartGame());
        Button _login=(Button)findViewById(R.id.btn_reg);
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PreloadActivity.this,GameLogin.class);
                startActivity(intent);
            }
        });
        _name=(TextView)findViewById(R.id.text_name);
        Intent intent = getIntent();
        _isLogin=intent.getIntExtra("isLogin",0);

        _name = (TextView) findViewById(R.id.text_name);
        _name.setText("欢迎    " + GameData.get_curuser().get_username() + "     !");
        if(_isLogin==1){
            _startgame.setEnabled(true);
        }else{
            _startgame.setEnabled(false);
        }
    }

    private class StartGame implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PreloadActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}

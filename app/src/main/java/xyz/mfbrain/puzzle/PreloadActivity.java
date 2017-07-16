package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
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
 *
 * @author MFunction
 */

public class PreloadActivity extends AppCompatActivity {
    private MySQLHelper _mySQLHelper;
    private TextView _name;

    private Button _ranklist;

    private Button _about;

    private Button _challenge;

    private Button _startgame;

    private Button _login;

    private Button _free;

    private TextView _titlecn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题栏
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preload);
        //更新和初次建立数据库时调用
        _mySQLHelper = new MySQLHelper(this, "Puzzle._db", null, 5);
        GameData.set_db(_mySQLHelper.getWritableDatabase());
        //InitDataBase();
        Init();
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fzstk.ttf");
        _about.setOnClickListener(new About());
        _ranklist.setOnClickListener(new RankList());
        _startgame.setOnClickListener(new StartGame());
        _login.setOnClickListener(new Login());
        _challenge.setOnClickListener(new Challenge());
        _free.setOnClickListener(new Free());
        _about.setTypeface(typeFace);
        _ranklist.setTypeface(typeFace);
        _startgame.setTypeface(typeFace);
        _login.setTypeface(typeFace);
        _challenge.setTypeface(typeFace);
        _titlecn.setTypeface(typeFace);
        GameStatus();
        Music.play(this,R.raw.start);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GameStatus();
    }

    private void Init() {
        _ranklist = (Button) findViewById(R.id.ranklist);
        _about = (Button) findViewById(R.id.about);
        _challenge = (Button) findViewById(R.id.challenge);
        _startgame = (Button) findViewById(R.id.start);
        _login = (Button) findViewById(R.id.btn_reg);
        _name = (TextView) findViewById(R.id.text_name);
        _free = (Button) findViewById(R.id.free);
        _titlecn = (TextView) findViewById(R.id.titlecn);
    }

    private class StartGame implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PreloadActivity.this, AdventureMode.class);
            startActivity(intent);
        }
    }

    private class About implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MyDialog1 dialog1=new MyDialog1(PreloadActivity.this);
            dialog1.initText("该游戏为休闲益智类游戏，其目的是将打乱的图片还原到最初的位置，界面左上方" +
                    "有完整图片供参考。游戏类型分两种，自由模式和闯关模式.闯关模式有固定的图片，玩家要在规定的步数内完成游戏。" +
                    "自由模式玩家可以自由选择图片跟游戏难度，还可以将自己喜欢的图片从本地加载进游戏内。");
            dialog1.show();

        }
    }

    private class Challenge implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try{
                GameData.set_gametype(1);
                Intent intent = new Intent(PreloadActivity.this, MainActivity.class);
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private class Free implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try{
                GameData.set_gametype(3);
                Intent intent = new Intent(PreloadActivity.this, FreeMode.class);
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private class RankList implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PreloadActivity.this, RankingList2.class);
            startActivity(intent);

        }
    }

    private class Login implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (GameData.get_islogin()) {
                GameData.set_curuser(new Users());
                GameData.set_islogin(false);
                GameStatus();
            } else {
                Intent intent = new Intent(PreloadActivity.this, GameLogin.class);
                intent.putExtra("home", 1);
                startActivity(intent);
                GameData.set_islogin(true);
            }
        }
    }

    private class PickPicClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra("crop", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 0);
        }
    }


    private void InitDataBase() {
        ContentValues values = new ContentValues();
        values.put("username", "aa");
        values.put("password", "11");
        GameData.get_db().insert("User", null, values);
        values.clear();

        for (int i = 0; i < 9; i++) {
            values.put("imageid", i + "");
            values.put("keeper1", "nobody");
            values.put("keeper2", "nobody");
            values.put("keeper3", "nobody");
            values.put("record1", 100000);
            values.put("record2", 100000);
            values.put("record3", 100000);
            GameData.get_db().insert("BestRecord", null, values);
            values.clear();
        }
    }

    private void GameStatus() {
        _name.setText("欢迎    " + GameData.get_curuser().get_username() + "     !");
        if (GameData.get_islogin()) {
            _startgame.setVisibility(View.VISIBLE);
            _challenge.setVisibility(View.VISIBLE);
            _ranklist.setVisibility(View.VISIBLE);
            _free.setVisibility(View.VISIBLE);
            _login.setText("注销");
        } else {
            _login.setText("登录");
            _startgame.setVisibility(View.INVISIBLE);
            _challenge.setVisibility(View.INVISIBLE);
            _ranklist.setVisibility(View.INVISIBLE);
            _free.setVisibility(View.INVISIBLE);
        }

    }
}

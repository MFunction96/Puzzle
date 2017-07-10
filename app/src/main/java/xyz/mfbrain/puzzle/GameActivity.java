package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by MFunction on 2017/7/5.
 *
 * @author MFunction
 */

public class GameActivity extends AppCompatActivity {

    private Bitmap _bmp;

    private GameController _gc;

    private GameUtil _gu;

    private HelpClass _hp;

    private TableLayout _tableLayout;

    private int _screenwidth, _screeheight;

    private Button _hintbtn;

    private Button _restartbtn;

    private Button _backmenubtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题栏
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        Init();
        ImageView imageview = (ImageView) findViewById(R.id.imageview);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setAdjustViewBounds(true);
        imageview.setMaxWidth(256);
        imageview.setMaxHeight(256);
        imageview.setPadding(8, 8, 8, 8);
        imageview.setImageBitmap(_bmp);

        _bmp = _gu.zoomBitmap(_bmp, _screenwidth - 50, _screenwidth - 50);
        _gu.fillGameZone(_bmp, MainActivity.GetRows(), MainActivity.GetColumns());

        _gc.randomtable(MainActivity.GetRows(),MainActivity.GetColumns());
    }

    /**
     *
     */
    private void Init() {
        //获取屏幕宽高
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        _screenwidth = dm.widthPixels;
        _screeheight = dm.heightPixels;
        _tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        _hintbtn = (Button) findViewById(R.id.hint);
        _restartbtn = (Button) findViewById(R.id.restart);
        _backmenubtn = (Button) findViewById(R.id.backmenu);
        _hintbtn.setOnClickListener(new Hint());
        _restartbtn.setOnClickListener(new Restart());
        _backmenubtn.setOnClickListener(new BackMenu());
        _bmp = ImageAdapter.FixBmp(BitmapFactory.decodeResource(getResources(), Integer.valueOf(getIntent().getStringExtra("bmpid"))));
        _gc = new GameController(this);
        _gu = new GameUtil(_bmp, _tableLayout, this,_gc);
        _hp = new HelpClass(this,_gc);
        _gc.initarraystep();
    }

    final TableLayout GetTableLayout() {
        return _tableLayout;
    }

    final int GetWidth() {
        return _screenwidth;
    }

    final int GetHeight() {
        return _screeheight;
    }

    final Button GetHintBtn() {
        return _hintbtn;
    }

    final Button GetRestartBtn() {
        return _restartbtn;
    }

    final Button GetBackMenuBtn() {
        return _backmenubtn;
    }

    final Bitmap GetBmp() {
        return _bmp;
    }
    final ImageView GetImageView(int id){
        return (ImageView)findViewById(id);
    }

    private class Hint implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            _hp.start();
        }
    }

    private class Restart implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private class BackMenu implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(GameActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    //新建一个Handler用于接受消息，修改UI界面
    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 此处可以更新UI
            switch (msg.what) {
                case 1:
                    GameController.Idclass idrecover = _gc.TraceStack.pop();
                    _gc.ChangeBitmap(idrecover.id1, idrecover.id2);
                    break;
            }
        }
    };
}

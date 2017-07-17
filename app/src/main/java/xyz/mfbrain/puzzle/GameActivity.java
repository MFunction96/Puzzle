package xyz.mfbrain.puzzle;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
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

    private ImageUtil _gu;

    private HelpClass _hp;

    private TableLayout _tableLayout;

    private int _screenwidth, _screeheight;

    private Button _hintbtn;

    private Button _restartbtn;

    private Button _backmenubtn;
    /**
     * 计时显示
     */
    private int _timerindex = 0;

    private ImageView _imageview;

    private Button _pausegame;

    private boolean _running;

    private TextView _recorder;

    private TextView _record;

    private TextView _player;

    /**
     * UI更新Handler
     */
    private Handler _mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新计时器
                    _mtextviewtimer.setText(String.valueOf(_timerindex));
                    _timerindex++;
                    break;
                default:
                    break;
            }
        }
    };
    // 计时器
    private TextView _mtextviewtimer;
   /* // 计时器类
    public static Timer _mtimer;
    //计时器线程
    public static TimerTask _mtimertask;*/

    /**
     * 创建窗体方法
     *
     * @param savedInstanceState 状态记录
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题栏
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        Init();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Init();
    }

    /**
     * 初始化属性
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
        _pausegame = (Button) findViewById(R.id.pausegame);
        _imageview = (ImageView) findViewById(R.id.imageview);
        _recorder = (TextView) findViewById(R.id.recorder);
        _record = (TextView) findViewById(R.id.record);
        _player = (TextView) findViewById(R.id.player);
        // TextView计时器
        _mtextviewtimer = (TextView) findViewById(R.id.tv_dispaly_time);
        _mtextviewtimer.setText("0");//从0s开始显示
        // 启用计时
        MyTimer.StartTimer(_mhandler);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        _hintbtn.setTypeface(typeFace);
        _record.setTypeface(typeFace);
        _recorder.setTypeface(typeFace);
        _pausegame.setTypeface(typeFace);
        _player.setTypeface(typeFace);
        _backmenubtn.setTypeface(typeFace);
        _restartbtn.setTypeface(typeFace);
        _mtextviewtimer.setTypeface(typeFace);

        TextView textView = (TextView) findViewById(R.id.recorderhint);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.recordhint);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.playerhint);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.timehint);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.secondhint);
        textView.setTypeface(typeFace);

        _pausegame.setOnClickListener(new PauseGame());
        _hintbtn.setOnClickListener(new Hint());
        _restartbtn.setOnClickListener(new Restart());
        _backmenubtn.setOnClickListener(new BackMenu());
        Intent intent = getIntent();
        if (intent.getBooleanExtra("id", false)) {
            _bmp = ImageUtil.FixBmp(BitmapFactory.decodeResource(getResources(), Integer.valueOf(getIntent().getStringExtra("bmp"))));
        } else {
            ContentResolver cr = getContentResolver();
            try {
                _bmp = ImageUtil.FixBmp(BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(intent.getStringExtra("bmp")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        _gc = new GameController(this);
        _gu = new ImageUtil(_tableLayout, this, _gc, this);
        _hp = new HelpClass(this, _gc);
        _gc.set_gu(_gu);
        _gc.initarraystep();
        _running = true;
        _player.setText(GameData.get_curuser().get_username());
        if (GameData.get_gametype() != 3) {
            ShowBestRecord();
        }
        int i = GameData.get_gamedifficulty();
        _bmp = ImageUtil.zoomBitmap(_bmp, _screenwidth - 50, _screenwidth - 50);
        _gu.fillGameZone(_bmp, GameData.get_gamedifficulty(), GameData.get_gamedifficulty());
        _gc.randomtable(GameData.get_gamedifficulty(), GameData.get_gamedifficulty());
        _imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        _imageview.setAdjustViewBounds(true);
        _imageview.setMaxWidth(256);
        _imageview.setMaxHeight(256);
        _imageview.setPadding(8, 8, 8, 8);
        _imageview.setImageBitmap(_bmp);

    }

    final TextView GetRecorder() {
        return _recorder;
    }

    final TextView GetRecord() {
        return _record;
    }

    final TextView GetPlayer() {
        return _player;
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

    final Button GetPauseGame() {
        return _pausegame;
    }

    final Bitmap GetBmp() {
        return _bmp;
    }

    final ImageView GetImageView(int id) {
        return (ImageView) findViewById(id);
    }

    /**
     * 点击帮助按钮的事件监听
     */
    private class Hint implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            _mtextviewtimer.setText(String.valueOf(_timerindex));
            // 停止计时器
            MyTimer.CancelTimer();
            _hp.start();
        }
    }

    /**
     * 点击重新启动按钮的事件监听
     */
    private class Restart implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 停止计时器
            MyTimer.CancelTimer();
            _timerindex = 0;
            // 启用计时
            MyTimer.StartTimer(_mhandler);


            _gc.TraceStack.clear();
            _gu.fillGameZone(_bmp, GameData.get_gamedifficulty(), GameData.get_gamedifficulty());
            _gc.randomtable(GameData.get_gamedifficulty(), GameData.get_gamedifficulty());
        }
    }

    /**
     * 点击返回主菜单按钮事件监听
     */
    private class BackMenu implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            _gc.TraceStack.clear();
            // 停止计时器
            Intent intent = new Intent();
            MyTimer.CancelTimer();
            _timerindex = 0;
            if (GameData.get_gametype() != 3) {
                intent.setClass(GameActivity.this, MainActivity.class);

            } else {
                intent.setClass(GameActivity.this, FreeMode.class);
            }

            startActivity(intent);
        }
    }

    /**
     * 点击暂停游戏按钮的事件监听
     */
    private class PauseGame implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (_running) {
                _pausegame.setText("开始游戏");
                //暂停计时
                MyTimer.CancelTimer();
                _timerindex = 0;

            } else {
                _pausegame.setText("暂停游戏");
                //继续计时
                MyTimer.StartTimer(_mhandler);
            }
            _running = !_running;
            for (int i = 0; i < GameData.get_gamedifficulty(); i++) {
                for (int j = 0; j < GameData.get_gamedifficulty(); j++) {
                    int id = i * 10 + j;
                    GetImageView(id).setClickable(_running);
                }
            }
        }
    }

    /**
     * 新建一个Handler用于接受消息，修改UI界面
     */
    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 此处可以更新UI
            switch (msg.what) {
                case 1:
                    GameController.Idclass idrecover = _gc.TraceStack.pop();
                    _gc.ChangeBitmap(idrecover.id1, idrecover.id2);
                    break;
                case 2:
                    ShowDialog(_hp.stepnumber_help);
                    break;
            }
        }
    };

    /**
     * 停止计时
     *//*
    public void CancelTimer() {
        _mtimer.cancel();
        _mtimertask.cancel();
        _timerindex = 0;
    }

    */

    /**
     * 开始计时
     *//*
    public void StartTimer() {

        // 启用计时器
        _mtimer = new Timer(true);
        // 计时器线程
        _mtimertask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                _mhandler.sendMessage(msg);
            }
        };
        // 每1000ms执行 延迟0s
        _mtimer.schedule(_mtimertask, 0, 1000);
    }*/
    public void SetTimerIndex(int t) {
        _timerindex = t;
    }

    public int GetTimerIndex() {
        return _timerindex;
    }

    //帮助结束后，显示Dialog
    public void ShowDialog(int step_number) {
        //其中的帮助走的步数为stepnumber_help
        if (GameData.get_gametype() == 3) {
            MyDialog1 dialog1 = new MyDialog1(GameActivity.this);
            dialog1.initText("恭喜您，拼图已完成，一共用时" + (_timerindex - 1) + " s");
            dialog1.show();
        } else {
            MyDialog2 dialog = new MyDialog2(GameActivity.this);
            dialog.initText("恭喜您，拼图已完成，一共用时" + (_timerindex - 1) + " s");
            dialog.show();
        }
    }

    public void ShowBestRecord() {
        String name = "";
        String record = "";
        switch (GameData.get_gamedifficulty()) {
            case 2:
                name = "keeper1";
                record = "record1";
                break;
            case 4:
                name = "keeper2";
                record = "record2";
                break;
            case 5:
                name = "keeper3";
                record = "record3";
                break;
        }
        Cursor cursor = GameData.get_db().query("BestRecord", new String[]{name, record}, "imageid=?", new String[]{GameData.get_imageid()}, null, null, null);
        if (cursor.moveToFirst()) {
            _recorder.setText(cursor.getString(cursor.getColumnIndex(name)));
            _record.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(record))));
        }
        cursor.close();


    }
}

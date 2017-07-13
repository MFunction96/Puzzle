package xyz.mfbrain.puzzle;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MFunction on 2017/7/5.
 *
 * @author MFunction
 */

public class GameActivity extends AppCompatActivity {

    private Bitmap _bmp;

    private GameController _gc, _gc_another;

    private GameUtil _gu;

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
    // 计时器类
    public static Timer _mtimer;
    //计时器线程
    public static TimerTask _mtimertask;

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
        _imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        _imageview.setAdjustViewBounds(true);
        _imageview.setMaxWidth(256);
        _imageview.setMaxHeight(256);
        _imageview.setPadding(8, 8, 8, 8);
        _imageview.setImageBitmap(_bmp);

        _bmp = _gu.zoomBitmap(_bmp, _screenwidth - 50, _screenwidth - 50);
        _gu.fillGameZone(_bmp, MainActivity.GetRows(), MainActivity.GetColumns());
        _gc.randomtable(MainActivity.GetRows(), MainActivity.GetColumns());
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
        StartTimer();
        _pausegame.setOnClickListener(new PauseGame());
        _hintbtn.setOnClickListener(new Hint());
        _restartbtn.setOnClickListener(new Restart());
        _backmenubtn.setOnClickListener(new BackMenu());
        Intent intent = getIntent();
        if (intent.getBooleanExtra("id", false)) {
            _bmp = ImageAdapter.FixBmp(BitmapFactory.decodeResource(getResources(), Integer.valueOf(getIntent().getStringExtra("bmp"))));
        } else {
            ContentResolver cr = getContentResolver();
            try {
                _bmp = ImageAdapter.FixBmp(BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(intent.getStringExtra("bmp")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        _gc = new GameController(this);
        _gu = new GameUtil(_bmp, _tableLayout, this, _gc);
        _hp = new HelpClass(this, _gc);
        _gc.set_gu(_gu);
        _gc.initarraystep();
        _running = true;
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
            // 停止计时器
            CancelTimer();
            _mtextviewtimer.setText(String.valueOf(_timerindex));
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
            CancelTimer();
            // 启用计时
            StartTimer();

            _gu.fillGameZone(_bmp, MainActivity.GetRows(), MainActivity.GetColumns());
            _gc.randomtable(MainActivity.GetRows(), MainActivity.GetColumns());
        }
    }

    /**
     * 点击返回主菜单按钮事件监听
     */
    private class BackMenu implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 停止计时器
            CancelTimer();
            Intent intent = new Intent();
            intent.setClass(GameActivity.this, MainActivity.class);
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
                _mtimer.cancel();
                _mtimertask.cancel();
            } else {
                _pausegame.setText("暂停游戏");
                //继续计时
                StartTimer();
            }
            _running = !_running;
            for (int i = 0; i < MainActivity.GetRows(); i++) {
                for (int j = 0; j < MainActivity.GetColumns(); j++) {
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
            }
        }
    };

    /**
     * 停止计时
     */
    public void CancelTimer() {
        _mtimer.cancel();
        _mtimertask.cancel();
        _timerindex = 0;
    }

    /**
     * 开始计时
     */
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
    }

    public void SetTimerIndex(int t) {
        _timerindex = t;
    }

    public int GetTimerIndex() {
        return _timerindex;
    }
}

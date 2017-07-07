package xyz.mfbrain.puzzle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

/**
 * Created by MFunction on 2017/7/5.
 *
 * @author MFunction
 */

public class GameActivity extends AppCompatActivity {

    private Bitmap _bmp;

    private GameController _gc;

    private GameUtil _gu;

    private TableLayout tableLayout;

    private int screen_width, scree_height;

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

        _bmp = _gu.zoomBitmap(_bmp, screen_width - 50, screen_width - 50);
        _gu.fillGameZone(_bmp, MainActivity.GetRows(), MainActivity.GetColumns());
    }

    /**
     *
     */
    private void Init() {
        //获取屏幕宽高
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        screen_width = dm.widthPixels;
        scree_height = dm.heightPixels;
        tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        _hintbtn = (Button) findViewById(R.id.hint);
        _restartbtn = (Button) findViewById(R.id.restart);
        _backmenubtn = (Button) findViewById(R.id.backmenu);
        _bmp = ImageAdapter.FixBmp(BitmapFactory.decodeResource(getResources(), Integer.valueOf(getIntent().getStringExtra("bmpid"))));
        _gu = new GameUtil(_bmp, tableLayout, this);
        _gc = new GameController(this);
    }
}

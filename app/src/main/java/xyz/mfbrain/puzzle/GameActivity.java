package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
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

    private int screen_width,scree_height;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题栏
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*
        获得屏幕宽度和高度
         */
        WindowManager windowManager = this.getWindowManager();
        screen_width = windowManager.getDefaultDisplay().getWidth();
        scree_height = windowManager.getDefaultDisplay().getHeight();
        //设置布局文件
        setContentView(R.layout.activity_game);

        tableLayout=(TableLayout)findViewById(R.id.tablelayout);
        Intent intent = getIntent();
        int _bmpid = Integer.valueOf(intent.getStringExtra("bmpid"));
        _bmp = ImageAdapter.FixBmp(BitmapFactory.decodeResource(getResources(), _bmpid));
        _gc = new GameController(this);
        ImageView imageview = (ImageView) findViewById(R.id.imageview);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setAdjustViewBounds(true);
        imageview.setMaxWidth(256);
        imageview.setMaxHeight(256);
        imageview.setPadding(8, 8, 8, 8);
        imageview.setImageBitmap(_bmp);
        _gu=new GameUtil(_bmp,tableLayout,this);
       _bmp= _gu.zoomBitmap( _bmp, screen_width - 50, screen_width - 50);
        _gu.fillGameZone(_bmp,MainActivity.getRows(),MainActivity.getColumns());
    }
}

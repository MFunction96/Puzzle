package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 *
 */
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private ImageAdapter _ia;
    private RadioGroup _levelgroup;
    private RadioButton _level1;
    private RadioButton _level2;
    private RadioButton _level3;
    private GridView _gridview;
    private Button _startgame;
    private boolean _isid;
    private String _bmp;
    private ImageView imageView;
    /**
     * 检测是否已经选择难度
     */
    private boolean level_chosen;
    /**
     *
     */
    private static int rows = 0;
    /**
     *
     */
    private static int columns = 0;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题栏
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Init();
        setGridview();
        _gridview.setOnItemClickListener(new ItemClickListener());
        _levelgroup.setOnCheckedChangeListener(this);
        _startgame.setOnClickListener(new StartGame());
        _startgame.setEnabled(false);
        Music.play(this, R.raw.background, true);
    }

    private void setGridview() {
        int size=_ia.getCount();
        int length=100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        _gridview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        _gridview.setColumnWidth(itemWidth); // 设置列表项宽
        _gridview.setHorizontalSpacing(5); // 设置列表项水平间距
        _gridview.setStretchMode(GridView.NO_STRETCH);
        _gridview.setNumColumns(size); // 设置列数量=列表集合数
        _gridview.setAdapter(_ia);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.stop(this);
    }

    /**
     *
     */
    private void Init() {
        _levelgroup = (RadioGroup) findViewById(R.id.radioGroup);
        _level1 = (RadioButton) findViewById(R.id.easy);
        _level2 = (RadioButton) findViewById(R.id.middle);
        _level3 = (RadioButton) findViewById(R.id.hard);
        _gridview = (GridView) findViewById(R.id.gridview);
        _startgame = (Button) findViewById(R.id.startgame);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        TextView textView = (TextView) findViewById(R.id.pickimg);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.pickdiff);
        textView.setTypeface(typeFace);
        _level1.setTypeface(typeFace);
        _level2.setTypeface(typeFace);
        _level3.setTypeface(typeFace);
        _startgame.setTypeface(typeFace);
        level_chosen = false;
        _ia = new ImageAdapter(this);
        _bmp = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
/*        _bmp = "";
        level_chosen = false;
        _startgame.setEnabled(false);
        _level1.setChecked(false);
        _level2.setChecked(false);
        _level3.setChecked(false);*/
    }

    /**
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == _level1.getId()) {
            rows = columns = 2;
        } else if (checkedId == _level2.getId()) {
            rows = columns = 4;
        } else if (checkedId == _level3.getId()) {
            rows = columns = 5;
        }
        level_chosen = true;
        if (!_bmp.equals("")) {
            _startgame.setEnabled(true);
        }
        GameData.set_gamedifficulty(rows);
    }


    private class StartGame implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(MainActivity.this, GameActivity.class);
            it.putExtra("id", _isid);
            it.putExtra("bmp", _bmp);
            startActivity(it);
        }
    }

    /**
     *
     */
    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _isid = true;
            _bmp = String.valueOf(_ia.getItemId(position));
            imageView=(ImageView)findViewById(R.id.image_bottom);
            imageView.setImageBitmap((Bitmap)_ia.getItem(position));
            GameData.set_imageid(position + "");
            if (level_chosen) {
                _startgame.setEnabled(true);
            }
        }
    }

    /**
     * 获得行数据
     *
     * @return
     */
    public static int GetRows() {
        return rows;
    }

    /**
     * 获得列数据
     *
     * @return
     */
    public static int GetColumns() {
        return columns;
    }

    final GridView GetGridview() {
        return _gridview;
    }
}

package xyz.mfbrain.puzzle;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.FileNotFoundException;


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
    private Button _pickpicbtn;
    private Button _startgame;
    private boolean _isid;
    private String _bmp;
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
        _gridview.setAdapter(_ia);
        _gridview.setOnItemClickListener(new ItemClickListener());
        _levelgroup.setOnCheckedChangeListener(this);
        _pickpicbtn.setOnClickListener(new PickPicClick());
        _startgame.setOnClickListener(new StartGame());
        _startgame.setEnabled(false);
    }

    /**
     *
     */
    private void Init() {
        _levelgroup = (RadioGroup) findViewById(R.id.radioGroup);
        _level1 = (RadioButton) findViewById(R.id.easy);
        _level2 = (RadioButton) findViewById(R.id.middle);
        _level3 = (RadioButton) findViewById(R.id.hard);
        _pickpicbtn = (Button) findViewById(R.id.pickpic);
        _gridview = (GridView) findViewById(R.id.gridview);
        _startgame = (Button) findViewById(R.id.startgame);
        level_chosen = false;
        _ia = new ImageAdapter(this);
        _bmp = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        _bmp = "";
        level_chosen = false;
        _startgame.setEnabled(false);
        _level1.setChecked(false);
        _level2.setChecked(false);
        _level3.setChecked(false);
    }

    /**
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == _level1.getId()) {
            rows = columns = 3;
        } else if (checkedId == _level2.getId()) {
            rows = columns = 4;
        } else if (checkedId == _level3.getId()) {
            rows = columns = 5;
        }
        level_chosen = true;
        if (!_bmp.equals("")) {
            _startgame.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            _isid = false;
            _bmp = data.getData().toString();
            if (level_chosen) {
                _startgame.setEnabled(true);
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
    private class StartGame implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(MainActivity.this, GameActivity.class);
            it.putExtra("id",_isid);
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
            if (level_chosen) {
                _startgame.setEnabled(true);
            }
        }
    }

    final Button GetPickPicBtn() {
        return _pickpicbtn;
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
}

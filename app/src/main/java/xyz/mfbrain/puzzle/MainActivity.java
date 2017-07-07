package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


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
        setContentView(R.layout.activity_main);
        Init();
        _gridview.setAdapter(_ia);
        _gridview.setOnItemClickListener(new ItemClickListener());
        _levelgroup.setOnCheckedChangeListener(this);
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
        level_chosen = false;
        _ia = new ImageAdapter(this);
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
    }

    /**
     *
     */
    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (level_chosen) { //如果用户已选择难度，则跳转到游戏界面
                Intent it = new Intent();
                it.setClass(MainActivity.this, GameActivity.class);
                it.putExtra("bmpid", String.valueOf(_ia.getItemId(position)));
                startActivity(it);
            } else {  //如果用户未选择难度，则弹出提示对话框，提示选择难度
                final AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
                build.setTitle("提示");
                build.setMessage("请选择游戏难度");
                build.show();
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
}

package xyz.mfbrain.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private ImageAdapter _ia;
    private RadioGroup levelgroup;
    private RadioButton level1;
    private RadioButton level2;
    private RadioButton level3;
    /**
     * 检测是否已经选择难度
     */
    private boolean level_chosen=false;

    private static int rows=0;
    private static int columns=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _ia = new ImageAdapter(this);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(_ia);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(level_chosen){ //如果用户已选择难度，则跳转到游戏界面
                    Intent it = new Intent();
                    it.setClass(MainActivity.this, GameActivity.class);
                    it.putExtra("bmpid", String.valueOf(_ia.getItemId(position)));
                    startActivity(it);
                }else {  //如果用户未选择难度，则弹出提示对话框，提示选择难度
                    final AlertDialog.Builder build=new AlertDialog.Builder(MainActivity.this);
                    build.setTitle("提示");
                    build.setMessage("请选择游戏难度");
                    build.show();
                }

            }
        });

        levelgroup=(RadioGroup)findViewById(R.id.radioGroup);
        level1=(RadioButton)findViewById(R.id.easy);
        level2=(RadioButton)findViewById(R.id.gmiddle);
        level3=(RadioButton)findViewById(R.id.hard);
        levelgroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if(checkedId==level1.getId()){
            rows=columns=3;
        }else if(checkedId==level2.getId()){
            rows=columns=4;
        }else if(checkedId==level3.getId()){
            rows=columns=5;
        }
        level_chosen=true;
    }

    /**
     * 获得行数据
     * @return
     */
    public static int getRows(){return rows;}

    /**
     * 获得列数据
     * @return
     */
    public static int getColumns(){return columns;}

}

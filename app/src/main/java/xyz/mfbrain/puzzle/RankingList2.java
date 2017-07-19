package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class RankingList2 extends AppCompatActivity implements AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

   private MyApplication _map;
    private ImageAdapter _imageAdapter;

    private ListView _listView;

    private GridView _gd;

    private RadioGroup _rg;

    private RankListAdapter _listAdapter;

    private List<Users> _playersList;

    private RadioButton _level1;
    private RadioButton _level2;
    private RadioButton _level3;

    private Button _btn_return;
    private boolean _imagehaschosen = false;
    private boolean _isdifficultyhaschosen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list2);
        Init();
    }

    private void Init() {
        _map=(MyApplication) this.getApplication();
        _playersList = new ArrayList<>();
        _imageAdapter = _map.get_imageAdapter();
        _rg = (RadioGroup) findViewById(R.id.difgroup);
        _rg.setOnCheckedChangeListener(this);
        _btn_return = (Button) findViewById(R.id.list2_btn);
        _btn_return.setOnClickListener(this);
        _level1 = (RadioButton) findViewById(R.id.level1);
        _level2 = (RadioButton) findViewById(R.id.level2);
        _level3 = (RadioButton) findViewById(R.id.level3);
        _listView = (ListView) findViewById(R.id.rankllist2);
        _gd = (GridView) findViewById(R.id.list2_grid);
        _gd.setAdapter(_imageAdapter);
        _gd.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        _map.set_imageid(position + "");
        _imagehaschosen = true;
        if (_isdifficultyhaschosen) {
            ShowRankingList();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == _level1.getId()) {
            _map.set_gamedifficulty(3);
        } else if (checkedId == _level2.getId()) {
            _map.set_gamedifficulty(4);
        } else if (checkedId == _level3.getId()) {
            _map.set_gamedifficulty(5);
        }
        _isdifficultyhaschosen = true;
        ShowRankingList();
    }

    private void InitPlayerList() {
        _playersList.clear();
        SQLiteDatabase _db = _map.get_db();
        int i = 1;
        String s = "record1";
        switch (_map.get_gamedifficulty()) {
            case 3:
                s = "record1";
                break;
            case 4:
                s = "record2";
                break;
            case 5:
                s = "record3";
        }
        Cursor cursor = _db.query("RankingList", new String[]{"playername", s}, "imageid=? ", new String[]{_map.get_imageid()}, null, null, s);
        if (cursor.moveToFirst()) {
            do {
                Users u = new Users();
                u.set_id(i++);
                u.set_username(cursor.getString(cursor.getColumnIndex("playername")));
                u.setRecord(cursor.getInt(cursor.getColumnIndex(s)));
                _playersList.add(u);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void ShowRankingList() {
        if (_imagehaschosen) {
            InitPlayerList();
            _listAdapter = new RankListAdapter(this, R.layout.list_layout, _playersList);
            _listView.setAdapter(_listAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PreloadActivity.class);
        startActivity(intent);
    }
}

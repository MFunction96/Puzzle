package xyz.mfbrain.puzzle.unmanaged;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.mfbrain.puzzle.R;
import xyz.mfbrain.puzzle.models.ImageAdapter;

public class RankingList extends AppCompatActivity {
    private List<Users> _playersList ;
    ImageAdapter _imageAdapter;
    MyApplication _map;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        Init();
        InitPlayerList();
        RankListAdapter _rank_adapter = new RankListAdapter(this, R.layout.list_layout, _playersList);
        ListView _listView = (ListView) findViewById(R.id.rankllist);
        _listView.setAdapter(_rank_adapter);
        Button btnreturn = (Button) findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RankingList.this, PreloadActivity.class);
                startActivity(intent1);
            }
        });
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        btnreturn.setTypeface(typeFace);
    }

    private void Init() {
        _map=(MyApplication)this.getApplication(); 
        _playersList = new ArrayList<>();
        _imageAdapter = _map.get_imageAdapter();
        int i = Integer.parseInt(_map.get_imageid());
        Bitmap bitmap = (Bitmap) _imageAdapter.getItem(i);
        bitmap = ImageUtil.ZoomBitmap(bitmap, 300, 300);
        bitmap = ImageUtil.ToRoundCornerImage(bitmap, 50);
        ImageView imageView = (ImageView) findViewById(R.id.challenge_image);
        imageView.setImageBitmap(bitmap);
        TextView textView = (TextView) findViewById(R.id.challenge_text);
        textView.setText(_map.get_gamedifficulty() + "*" + _map.get_gamedifficulty());
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        textView.setTypeface(typeFace);
        textView = (TextView)findViewById(R.id.rank1diff);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.rank1rank);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.rank1ranking);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.rank1rec);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.rank1title);
        textView.setTypeface(typeFace);
    }

    private void InitPlayerList() {
        SQLiteDatabase _db = _map.get_db();
        int i = 1;
        String s = "record1";  //查询条件变量
        switch (_map.get_gamedifficulty()) {//利用全局变量，读取游戏难度，设置查询条件，以动态查询数据
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
            UpDateDataBase();

        }
    }

    private void UpDateDataBase() {
        _map.set_bestrecord(_playersList.get(0).get_last_record() + "");
        _map.set_recordkeeper(_playersList.get(0).get_username());
        ContentValues values = new ContentValues();
        switch (_map.get_gamedifficulty()) {
            case 3:
                values.put("keeper1", _map.get_recordkeeper());
                values.put("record1", _map.get_bestrecord());
                break;
            case 4:
                values.put("keeper2", _map.get_recordkeeper());
                values.put("record2", _map.get_bestrecord());
                break;
            case 5:
                values.put("keeper3", _map.get_recordkeeper());
                values.put("record3", _map.get_bestrecord());
        }
        _map.get_db().update("BestRecord", values, "imageid=?", new String[]{_map.get_imageid()});
        values.clear();
    }
}

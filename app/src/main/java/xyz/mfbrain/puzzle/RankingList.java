package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RankingList extends AppCompatActivity {
    private List<Users> _playersList=new ArrayList<>();
    private Intent intent;
    private boolean isfromhome=false;
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        Init();
        intent=getIntent();
        if(intent.getIntExtra("home",0)==1){
            isfromhome=true;
        }else{
            isfromhome=false;
        }
        InitPlayerList();
        RankListAdapter _rank_adapter=new RankListAdapter(this,R.layout.list_layout,_playersList);
        ListView _listView=(ListView)findViewById(R.id.rankllist);
        _listView.setAdapter(_rank_adapter);
        Button btnreturn=(Button)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfromhome){
                    Intent intent1=new Intent(RankingList.this,PreloadActivity.class);
                    startActivity(intent1);
                }else{
                    Intent intent2=new Intent(RankingList.this,MainActivity.class);
                    startActivity(intent2);
                }

            }
        });

    }

    private void Init(){
        imageAdapter=new ImageAdapter(this);
        int i=Integer.parseInt(GameData.get_imageid());
        Bitmap bitmap=(Bitmap) imageAdapter.getItem(i);
        bitmap=GameUtil.zoomBitmap(bitmap,300,300);
        ImageView imageView=(ImageView)findViewById(R.id.challenge_image);
        imageView.setImageBitmap(bitmap);
        TextView textView=(TextView)findViewById(R.id.challenge_text);
        textView.setText(GameData.get_gametype()+"*"+GameData.get_gametype());

    }

    private void InitPlayerList(){
        SQLiteDatabase _db=GameData.get_db();
        int i=1;
        String s="record1";
        switch (GameData.get_gamedifficulty()){
            case 2:
                s="record1";
                break;
            case 4:
                s="record2";
                break;
            case 5:
                s="record3";
        }
        Cursor cursor=_db.query("RankingList",new String[]{"playername",s},"imageid=? ",new String[]{GameData.get_imageid()},null,null,s);
        if(cursor.moveToFirst()){
            do{
                Users u=new Users();
                u.set_id(i++);
                u.set_username(cursor.getString(cursor.getColumnIndex("playername")));
                u.setRecord(cursor.getInt(cursor.getColumnIndex(s)));
                _playersList.add(u);
            }while(cursor.moveToNext());
            cursor.close();
          if(!isfromhome){
              UpDateDataBase();
          }
        }
    }

    private void UpDateDataBase(){
        GameData.set_bestrecord( _playersList.get(0).get_last_record()+"");
        GameData.set_recordkeeper(_playersList.get(0).get_username());
        ContentValues values=new ContentValues();
        switch (GameData.get_gamedifficulty()){
            case 2:
                values.put("keeper1",GameData.get_recordkeeper());
                values.put("record1",GameData.get_bestrecord());
                break;
            case 4:
                values.put("keeper2",GameData.get_recordkeeper());
                values.put("record2",GameData.get_bestrecord());
                break;
            case 5:
                values.put("keeper3",GameData.get_recordkeeper());
                values.put("record3",GameData.get_bestrecord());
        }
        GameData.get_db().update("BestRecord",values,"imageid=?",new String[]{GameData.get_imageid()});
        values.clear();
    }
}

package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RankingList extends AppCompatActivity {
    private List<Users> _playersList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        InitPlayerList();
        RankListAdapter _rank_adapter=new RankListAdapter(this,R.layout.list_layout,_playersList);
        ListView _listView=(ListView)findViewById(R.id.rankllist);
        _listView.setAdapter(_rank_adapter);
        Button btnreturn=(Button)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RankingList.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InitPlayerList(){
        SQLiteDatabase _db=GameData.get_db();
        int i=1;
        Cursor cursor=_db.query("PlayerInfo",new String[]{"playername","best_record"},null,null,null,null,"best_record");
        if(cursor.moveToFirst()){
            do{
                Users u=new Users();
                u.set_id(i++);
                u.set_username(cursor.getString(cursor.getColumnIndex("playername")));
                u.set_best_record(cursor.getInt(cursor.getColumnIndex("best_record")));
                _playersList.add(u);
            }while(cursor.moveToNext());
            cursor.close();
        }
    }
}

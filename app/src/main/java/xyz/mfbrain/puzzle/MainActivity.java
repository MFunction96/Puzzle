package xyz.mfbrain.puzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageAdapter _ia;

    private GameController _gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _ia = new ImageAdapter(this);
        _gc = new GameController(this);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(_ia);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //弹出单击的GridView元素的位置
                Toast.makeText(MainActivity.this, _ia.getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    final ImageAdapter GetImageAdapter() {
        return _ia;
    }

}

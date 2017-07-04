package xyz.mfbrain.puzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private PhotoAdapter _pa;

    private GameController _gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _pa = new PhotoAdapter(this);
        _gc = new GameController(this);
    }

    final GridView GetGridView() {
        return (GridView) findViewById(R.id.gridview);
    }

    final PhotoAdapter GetPhotoAdapter() {
        return _pa;
    }

}

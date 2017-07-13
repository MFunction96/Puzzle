package xyz.mfbrain.puzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class AdventureMode extends AppCompatActivity {
    private int level;//关卡数

    private int step_left;//剩余步数

    private ImageView _imageView;

    private ImageAdapter _imageadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventuremode);
        init();
    }

    private void init() {
        _imageView=(ImageView)findViewById(R.id.image);
    }
}

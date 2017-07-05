package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by MFunction on 2017/7/5.
 *
 * @author MFunction
 */

public class GameActivity extends AppCompatActivity {

    private int _imgid;

    private GameController _gc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        _imgid = bundle.getInt("ID");
        _gc = new GameController(this);
        ImageView imageview = (ImageView)findViewById(R.id.imageview);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setAdjustViewBounds(true);
        imageview.setMaxWidth(256);
        imageview.setMaxHeight(256);
        imageview.setPadding(8, 8, 8, 8);
        imageview.setImageResource(_imgid);
    }
}

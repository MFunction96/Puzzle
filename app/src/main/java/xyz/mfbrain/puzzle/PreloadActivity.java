package xyz.mfbrain.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by MFunction on 2017/7/7.
 * @author MFunction
 */

public class PreloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        Button _startgame = (Button) findViewById(R.id.startgame);
        _startgame.setOnClickListener(new StartGame());
    }

    private class StartGame implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PreloadActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}

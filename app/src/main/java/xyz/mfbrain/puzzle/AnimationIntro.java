package xyz.mfbrain.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Lisa on 2017/7/14.
 */

public class AnimationIntro extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ImageView imageView=(ImageView)findViewById(R.id.iv);
        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();

        Button button=(Button)findViewById(R.id.bt_skip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AnimationIntro.this,PreloadActivity.class);
                startActivity(intent);
            }
        });
    }
}

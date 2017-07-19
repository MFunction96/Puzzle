package xyz.mfbrain.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Lisa on 2017/7/14.
 */

public class AnimationIntro extends Activity {
    MyVideoView myVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        myVideoView=(MyVideoView)findViewById(R.id.myVideo);
        //设置播放加载路径
        myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.donghua));
        myVideoView.start();

        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent=new Intent(AnimationIntro.this,PreloadActivity.class);
                startActivity(intent);
            }
        });

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

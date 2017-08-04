package xyz.mfbrain.puzzle.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import xyz.mfbrain.puzzle.R;

/**
 * Created by Lisa on 2017/7/12.
 */

public class MyDialog1 extends Dialog {
    public Context context;
    public View dialogView;
    public String message;    //对话框内容


    public MyDialog1(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog1(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected MyDialog1(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;

    }

    //定义对话框的内容
    public void initText(String s) {
        this.message = s;
    }


    public void donghua() {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        animationSet.addAnimation(rotateAnimation);
        dialogView.startAnimation(rotateAnimation);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogView = View.inflate(context, R.layout.dialog1, null);
        setContentView(dialogView);

        setCanceledOnTouchOutside(false);//点击边缘对话框不消失

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myStyle);  //添加动画


        Button bt_sure = (Button) dialogView.findViewById(R.id.btSure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        TextView textView = (TextView) dialogView.findViewById(R.id.tv_intro);
        textView.setText(message);
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/fzstk.ttf");
        bt_sure.setTypeface(typeFace);
        textView.setTypeface(typeFace);
        textView = (TextView) findViewById(R.id.diatitle);
        textView.setTypeface(typeFace);
    }
}

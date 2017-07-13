package xyz.mfbrain.puzzle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

/**
 * Created by Lisa on 2017/7/12.
 */

public class MyDialog extends Dialog{
    public Context context;
    public View dialogView;
    public String message;    //对话框内容
    public int id;  //选择对话框样式

    public MyDialog(@NonNull Context context,int i) {
        super(context);
        this.context=context;
        this.id=i;
    }

    public MyDialog(@NonNull Context context, @StyleRes int themeResId,int i) {
        super(context, themeResId);
        this.context=context;
        this.id=i;
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener,int i) {
        super(context, cancelable, cancelListener);
        this.context=context;
        this.id=i;
    }

    //定义对话框的内容
    public void initText(String s){
        this.message=s;
    }



    public void donghua(){
        AnimationSet animationSet=new AnimationSet(true);
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        animationSet.addAnimation(rotateAnimation);
        dialogView.startAnimation(rotateAnimation);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogView= View.inflate(context,id,null);
        setContentView(dialogView);

        setCanceledOnTouchOutside(false);//点击边缘对话框不消失

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myStyle);  //添加动画


        Button bt_sure=(Button)dialogView.findViewById(R.id.btSure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button bt_Rank=(Button)dialogView.findViewById(R.id.btRank);
        bt_Rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RankingList.class);
                context.startActivity(intent);
            }
        });

        TextView textView=(TextView)dialogView.findViewById(R.id.tv_intro);
        textView.setText(message);

    }
}

package xyz.mfbrain.puzzle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lisa on 2017/7/13.
 */

public class MyDialog2 extends Dialog {

    public Context context;
    public String message;
    public View dialogView;

    public MyDialog2(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    public MyDialog2(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    protected MyDialog2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }
    //定义对话框的内容
    public void initText(String s){
        this.message=s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogView= View.inflate(context,R.layout.dialog2,null);
        setContentView(dialogView);

        setCanceledOnTouchOutside(false);//点击边缘对话框不消失

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myStyle);  //添加动画

        TextView textView=(TextView)dialogView.findViewById(R.id.tv_intro);
        textView.setText(message);  //设置内容

        Button bt_sure=(Button)dialogView.findViewById(R.id.btSure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button btn_list=(Button)dialogView.findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RankingList.class);
                context.startActivity(intent);
            }
        });
    }
}

package xyz.mfbrain.puzzle;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ASUS-PC on 2017/7/7.
 */
public class HelpClass extends Thread {
    GameActivity _aca;
    GameController _gc;
    int stepnumber_help=0;//点击帮助后需要走的步数
    HelpClass(GameActivity appCompatActivity,GameController gameController) {
        _aca = appCompatActivity;
        _gc=gameController;
    }
    public synchronized void run(){
        while (!_gc.TraceStack.isEmpty()) {
            Message msg = new Message();
            msg.what = 1;
            _aca.handler.sendMessage(msg);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stepnumber_help++;
        }
    }
    //帮助结束后，显示Dialog
   public void showDialog(){
     //其中的帮助走的步数为stepnumber_help
   }

}

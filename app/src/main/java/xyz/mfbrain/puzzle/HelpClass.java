package xyz.mfbrain.puzzle;

import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

/**
 * Created by ASUS-PC on 2017/7/7.
 */
public class HelpClass extends Thread {
    GameActivity _aca;
    GameController _gc;
    int stepnumber_help = 0;//点击帮助后需要走的步数
    MyApplication _map;

    HelpClass(GameActivity appCompatActivity, GameController gameController) {
        _aca = appCompatActivity;
        _gc = gameController;
        _map=(MyApplication) _aca.getApplication();
    }

    public synchronized void run() {
        _aca.GetPauseGame().setClickable(false);
        _aca.GetRestartBtn().setClickable(false);
        _aca.GetHintBtn().setClickable(false);
        set_imageview(false);
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
        _aca.GetPauseGame().setClickable(true);
        _aca.GetRestartBtn().setClickable(true);
        _aca.GetHintBtn().setClickable(true);
        set_imageview(true);
        Message msg = new Message();
        msg.what = 2;
        _aca.handler.sendMessage(msg);
    }

    private void set_imageview(Boolean isRunning) {
        for (int i = 0; i < _map.get_gamedifficulty(); i++) {
            for (int j = 0; j < _map.get_gamedifficulty(); j++) {
                int id = i * 10 + j;
                _aca.GetImageView(id).setClickable(isRunning);
            }

        }
    }
}

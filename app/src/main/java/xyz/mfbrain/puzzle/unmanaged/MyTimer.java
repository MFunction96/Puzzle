package xyz.mfbrain.puzzle.unmanaged;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lv on 2017/7/17.
 */

public class MyTimer {

    // 计时器类
    public static Timer _mtimer;
    //计时器线程
    public static TimerTask _mtimertask;

    //停止计时

    public static void CancelTimer() {
        _mtimer.cancel();
        _mtimertask.cancel();
    }

    // 开始计时

    public static void StartTimer(final Handler _mhandler) {

        // 启用计时器
        _mtimer = new Timer(true);
        // 计时器线程
        _mtimertask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                _mhandler.sendMessage(msg);
            }
        };
        // 每1000ms执行 延迟0s
        _mtimer.schedule(_mtimertask, 0, 1000);
    }
}

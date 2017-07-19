package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by MFunction on 2017/7/4.
 *
 * @author MFunction
 */


class GameController {
    GameActivity _ga;
    ImageUtil _gu;
    private AppCompatActivity _pa;
    int _position = 0;//用于记录空白位置的id
    //用于记录图片的移动轨迹
    private int arraystep[][];
    //用于记录每一步操作的栈，其内存储了一个类，该类内部有两个id，是每一步交换的id
    LinkedList<Idclass> TraceStack = new LinkedList<Idclass>();
    private boolean isAniming;//是否在进行动画

    //记录id的类
    public static class Idclass {
        int id1;
        int id2;
    }

    private int _gameType;
    private boolean _hasplayed;
    private MyApplication _map;

    public int GetPosition() {
        return _position;
    }

    GameController(AppCompatActivity appCompatActivity) {
        _pa = appCompatActivity;
        _map=(MyApplication)_pa.getApplication();
        _gameType = _map.get_gametype();
        _hasplayed = false;
    }

    GameController(GameActivity gameActivity) {
        _ga = gameActivity;
        _map=(MyApplication)_ga.getApplication();
        _gameType = _map.get_gametype();
        _hasplayed = false;

    }

    public void set_gu(ImageUtil imageUtil) {
        _gu = imageUtil;
    }

    //将图片随机打乱
    public void randomtable(int rows, int clos) {
        int direction[] = {1, -1, 10, -10};
        int tag = -10;
        for (int i = 0; i < rows * clos * rows * clos; i++) {
            Random random = new Random();
            if (Math.abs(tag) > 1)
                tag = random.nextInt(2);
            else
                tag = random.nextInt(2) + 2;
            int direct = direction[tag];
            if ((direct + _position) > 0 && (((direct + _position) % 10) < clos) && (((direct + _position) / 10) < rows)) {
                ChangeBitmap(_position + direct, _position);
                Idclass id = new Idclass();
                id.id1 = _position - direct;
                id.id2 = _position;
                TraceStack.push(id);
            }
        }

    }

    public void initarraystep() {
        int rows, columns;
        rows = _map.get_gamedifficulty();
        columns = _map.get_gamedifficulty();
        arraystep = new int[rows][columns];
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                arraystep[m][n] = m * 10 + n;
            }
        }
    }

    public void ChangeBitmap(int id1, int id2) {

        //将两个id对应的imageview的图片交换
        int ten1, ten2, single1, single2, change;
        //通过传进的Id得到对应的imageview
        final ImageView imageview1 = (ImageView) _ga.GetImageView(id1);
        final ImageView imageview2 = (ImageView) _ga.GetImageView(id2);
        //得到两个ImageView的图片
        final Bitmap bitmap1 = ((BitmapDrawable) imageview1.getDrawable()).getBitmap();
        final Bitmap bitmap2 = ((BitmapDrawable) imageview2.getDrawable()).getBitmap();
        //交换两者的图片
        imageview1.setImageBitmap(bitmap2);
        imageview2.setImageBitmap(bitmap1);
        _position = id1;
        ten1 = id1 / 10;
        ten2 = id2 / 10;
        single1 = id1 % 10;
        single2 = id2 % 10;
        change = arraystep[ten1][single1];
        arraystep[ten1][single1] = arraystep[ten2][single2];
        arraystep[ten2][single2] = change;
    }

    public void checkfinish() {
        boolean isfinished = true;
        for (int i = 0; i < _map.get_gamedifficulty(); i++) {
            for (int j = 0; j < _map.get_gamedifficulty(); j++) {
                if (arraystep[i][j] != i * 10 + j) {
                    isfinished = false;
                }
            }
        }
        if (isfinished) {
            SQLiteDatabase _db = _map.get_db();
            ContentValues values = new ContentValues();
            _map.get_curuser().setRecord(_ga.GetTimerIndex() - 1);
            _ga.ShowDialog(_gu.GetStep_Player());
            if (_gameType == 1) {
                //查询数据库
//                Cursor cursor = _db.query("RankingList", new String[]{"playername", "imageid"}, "imageid=? and playername=?", new String[]{_map.get_imageid(), _map.get_curuser().get_username()}, null, null, null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        String n = cursor.getString(cursor.getColumnIndex("playername"));
//                        if (n.equals(_map.get_curuser().get_username())) {
//                            _hasplayed = true;
//                            break;
//                        }
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//                if (_hasplayed) {
//                    switch (_map.get_gamedifficulty()) {
//                        case 2:
//                            values.put("record1", _map.get_curuser().get_last_record());
//                            break;
//                        case 4:
//                            values.put("record2", _map.get_curuser().get_last_record());
//                            break;
//                        case 5:
//                            values.put("record3", _map.get_curuser().get_last_record());
//                            break;
//                    }
//                   // _db.update("RankingList", values, "playername=?", new String[]{_map.get_curuser().get_username()});
//
//                } else
                {
                    values.put("imageid", _map.get_imageid());
                    values.put("playername", _map.get_curuser().get_username());
                    switch (_map.get_gamedifficulty()) {
                        case 2:
                            values.put("record1", _map.get_curuser().get_last_record());
                            break;
                        case 4:
                            values.put("record2", _map.get_curuser().get_last_record());
                            break;
                        case 5:
                            values.put("record3", _map.get_curuser().get_last_record());
                            break;
                    }
                    _db.insert("RankingList", null, values);
                }
            } else if (_gameType == 2) {
                values.put("last_record", _map.get_curuser().get_last_record());
                values.put("best_record", _map.get_curuser().get_best_record());
                _db.update("PlayerInfo", values, "playername=?", new String[]{_map.get_curuser().get_username()});

            }
            values.clear();
            //GameActivity._mtimer.cancel();
            // GameActivity._mtimertask.cancel();

            MyTimer.CancelTimer();
            _ga.SetTimerIndex(0);
        }

    }


}

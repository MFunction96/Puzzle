package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by MFunction on 2017/7/4.
 *
 * @author MFunction
 */


class GameController {
    GameActivity _ga;
    GameUtil _gu;
    private AppCompatActivity _pa;
    int _position = 0;//用于记录空白位置的id
    //用于记录图片的移动轨迹
    private int arraystep[][];
    //用于记录每一步操作的栈，其内存储了一个类，该类内部有两个id，是每一步交换的id
    LinkedList<Idclass> TraceStack = new LinkedList<Idclass>();

    //记录id的类
    public static class Idclass {
        int id1;
        int id2;
    }

    private int _gameType;
    private boolean _hasplayed;

    public int GetPosition() {
        return _position;
    }

    GameController(AppCompatActivity appCompatActivity) {
        _pa = appCompatActivity;
        _gameType = GameData.get_gametype();
        _hasplayed = false;
    }

    GameController(GameActivity gameActivity) {
        _ga = gameActivity;
        _gameType = GameData.get_gametype();
        _hasplayed = false;
    }

    public void set_gu(GameUtil gameUtil) {
        _gu = gameUtil;
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
        rows = MainActivity.GetRows();
        columns = MainActivity.GetColumns();
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
        ImageView imageview1 = (ImageView) _ga.GetImageView(id1);
        ImageView imageview2 = (ImageView) _ga.GetImageView(id2);
        //得到两个ImageView的图片
        Bitmap bitmap1 = ((BitmapDrawable) imageview1.getDrawable()).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable) imageview2.getDrawable()).getBitmap();
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
        for (int i = 0; i < MainActivity.GetRows(); i++) {
            for (int j = 0; j < MainActivity.GetColumns(); j++) {
                if (arraystep[i][j] != i * 10 + j) {
                    isfinished = false;
                }
            }
        }
        if (isfinished) {
            SQLiteDatabase _db = GameData.get_db();
            ContentValues values = new ContentValues();
            GameData.get_curuser().setRecord(_ga.GetTimerIndex() - 1);
            _ga.ShowDialog(_gu.GetStep_Player());
            if (_gameType == 1) {
                //查询数据库
                Cursor cursor = _db.query("RankingList", new String[]{"playername", "imageid"}, "imageid=? and playername=?", new String[]{GameData.get_imageid(), GameData.get_curuser().get_username()}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String n = cursor.getString(cursor.getColumnIndex("playername"));
                        if (n.equals(GameData.get_curuser().get_username())) {
                            _hasplayed = true;
                            break;
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                if (_hasplayed) {
                    values.put("record", GameData.get_curuser().get_last_record());
                    _db.update("RankingList", values, "playername=?", new String[]{GameData.get_curuser().get_username()});

                } else {
                    values.put("imageid", GameData.get_imageid());
                    values.put("playername", GameData.get_curuser().get_username());
                    values.put("record", GameData.get_curuser().get_last_record());
                    _db.insert("RankingList", null, values);
                }
            }
            else if (_gameType == 2) {
                values.put("last_record", GameData.get_curuser().get_last_record());
                values.put("best_record", GameData.get_curuser().get_best_record());
                _db.update("PlayerInfo", values, "playername=?", new String[]{GameData.get_curuser().get_username()});

            }
            values.clear();
            GameActivity._mtimer.cancel();
            GameActivity._mtimertask.cancel();
            _ga.SetTimerIndex(0);
        }

    }


}

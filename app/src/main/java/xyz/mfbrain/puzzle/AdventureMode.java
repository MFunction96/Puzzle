package xyz.mfbrain.puzzle;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Random;

public class AdventureMode extends AppCompatActivity implements Runnable {
    private int level = 0;//关卡数
    private int Maxlevel = 5;
    private int rows = 3;
    private int step_origin = 30;//初试步数
    private int step_left = 30;//剩余步数

    private int position;//空白处的位置

    private int coin_num;//剩余金币的数量

    private int award_coin = 2;//奖励金币的数量

    private int timeleft = 60;//剩余时间

    private int time_origin = 60;//初始时间

    private int last_record;//上次记录

    private ImageView _imageView;

    private Button addstep_prop;

    private Button hint_prop;

    private Button restart_prop;

    private Button addtime;

    private Button addcoin;

    private Bitmap bitmap;

    private int screenwidth;

    private TextView step_left_num;

    private TableLayout tableLayout;
    private static int[] pictures = {R.drawable.lyoko1, R.drawable.lyoko2, R.drawable.lyoko3, R.drawable.lyoko4, R.drawable.lyoko5, R.drawable.lyoko6};

    private TextView curplayer;

    private TextView bestrecord;

    private TextView lastrecord;

    private TextView Timeleft;

    private TextView Coin_num;

    private MyApplication _map;


    /**
     * UI更新Handler
     */
    private Handler _mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新计时器
                    Timeleft.setText(String.valueOf(timeleft));
                    timeleft--;
                    checkfinish();
                    break;
                default:
                    break;
            }
        }
    };

    public void SetTimerLeft(int t) {
        timeleft = t;
    }

    public int GetTimerLeft() {
        return timeleft;
    }


    @Override
    public void run() {
        while (!TraceStack.isEmpty()) {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public class Idclass {
        int id1;
        int id2;
    }

    int golbal_cols, golbal_rows;
    int array[][];
    LinkedList<Idclass> TraceStack = new LinkedList<Idclass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager wm = this.getWindowManager();
        screenwidth = wm.getDefaultDisplay().getWidth();
        setContentView(R.layout.activity_adventuremode);
        _map=(MyApplication)this.getApplication();
        last_record = _map.get_curuser().get_last_record();
        if (last_record > 1) {
            final AlertDialog.Builder question = new AlertDialog.Builder(AdventureMode.this);
            question.setTitle("游戏提示");
            question.setMessage("是否回到上次进度？");
            question.setCancelable(false);
            question.setPositiveButton("读档", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    check(last_record);
                    init();
                    bitmap = zoombitmap(bitmap, screenwidth, screenwidth);
                    _imageView.setImageBitmap(bitmap);
                    chooselevel(rows, rows);
                    step_left_num.setText(String.valueOf(step_left));
                }
            });
            question.setNegativeButton("新游戏", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    init();
                    bitmap = zoombitmap(bitmap, screenwidth, screenwidth);
                    _imageView.setImageBitmap(bitmap);
                    chooselevel(rows, rows);
                    step_left_num.setText(String.valueOf(step_left));
                }
            });
            question.show();
        } else {
            init();
            bitmap = zoombitmap(bitmap, screenwidth, screenwidth);
            _imageView.setImageBitmap(bitmap);
            chooselevel(rows, rows);
            step_left_num.setText(String.valueOf(step_left));
        }
    }

    private void check(int last_record) {
        level = last_record - 1;
        switch (last_record) {
            case 1:
                rows = 3;
                break;
            case 2:
                rows = 3;
                break;
            case 3:
                rows = 4;
                time_origin = time_origin + 60;
                step_origin += 20;
                step_left = step_origin;
                timeleft = time_origin;
                break;
            case 4:
                rows = 4;
                time_origin = time_origin + 60;
                step_origin += 20;
                step_left = step_origin;
                timeleft = time_origin;
                break;
            case 5:
                rows = 5;
                time_origin = time_origin + 120;
                step_origin += 40;
                step_left = step_origin;
                timeleft = time_origin;
                break;
            case 6:
                rows = 5;
                time_origin = time_origin + 120;
                step_origin += 40;
                step_left = step_origin;
                timeleft = time_origin;
                break;
        }
    }

    private void init() {
        bestrecord = (TextView) findViewById(R.id.bestrecord);

        lastrecord = (TextView) findViewById(R.id.lastrecord);

        //curplayer = (TextView) findViewById(R.id.curplayer);

        _imageView = (ImageView) findViewById(R.id.imageview_adventure);

        addstep_prop = (Button) findViewById(R.id.addstep);

        hint_prop = (Button) findViewById(R.id.hint_prop);

        Coin_num = (TextView) findViewById(R.id.coin_num);

        addcoin = (Button) findViewById(R.id.addcoin);

        restart_prop = (Button) findViewById(R.id.restart_prop);

        tableLayout = (TableLayout) findViewById(R.id.tablelayout);

        step_left_num = (TextView) findViewById(R.id.step_left_num);

        Timeleft = (TextView) findViewById(R.id.time_left);

        addtime = (Button) findViewById(R.id.addtime);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        bestrecord.setTypeface(typeFace);
        lastrecord.setTypeface(typeFace);
        addstep_prop.setTypeface(typeFace);
        hint_prop.setTypeface(typeFace);
        restart_prop.setTypeface(typeFace);
        Coin_num.setTypeface(typeFace);
        step_left_num.setTypeface(typeFace);
        Timeleft.setTypeface(typeFace);
        addtime.setTypeface(typeFace);

        TextView textview = (TextView) findViewById(R.id.textView2);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.bestrecordhint);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.lastestrecordhint);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.textView3);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.lefttimehint);
        textview.setTypeface(typeFace);
        textview = (TextView) findViewById(R.id.second);
        textview.setTypeface(typeFace);
        coin_num = 0;
        Timeleft.setText(String.valueOf(timeleft));

        // 启用计时
        MyTimer.StartTimer(_mhandler);

        bitmap = BitmapFactory.decodeResource(getResources(), pictures[level]);


        addstep_prop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkcoinnumber(3)) {
                    step_left = step_left + 5;
                    step_left_num.setText(String.valueOf(step_left));
                } else {
                    Toast.makeText(AdventureMode.this, "金币不足", Toast.LENGTH_SHORT).show();
                }

            }
        });
        addtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkcoinnumber(3)) {
                    timeleft = timeleft + 10;
                    Timeleft.setText(String.valueOf(timeleft));
                } else {
                    Toast.makeText(AdventureMode.this, "金币不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder shop = new AlertDialog.Builder(AdventureMode.this);
                shop.setTitle("金币商店");
                shop.setMessage("增加10金币，需花费10元人民币");
                shop.setPositiveButton("确定购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        coin_num = coin_num + 10;
                        Coin_num.setText(String.valueOf(coin_num));
                    }
                });
                shop.show();
            }
        });

        hint_prop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkcoinnumber(20)) {
                    Thread thread = new Thread(AdventureMode.this);
                    thread.start();
                } else {
                    Toast.makeText(AdventureMode.this, "金币不足", Toast.LENGTH_SHORT).show();
                }

            }
        });
        restart_prop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TraceStack.clear();
                bitmap = BitmapFactory.decodeResource(getResources(), pictures[level]);
                bitmap = zoombitmap(bitmap, screenwidth, screenwidth);
                _imageView.setImageBitmap(bitmap);
                chooselevel(rows, rows);
                step_left = step_origin;
                step_left_num.setText(String.valueOf(step_left));
                timeleft = time_origin;
                Timeleft.setText(String.valueOf(timeleft));

                // 停止计时器
                MyTimer.CancelTimer();
                // 启用计时
                MyTimer.StartTimer(_mhandler);
            }
        });

        //curplayer.setText(_map.get_curuser().get_username());
        UpdateInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UpdateInfo();
        // 停止计时器
        MyTimer.CancelTimer();
        // 启用计时
        MyTimer.StartTimer(_mhandler);
    }

    private void chooselevel(final int rows, final int cols) {
        golbal_rows = rows;
        golbal_cols = cols;
        array = new int[rows][cols];
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < cols; n++) {
                array[m][n] = m * 10 + n;
            }
        }
        position = (rows - 1) * 10 + (cols - 1);
        tableLayout.removeAllViewsInLayout();
        TableRow.LayoutParams tlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        tlp.setMargins(3, 3, 0, 0);
        tlp.weight = 1;
        int blockwidth = bitmap.getWidth() / cols;
        int blockhight = bitmap.getHeight() / rows;
        for (int i = 0; i < rows; i++) {
            final TableRow tableRow = new TableRow(this);
            final int imageid[][] = new int[rows][cols];
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ));
            for (int j = 0; j < cols; j++) {
                ImageView curimg = new ImageView(this);
                curimg.setId(i * 10 + j);
                imageid[i][j] = curimg.getId();
                curimg.setScaleType(ImageView.ScaleType.FIT_XY);
                curimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();//获得需要改变的id
                        if (id - 10 == position) {
                            int id2 = id - 10;
                            changebitmap(id, id2);
                            Music.play(AdventureMode.this,R.raw.yidong2,false);
                            Idclass idclass1 = new Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            TraceStack.push(idclass1);
                            step_left--;
                            step_left_num.setText(String.valueOf(step_left));
                            checkfinish();
                        } else if (id + 10 == position) {
                            int id2 = id + 10;
                            changebitmap(id, id2);
                            Music.play(AdventureMode.this,R.raw.yidong2,false);
                            Idclass idclass1 = new Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            TraceStack.push(idclass1);
                            step_left--;
                            step_left_num.setText(String.valueOf(step_left));
                            checkfinish();
                        } else if (id - 1 == position) {
                            int id2 = id - 1;
                            changebitmap(id, id2);
                            Music.play(AdventureMode.this,R.raw.yidong2,false);
                            Idclass idclass1 = new Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            TraceStack.push(idclass1);
                            step_left--;
                            step_left_num.setText(String.valueOf(step_left));
                            checkfinish();
                        } else if (id + 1 == position) {
                            int id2 = id + 1;
                            changebitmap(id, id2);
                            Music.play(AdventureMode.this,R.raw.yidong2,false);
                            Idclass idclass1 = new Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            TraceStack.push(idclass1);
                            step_left--;
                            step_left_num.setText(String.valueOf(step_left));
                            checkfinish();
                        }
                    }
                });
                curimg.setLayoutParams(tlp);
                if (i == rows - 1 && j == cols - 1) {
                    curimg.setImageBitmap(null);
                } else {
                    curimg.setImageBitmap(Bitmap.createBitmap(bitmap, blockwidth * j, blockhight * i, blockwidth, blockhight));
                }
                tableRow.addView(curimg);

            }
            tableLayout.addView(tableRow);
        }
        randomtable(rows, cols);
    }

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
            if ((direct + position) > 0 && (((direct + position) % 10) < clos) && (((direct + position) / 10) < rows)) {
                changebitmap(position + direct, position);
                Idclass id = new Idclass();
                id.id1 = position - direct;
                id.id2 = position;
                TraceStack.push(id);
            }
        }

    }

    private void changebitmap(int id1, int id2) {
        //将两个id对应的imageview的图片交换
        int ten1, ten2, single1, single2, change;
        ImageView imageview1 = (ImageView) findViewById(id1);
        ImageView imageview2 = (ImageView) findViewById(id2);
        Bitmap bitmap1 = ((BitmapDrawable) imageview1.getDrawable()).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable) imageview2.getDrawable()).getBitmap();
        imageview1.setImageBitmap(bitmap2);
        imageview2.setImageBitmap(bitmap1);
        position = id1;
        ten1 = id1 / 10;
        ten2 = id2 / 10;
        single1 = id1 % 10;
        single2 = id2 % 10;
        change = array[ten1][single1];
        array[ten1][single1] = array[ten2][single2];
        array[ten2][single2] = change;
    }

    private Bitmap zoombitmap(Bitmap bitmap, int w, int h) {
        //获得宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //获得缩放因子
        float scalewidth = ((float) w / width);
        float scalehight = ((float) h / height);
        //建立矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scalewidth, scalehight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

    }

    private Bitmap cutbitmap(Bitmap bitmap, int x, int y, int width, int height) {
        return Bitmap.createBitmap(bitmap, x, y, width, height);
    }

    public void checkfinish() {
        boolean isfinished = true;
        for (int i = 0; i < golbal_rows; i++) {
            for (int j = 0; j < golbal_cols; j++) {
                if (array[i][j] != i * 10 + j) {
                    isfinished = false;
                }
            }
        }
        if (step_left == 0 || timeleft <0) {
            AlertDialog.Builder dialog_over = new AlertDialog.Builder(AdventureMode.this);
            dialog_over.setTitle("游戏提示");
            dialog_over.setMessage("游戏结束");
            dialog_over.setCancelable(false);
            //取消计时
            MyTimer.CancelTimer();
            dialog_over.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TraceStack.clear();
                    bitmap = BitmapFactory.decodeResource(getResources(), pictures[level]);
                    bitmap = zoombitmap(bitmap, screenwidth, screenwidth);
                    _imageView.setImageBitmap(bitmap);
                    chooselevel(rows, rows);
                    step_left = step_origin;
                    timeleft = time_origin;


                    // 启用计时
                    MyTimer.StartTimer(_mhandler);

                    step_left_num.setText(String.valueOf(step_left));
                    Timeleft.setText(String.valueOf(timeleft));
                }
            });
            dialog_over.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    Intent intent = new Intent(AdventureMode.this, PreloadActivity.class);
                    startActivity(intent);

                    //取消计时
                    MyTimer.CancelTimer();
                }
            });
            dialog_over.show();
        }

        if (isfinished) {

            //取消计时
            MyTimer.CancelTimer();

            if (level == Maxlevel) {
                createdialog_finish();

            } else {

                createDialog();
            }
        }

    }

    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 此处可以更新UI
            switch (msg.what) {
                case 1:
                    Idclass idrecover = TraceStack.pop();
                    changebitmap(idrecover.id1, idrecover.id2);
                    break;
            }
        }
    };

    public void createDialog() {
        AlertDialog.Builder dialog_next = new AlertDialog.Builder(AdventureMode.this);
        dialog_next.setTitle("游戏提示");

        dialog_next.setCancelable(false);
        dialog_next.setPositiveButton("下一关", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (level == 1 || level == 3) {
                    rows++;
                    step_origin = step_origin + 20;
                    time_origin = time_origin + 60;
                    award_coin = award_coin + 5;
                } else {
                    award_coin = award_coin + 2;
                }
                step_left = step_origin;
                timeleft = time_origin;


                level++;
                TraceStack.clear();
                bitmap = BitmapFactory.decodeResource(getResources(), pictures[level]);
                bitmap = zoombitmap(bitmap, screenwidth, screenwidth);
                _imageView.setImageBitmap(bitmap);
                chooselevel(rows, rows);
                step_left_num.setText(String.valueOf(step_left));
                Timeleft.setText(String.valueOf(timeleft));

                // 停止计时器
                MyTimer.CancelTimer();
                // 启用计时
                MyTimer.StartTimer(_mhandler);
            }
        });
        dialog_next.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdventureMode.this, PreloadActivity.class);
                startActivity(intent);

                // 停止计时器
                MyTimer.CancelTimer();
            }
        });
        dialog_next.setMessage("本关通过,奖励道具币" + award_coin + "枚");
        dialog_next.show();
        coin_num = coin_num + award_coin;
        Coin_num.setText(String.valueOf(coin_num));
        UpdateDataBase();
    }

    public void createdialog_finish() {
        AlertDialog.Builder dialog_next = new AlertDialog.Builder(AdventureMode.this);
        dialog_next.setTitle("游戏提示");
        dialog_next.setMessage("恭喜你，现已通关");
        dialog_next.setCancelable(false);
        dialog_next.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdventureMode.this, PreloadActivity.class);
                startActivity(intent);

                // 停止计时器
                MyTimer.CancelTimer();
            }
        });
        dialog_next.show();
        UpdateDataBase();
    }

    private void UpdateInfo() {
        bestrecord.setText(String.valueOf(_map.get_curuser().get_best_record()));
        lastrecord.setText(String.valueOf(_map.get_curuser().get_last_record()));
        coin_num = (_map.get_curuser().get_money());
        Coin_num.setText(String.valueOf(coin_num));
    }

    private void UpdateDataBase() {
        SQLiteDatabase db = _map.get_db();
        ContentValues values = new ContentValues();
        _map.get_curuser().setAdRecord(level + 1);
        values.put("last_record", _map.get_curuser().get_last_record());
        values.put("best_record", _map.get_curuser().get_best_record());
        values.put("money", coin_num);
        db.update("PlayerInfo", values, "playername=?", new String[]{_map.get_curuser().get_username()});
        values.clear();
    }

    public Boolean checkcoinnumber(int substract_coin) {
        if (coin_num - substract_coin >= 0) {
            coin_num = coin_num - substract_coin;
            Coin_num.setText(String.valueOf(coin_num));
            return true;
        }
        return false;
    }
}
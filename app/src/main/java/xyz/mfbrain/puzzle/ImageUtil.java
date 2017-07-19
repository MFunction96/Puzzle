package xyz.mfbrain.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.InputStream;

/**
 * Created by Chris Young on 2017/7/6.
 */
public class ImageUtil {
    /**
     * GameView中的游戏区域（表格布局）
     */
    private TableLayout tableLayout;
    /**
     * 动态生成表格行
     */
    private TableRow currow;
    /**
     * 当前图像
     */
    private ImageView curimage;
    /**
     * 上下文对象
     */
    private Context context;
    private GameActivity _ga;//GameActivity的对象

    private GameController _gc;//GameController的对象

    private int Step_Player = 0;//玩家移动的步数

    public ImageUtil(TableLayout tl, Context c, GameController gameController, GameActivity gameActivity) {
        tableLayout = tl;
        context = c;
        _gc = gameController;
        _ga = gameActivity;
    }


    /**
     * 对选择的图片进行缩放
     *
     * @param bitmap 位图
     * @param w      目标宽
     * @param h      目标高
     * @return
     */
   public static Bitmap ZoomBitmap(Bitmap bitmap, int w, int h) {
        //得到原始位图宽 高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //计算缩放因子
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        //设置缩放矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        //缩放图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 对位图进行裁剪
     *
     * @param bitmap 位图
     * @param x      起点横坐标
     * @param y      起点纵坐标
     * @param w      裁剪宽度
     * @param h      裁剪高度
     * @return
     */

    static Bitmap CutBitmap(Bitmap bitmap, int x, int y, int w, int h) {
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }

    /**
     * 填充游戏区域（表格布局）
     *
     * @param bitmap  缩放后的位图
     * @param rows    行数
     * @param colunms 列数
     */

    void FillGameZone(Bitmap bitmap, int rows, int colunms) {
        int blockWidth = bitmap.getWidth() / colunms;
        int blockHeight = bitmap.getHeight() / rows;
        tableLayout.removeAllViewsInLayout();
        //设置表格布局每单元格的布局参数
        TableRow.LayoutParams blockParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        //设置间距
        blockParams.setMargins(1, 1, 0, 0);
        blockParams.weight = 1;
        for (int i = 0; i < rows; i++) {
            currow = new TableRow(context);//当前行对象
            //设置当前行布局
            currow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //添加单元格
            for (int j = 0; j < colunms; j++) {
                _gc._position = (rows - 1) * 10 + colunms - 1;
                curimage = new ImageView(context);//新建ImageView对象
                curimage.setId(i * 10 + j);//为imageview赋id
                //设置ImageView属性
                curimage.setScaleType(ImageView.ScaleType.FIT_XY);
                curimage.setLayoutParams(blockParams);
                curimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();//获得需要改变的id
                        if (id - 10 == _gc._position) {
                            int id2 = id - 10;
                            _gc.ChangeBitmap(id, id2);
                            Music.play(_ga, R.raw.yidong2, false);
                            GameController.Idclass idclass1 = new GameController.Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        } else if (id + 10 == _gc._position) {
                            int id2 = id + 10;
                            _gc.ChangeBitmap(id, id2);
                            Music.play(_ga, R.raw.yidong2, false);
                            GameController.Idclass idclass1 = new GameController.Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        } else if (id - 1 == _gc._position) {
                            int id2 = id - 1;
                            _gc.ChangeBitmap(id, id2);
                            Music.play(_ga, R.raw.yidong2, false);
                            GameController.Idclass idclass1 = new GameController.Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        } else if (id + 1 == _gc._position) {
                            int id2 = id + 1;
                            _gc.ChangeBitmap(id, id2);
                            Music.play(_ga, R.raw.yidong2, false);
                            GameController.Idclass idclass1 = new GameController.Idclass();
                            idclass1.id1 = id2;
                            idclass1.id2 = id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        }
                    }

                });
                //将最后一个imageview的图片赋空
                if (i == rows - 1 && j == colunms - 1) {
                    curimage.setImageBitmap(null);
                } else {
                    curimage.setImageBitmap(bitmap.createBitmap(bitmap, blockWidth * j, blockHeight * i, blockWidth, blockHeight));
                }
                currow.addView(curimage);//将imageview添加到当前行中
            }
            tableLayout.addView(currow); //添加行对象
        }

    }

    public int GetStep_Player() {
        return Step_Player;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap ToRoundCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        // 抗锯齿
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap FixBmp(Bitmap bmp) {
        int x, y, width, height;
        final int sw = bmp.getWidth(), sh = bmp.getHeight();
        if (sw < sh) {
            x = 0;
            y = sh - sw >> 1;
            width = sw;
            height = sw;
        } else if (sw > sh) {
            x = sw - sh >> 1;
            y = 0;
            width = sh;
            height = sh;
        } else {
            x = 0;
            y = 0;
            width = sw;
            height = sh;
        }
        return Bitmap.createBitmap(bmp, x, y, width, height);
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


}

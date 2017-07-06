package xyz.mfbrain.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

/**
 * Created by Chris Young on 2017/7/6.
 */

public class GameUtil {
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


    GameUtil(Bitmap bm,TableLayout tl,Context c){
        tableLayout=tl;
        context=c;
    }

    /**
     * 对选择的图片进行缩放
     * @param bitmap 位图
     * @param w 目标宽
     * @param h 目标高
     * @return
     */
    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
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
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    /**
     * 对位图进行裁剪
     * @param bitmap 位图
     * @param x 起点横坐标
     * @param y 起点纵坐标
     * @param w 裁剪宽度
     * @param h 裁剪高度
     * @return
     */

    public Bitmap cutBitmap(Bitmap bitmap, int x, int y, int w, int h) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, x, y, w, h);
        return newBitmap;
    }

    /**
     * 填充游戏区域（表格布局）
     * @param bitmap 缩放后的位图
     * @param rows 行数
     * @param colunms 列数
     */

    public void fillGameZone(Bitmap bitmap,int rows, int colunms) {

        int blockWidth = bitmap.getWidth() / colunms;
        int blockHeignt = bitmap.getHeight() / rows;
        tableLayout.removeAllViewsInLayout();
        //设置表格布局每单元格的布局参数
        TableRow.LayoutParams blockParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        //设置间距
        blockParams.setMargins(1, 1, 0, 0);

        for (int i = 0; i < rows; i++) {
            currow = new TableRow(context);//当前行对象
            //设置当前行布局
            currow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //添加单元格
            for (int j = 0; j < colunms; j++) {
                curimage = new ImageView(context);//新建ImageView对象
                //设置ImageView属性
                curimage.setScaleType(ImageView.ScaleType.FIT_XY);
                curimage.setLayoutParams(blockParams);
                //将剪切的位图添加到ImageView
                curimage.setImageBitmap(cutBitmap(bitmap, j * blockWidth, i * blockHeignt, blockWidth, blockHeignt));
                currow.addView(curimage);//将imageview添加到当前行中
            }
            tableLayout.addView(currow); //添加行对象
        }

    }

}

package xyz.mfbrain.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

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
    GameActivity _ga;//GameActivity的对象

    GameController _gc;//GameController的对象

    private int Step_Player =0;//玩家移动的步数
    GameUtil(Bitmap bm, TableLayout tl, Context c,GameController gameController) {
        tableLayout = tl;
        context = c;
        _gc=gameController;
    }


    /**
     * 对选择的图片进行缩放
     *
     * @param bitmap 位图
     * @param w      目标宽
     * @param h      目标高
     * @return
     */
    Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
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

    Bitmap cutBitmap(Bitmap bitmap, int x, int y, int w, int h) {
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }

    /**
     * 填充游戏区域（表格布局）
     *
     * @param bitmap  缩放后的位图
     * @param rows    行数
     * @param colunms 列数
     */

    void fillGameZone(Bitmap bitmap, int rows, int colunms) {
        int blockWidth = bitmap.getWidth() / colunms;
        int blockHeight = bitmap.getHeight()/rows;
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
                curimage.setId(i*10+j);//为imageview赋id
                //设置ImageView属性
                curimage.setScaleType(ImageView.ScaleType.FIT_XY);
                curimage.setLayoutParams(blockParams);
                curimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id=v.getId();//获得需要改变的id
                        if(id-10==_gc._position)
                        {
                            int id2=id-10;
                            _gc.ChangeBitmap(id,id2);
                            GameController.Idclass idclass1=new GameController.Idclass();
                            idclass1.id1=id2;
                            idclass1.id2=id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        }
                        else if(id+10==_gc._position)
                        {
                            int id2=id+10;
                            _gc.ChangeBitmap(id,id2);
                            GameController.Idclass idclass1=new GameController.Idclass();
                            idclass1.id1=id2;
                            idclass1.id2=id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        }
                        else if(id-1==_gc._position)
                        {
                            int id2=id-1;
                            _gc.ChangeBitmap(id,id2);
                            GameController.Idclass idclass1=new GameController.Idclass();
                            idclass1.id1=id2;
                            idclass1.id2=id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        }
                        else if(id+1==_gc._position){
                            int id2=id+1;
                            _gc.ChangeBitmap(id,id2);
                            GameController.Idclass idclass1=new GameController.Idclass();
                            idclass1.id1=id2;
                            idclass1.id2=id;
                            _gc.TraceStack.push(idclass1);
                            _gc.checkfinish();
                            Step_Player++;
                        }
                    }

                });
                //将最后一个imageview的图片赋空
                if(i==rows-1&&j==colunms-1)
                {
                    curimage.setImageBitmap(null);
                }
                else {
                    curimage.setImageBitmap(bitmap.createBitmap(bitmap, blockWidth * j, blockHeight * i, blockWidth, blockHeight));
                }
                currow.addView(curimage);//将imageview添加到当前行中
            }
            tableLayout.addView(currow); //添加行对象
        }

    }
    public int GetStep_Player(){
        return Step_Player;
    }

}

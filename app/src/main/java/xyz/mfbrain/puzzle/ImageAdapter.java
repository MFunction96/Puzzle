package xyz.mfbrain.puzzle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by MFunction on 2017/7/3.
 *
 * @author MFunction
 */


final class ImageAdapter extends BaseAdapter {

    private Activity _ma;
    private int[] _bmpid;
    private Bitmap[] _bmp;

    ImageAdapter(Activity mainActivity) {
        final int COUNT = 9;
        BitmapWorkerTask[] bwt = new BitmapWorkerTask[COUNT];
        _ma = mainActivity;
        _bmpid = new int[COUNT];
        _bmp = new Bitmap[_bmpid.length];
        _bmpid[0] = R.drawable.lyoko1;
        _bmpid[1] = R.drawable.lyoko2;
        _bmpid[2] = R.drawable.lyoko3;
        _bmpid[3] = R.drawable.lyoko4;
        _bmpid[4] = R.drawable.lyoko5;
        _bmpid[5] = R.drawable.lyoko6;
        _bmpid[6] = R.drawable.conan1;
        _bmpid[7] = R.drawable.conan2;
        _bmpid[8] = R.drawable.conan3;
        for (int i = 0; i < COUNT; i++) {
/*            bwt[i] = new BitmapWorkerTask(_bmp[i], _ma.getResources(), _ma.GetGridview(), this);
            bwt[i].execute(String.valueOf(_bmpid[i]));*/
            _bmp[i] = FixBmp(BitmapFactory.decodeResource(_ma.getResources(), _bmpid[i]));
        }

    }

    @Override
    public int getCount() {
        return _bmpid.length;
    }

    @Override
    public Object getItem(int position) {
        return _bmp[position];
    }

    @Override
    public long getItemId(int position) {
        return _bmpid[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageview;
        if (convertView == null) {
            imageview = new ImageView(_ma);
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageview.setAdjustViewBounds(true);
            imageview.setMaxWidth(256);
            imageview.setMaxHeight(256);
            imageview.setPadding(8, 8, 8, 8);
        } else {
            imageview = (ImageView) convertView;
        }
        imageview.setImageBitmap(_bmp[position]);
        return imageview;
    }

    static Bitmap FixBmp(Bitmap bmp) {
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
}

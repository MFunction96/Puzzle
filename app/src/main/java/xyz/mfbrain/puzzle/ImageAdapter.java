package xyz.mfbrain.puzzle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by MFunction on 2017/7/3.
 *
 * @author MFunction*/



final class ImageAdapter extends BaseAdapter {

    private Context _context;
    private int[] _image;

    ImageAdapter(Context context) {
        _image = new int[4];
        _image[0] = R.drawable.lyoko1;
        _image[1] = R.drawable.lyoko2;
        _image[2] = R.drawable.lyoko3;
        _image[3] = R.drawable.lyoko4;
        _context = context;
    }

    @Override
    public int getCount() {
        return _image.length;
    }

    @Override
    public Object getItem(int position) {
        return _image[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageview;
        if (convertView == null) {
            imageview = new ImageView(_context);
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageview.setAdjustViewBounds(true);
            imageview.setMaxWidth(256);
            imageview.setMaxHeight(256);
            imageview.setPadding(8, 8, 8, 8);
        } else {
            imageview = (ImageView) convertView;
        }
        imageview.setImageResource(_image[position]);
        return imageview;
    }
}

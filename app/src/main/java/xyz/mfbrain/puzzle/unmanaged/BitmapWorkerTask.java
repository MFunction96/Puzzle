package xyz.mfbrain.puzzle.unmanaged;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;

import xyz.mfbrain.puzzle.models.ImageAdapter;

/**
 * Created by MFunction on 2017/7/12.
 *
 * @author MFunction
 */

class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private Bitmap _bmp;

    private ImageAdapter _ia;

    private Resources _resources;

    private GridView _gridview;

    BitmapWorkerTask(Bitmap bmp, Resources resources, GridView gridView, ImageAdapter imageAdapter) {
        _bmp = bmp;
        _resources = resources;
        _gridview = gridView;
        _ia = imageAdapter;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private Bitmap decodeSampledBitmap(String str, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (_resources == null) {
            BitmapFactory.decodeFile(str, options);
        } else {
            BitmapFactory.decodeResource(_resources, Integer.valueOf(str), options);
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        if (_resources == null) {
            return BitmapFactory.decodeFile(str, options);
        } else {
            return BitmapFactory.decodeResource(_resources, Integer.valueOf(str), options);
        }
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        String data = params[0];
        return decodeSampledBitmap(data, 720, 720);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        _bmp = ImageUtil.FixBmp(bitmap);
        if (_ia != null) {
            _ia.notifyDataSetChanged();
            _gridview.deferNotifyDataSetChanged();
            _gridview.setAdapter(_ia);
        }
    }
}
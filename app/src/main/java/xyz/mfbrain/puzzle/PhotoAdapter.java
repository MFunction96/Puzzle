package xyz.mfbrain.puzzle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by MFunction on 2017/7/3.
 *
 * @author MFunction
 */

final class PhotoAdapter extends BaseAdapter {

    private Context _context;
    private int[] _icon;
    private GridView _gridview;
    private List<Map<String,Object>> _datalist;

    PhotoAdapter(MainActivity mainActivity) {
        _gridview = mainActivity.GetGridView();
        _icon = new int[6];
        _icon[0] = R.drawable.Lyoko1;
        _icon[1] = R.drawable.Lyoko2;
        _icon[2] = R.drawable.Lyoko3;
        _icon[3] = R.drawable.Lyoko4;
        _icon[4] = R.drawable.Lyoko5;
        _icon[5] = R.drawable.Lyoko6;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

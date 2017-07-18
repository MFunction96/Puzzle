package xyz.mfbrain.puzzle;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chris Young on 2017/7/13.
 */

public class RankListAdapter extends ArrayAdapter<Users> {
    private int _resourceId;
    private Context _context;
    private int[] _imageid;

    public RankListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Users> objects) {
        super(context, resource, objects);
        _context = context;
        _resourceId = resource;
        _imageid = new int[]{R.drawable.rank1, R.drawable.rank2, R.drawable.rank3};

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Users _player = getItem(position);
        View _view;
        Typeface typeFace = Typeface.createFromAsset(_context.getAssets(), "fonts/fzstk.ttf");
        if (_player.get_id() < 4) {
            _view = LayoutInflater.from(_context).inflate(R.layout.list_layout2, parent, false);
            ImageView _image = (ImageView) _view.findViewById(R.id.list_image);
            _image.setImageResource(_imageid[_player.get_id() - 1]);
            TextView _name2 = (TextView) _view.findViewById(R.id.list_name2);
            TextView _record2 = (TextView) _view.findViewById(R.id.list_record2);
            _name2.setText(_player.get_username());
            _record2.setText(String.valueOf(_player.get_last_record()));
            _name2.setTypeface(typeFace);
            _record2.setTypeface(typeFace);
        } else {
            _view = LayoutInflater.from(_context).inflate(R.layout.list_layout, parent, false);
            TextView _name = (TextView) _view.findViewById(R.id.list_name);
            TextView _record = (TextView) _view.findViewById(R.id.list_record);
            TextView _num = (TextView) _view.findViewById(R.id.list_number);
            if (_player.get_username().equals(GameData.get_curuser().get_username())) {
                _num.setTextColor(_context.getResources().getColor(R.color.qianhong));
                _name.setTextColor(_context.getResources().getColor(R.color.qianhong));
                _record.setTextColor(_context.getResources().getColor(R.color.qianhong));
            }
            _name.setTypeface(typeFace);
            _record.setTypeface(typeFace);
            _num.setTypeface(typeFace);
            _num.setText(String.valueOf(_player.get_id()));
            _name.setText(_player.get_username());
            _record.setText(String.valueOf(_player.get_last_record()));
        }
        return _view;
    }
}

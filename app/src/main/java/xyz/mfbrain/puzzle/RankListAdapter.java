package xyz.mfbrain.puzzle;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Chris Young on 2017/7/13.
 */

public class RankListAdapter extends ArrayAdapter<Users> {
    private int _resourceId;
    private Context _context;

    public RankListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Users> objects) {
        super(context, resource, objects);
        _context = context;
        _resourceId = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Users _player = getItem(position);
        View _view;
        if (convertView == null) {
            _view = LayoutInflater.from(_context).inflate(_resourceId, parent, false);
        } else {
            _view = convertView;
        }
        TextView _name = (TextView) _view.findViewById(R.id.list_name);
        TextView _record = (TextView) _view.findViewById(R.id.list_record);
        TextView _num = (TextView) _view.findViewById(R.id.list_number);
        _num.setText(_player.get_id()+"");
        _name.setText(_player.get_username());
        _record.setText(_player.get_last_record()+"");
        return _view;
    }
}

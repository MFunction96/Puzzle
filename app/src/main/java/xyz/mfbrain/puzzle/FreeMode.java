package xyz.mfbrain.puzzle;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.FileNotFoundException;

public class FreeMode extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Bitmap _btm;
    private Button _btn;
    private ImageView _image;
    private RadioGroup _rg;
    private RadioButton _freeLevel1;
    private RadioButton _freeLevel2;
    private RadioButton _freeLevel3;
    private boolean _islevelChosen;
    private boolean _isImageChosen;
    private String _bmp;
    private boolean _isid;
    private MyApplication _map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_mode);
        Init();
    }

    private void Init() {
        _map=(MyApplication)this.getApplication(); 
        _btn = (Button) findViewById(R.id.free_btn);
        _btn.setOnClickListener(this);
        _rg = (RadioGroup) findViewById(R.id.free_group);
        _rg.setOnCheckedChangeListener(this);
        _freeLevel1 = (RadioButton) findViewById(R.id.free_level1);
        _freeLevel2 = (RadioButton) findViewById(R.id.free_level2);
        _freeLevel3 = (RadioButton) findViewById(R.id.free_level3);
        _image = (ImageView) findViewById(R.id.free_image);
        _image.setOnClickListener(this);
        _btn.setEnabled(false);
        _bmp = "";
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
        _freeLevel1.setTypeface(typeFace);
        _freeLevel2.setTypeface(typeFace);
        _freeLevel3.setTypeface(typeFace);
        _btn.setTypeface(typeFace);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.free_image) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra("crop", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.free_btn) {
            Intent it = new Intent();
            it.setClass(this, GameActivity.class);
            it.putExtra("id", _isid);
            it.putExtra("bmp", _bmp);
            startActivity(it);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            _isid = false;
            _bmp = data.getData().toString();
            ContentResolver cr = getContentResolver();
            try {
                _btm = ImageUtil.FixBmp(BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(_bmp))));
                _btm = ImageUtil.ZoomBitmap(_btm, 500, 500);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            _image.setImageBitmap(_btm);
            if (_islevelChosen) {
                _btn.setEnabled(true);
                _btn.setText("开始游戏");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == _freeLevel1.getId()) {
            _map.set_gamedifficulty(3);
        } else if (checkedId == _freeLevel2.getId()) {
            _map.set_gamedifficulty(4);
        } else if (checkedId == _freeLevel3.getId()) {
            _map.set_gamedifficulty(5);
        }
        _islevelChosen = true;
        if (!_bmp.equals("")) {
            _btn.setEnabled(true);
        }

    }
}

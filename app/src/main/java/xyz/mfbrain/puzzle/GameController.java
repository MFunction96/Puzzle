package xyz.mfbrain.puzzle;

/**
 * Created by MFunction on 2017/7/4.
 * @author MFunction
 */

class GameController {

    private PhotoAdapter _pa;

    GameController(MainActivity mainActivity) {
        _pa=mainActivity.GetPhotoAdapter();
    }

}

package top.itmp.jiandan.ui.fragment;

import android.os.Bundle;

import top.itmp.jiandan.model.Picture;

public class SisterFragment extends PictureFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mType = Picture.PictureType.Sister;
    }
}

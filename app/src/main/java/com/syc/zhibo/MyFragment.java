package com.syc.zhibo;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class MyFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fg_my,container,false);
        SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.photo);
        try{
            img.setImageURI(Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530083220237&di=2ff7739aa50c57425839cf5a7910bbfa&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F4a36acaf2edda3ccd53548ea0be93901203f9223.jpg"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
}

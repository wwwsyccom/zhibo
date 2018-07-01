package com.syc.zhibo.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerMarginDecoration  extends RecyclerView.ItemDecoration {
    private int space;
    private int col;
    public RecyclerMarginDecoration(int space, int col) {
        this.space = space;
        this.col = col;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if ((parent.getChildLayoutPosition(view)+1) % col == 0){
            outRect.right= 0;
        }else{
            outRect.right= space;
        }
    }
}

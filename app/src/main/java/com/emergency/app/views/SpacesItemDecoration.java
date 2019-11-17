package com.emergency.app.views;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        outRect.top = mSpace;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.right =mSpace/2;
            outRect.bottom = mSpace/2;
        }
        else if(parent.getChildAdapterPosition(view)==1)
        {
            outRect.left =mSpace/2;
            outRect.bottom = mSpace/2;
        }
        else if(parent.getChildAdapterPosition(view)%2==0&&parent.getChildAdapterPosition(view)>1)
        {
            outRect.right =mSpace/2;
            outRect.bottom = mSpace/2;
            outRect.top = mSpace/2;
        }
        else
        {
            outRect.left =mSpace/2;
            outRect.bottom = mSpace/2;
            outRect.top = mSpace/2;
        }
    }
}


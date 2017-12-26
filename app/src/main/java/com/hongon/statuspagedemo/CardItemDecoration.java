package com.hongon.statuspagedemo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * Created by Admin on 2017/12/22.
 */

public class CardItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int size;

    public CardItemDecoration() {
        paint = new Paint();
    }

    ;

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + size;

            c.drawRect(left, top, right, bottom, paint);

        }
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setSize(int size) {
        this.size = size;
    }
}
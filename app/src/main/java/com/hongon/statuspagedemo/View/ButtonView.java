package com.hongon.statuspagedemo.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.hongon.statuspagedemo.R;

/**
 * Created by CoCO on 2018/1/2.
 */

public class ButtonView extends View {

    //--- Member ---

    // size
    int mHeight;
    int mWidth;

    // paint
    Paint mPaint;
    // icon的id
    int icon;

    VectorDrawableCompat x;

    public void setIcon(int DrawableId) {
        this.icon = DrawableId;
    }


    //--- Constructor

    public ButtonView(Context context)
    {
        super(context,null);
    }

    public ButtonView(Context context, AttributeSet attr)
    {
        super(context,attr);
        initPaint();
        initChild();


    }



    //----- Core Function

    private  void initPaint()
    {
       mPaint = new Paint();
       mPaint.setStyle(Paint.Style.STROKE);
       mPaint.setStrokeCap(Paint.Cap.ROUND);
       mPaint.setStrokeWidth(3f);
       mPaint.setAntiAlias(true);
    }

    private void initChild(){
        ImageView icon = new ImageView(getContext());
        //icon;
        //addView();

    }

    private void initVectorDraw()
    {
        //x = VectorDrawableCompat.create(,R.drawable.ic_sun);
    }


    //--- Override




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 中心
        canvas.translate(mWidth/2,mHeight/2);

        // 画边框圆
        canvas.drawCircle(0,0,(mWidth/2f -3f ),mPaint);

        //canvas.drawRect(0,0,48,48,mPaint);

        VectorDrawableCompat x = VectorDrawableCompat.create(this.getContext().getResources(),R.drawable.ic_sun,null);
        x.draw(canvas);
    }
}

package com.hongon.statuspagedemo.View;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

/**
 * Created by CoCO on 2018/1/3.
 * 目前通过模仿实现了一条重复动画的播放。从一侧端点到 画布中央。
 * 需要补上状态监听器，安上接口从外部控制该View中动画的内容。
 * 4个圈中的内容在另外的ButtonView中，点击事件也是从其中触发的，
 * 最后将融合在一起达到统一的效果。
 *
 */

public class BigView extends View {
    //--- Member ---

    // size
    int mHeight;
    int mWidth;

    // paint
    Paint mPaint;
    // icon的id
    int icon;

    // Path
    Path pa;
    Path pb;
    PathMeasure pathMeasure;

    // Anim

    ValueAnimator valueAnimator ;


    public void setIcon(int DrawableId) {
        this.icon = DrawableId;
    }


    //--- Constructor

    public BigView(Context context)
    {
        super(context,null);
    }

    public BigView(Context context, AttributeSet attr)
    {
        super(context,attr);
        initPaint();
        //initPath();
        initAnim();
        valueAnimator.start();

    }
    //--
    private  void initPaint()
    {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
    }
    private void initPath()
    {
        float w = 2/8f*mWidth;
        float h = 2/8f*mHeight;

        pathMeasure =new PathMeasure();
        pa = new Path();
        //RectF ova1 = new RectF(-w,-10,0,10);
        pa.moveTo(-w,0);
        pa.lineTo(0,0);
        pathMeasure.setPath(pa,false);


    }

    private void initAnim()
    {
        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    //---

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    //---On Draw


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPath();
        canvas.translate(mWidth/2,mHeight/2);
        //canvas.drawCircle(0,0,3/8f*mWidth,mPaint);
        //画中间的线
        canvas.drawLine(0,-2/8f*mHeight,0,2/8f*mHeight,mPaint);
        canvas.drawLine(-2/8f*mWidth,0,2/8f*mWidth,0,mPaint);
        // 画4个大圆
        canvas.drawCircle(0,-3/8f*mHeight,1/8f*mHeight-2,mPaint);
        canvas.drawCircle(0,3/8f*mHeight,1/8f*mHeight-2,mPaint);
        canvas.drawCircle(-3/8f*mWidth,0,1/8f*mHeight-2,mPaint);
        canvas.drawCircle(3/8f*mWidth,0,1/8f*mHeight-2,mPaint);

        Paint x = new Paint(mPaint);
        x.setStrokeCap(Paint.Cap.ROUND);
        x.setStrokeWidth(12);
        x.setColor(Color.RED);
        //画path

        //canvas.drawPath(pa,x);
        float l =pathMeasure.getLength();
        float v = (float)valueAnimator.getAnimatedValue();
        float startD = v*l;
        float stopD =(v+0.05f)*l;
        Path dst = new Path();
        pathMeasure.getSegment(startD,stopD,dst,true);

        canvas.drawPath(dst,x);
    }
}

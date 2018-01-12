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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoCO on 2018/1/3.
 * 目前通过模仿实现了一条重复动画的播放。从一侧端点到 画布中央。
 * 需要补上状态监听器，安上接口从外部控制该View中动画的内容。
 * 4个圈中的内容在另外的ButtonView中，点击事件也是从其中触发的，
 * 最后将融合在一起达到统一的效果。
 * Update in 2018.1.5
 * 目前4个轴共用一个动画变量
 * 动画的方向以及是否显示是在onDraw的时候确定的。
 *
 */

public class BigView extends View {
    //--- Member ---

    public static final int PATH_DISABLE=0;
    public static final int PATH_0_1=1;
    public static final int PATH_1_0=2;

    private int paflag =PATH_DISABLE;
    private int pbflag =PATH_DISABLE;
    private int pcflag =PATH_DISABLE;
    private int pdflag =PATH_DISABLE;
    public void setFlag(int a,int b,int c,int d)
    {
        paflag =a;
        pbflag =b;
        pcflag =c;
        pdflag =d;
        Log.e("setFlag", "flag: "+a+" "+b+" "+c+" "+d);
    }
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
    Path pc;
    Path pd;
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
        mPaint.setStrokeWidth(0f);
        mPaint.setAntiAlias(true);
    }
    private void initPath()
    {
        //数值是外圈的直径，也是路径的长度
        float w = 2/8f*mWidth;
        float h = 2/8f*mHeight;

        pathMeasure = new PathMeasure();
        // path A


        pa = new Path();
        RectF ova1 = new RectF(-2*w,-0.5f*h,-w,0.5f*h);
        pa.addArc(ova1,0,359.9f);
        pa.lineTo(0,0);

        // pathB
        pb = new Path();
        RectF ova2 = new RectF(-0.5f*w,-2*h,0.5f*w,-h);
        pb.addArc(ova2,90f,359.9f);
        pb.lineTo(0,0);

        // pathC
        pc = new Path();
        RectF ova3 = new RectF(1f*w,-0.5f*h,2f*w,0.5f*h);
        pc.addArc(ova3,180f,359.9f);
        pc.lineTo(0,0);

        // pathD
        pd = new Path();
        RectF ova4 = new RectF(-0.5f*w,1*h,0.5f*w,2*h);
        pd.addArc(ova4,-90f,359.9f);
        pd.lineTo(0,0);



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
        valueAnimator.setDuration(3000);
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

    final String tag = "BigView";
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 试试看能不能做到正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height =MeasureSpec.getSize(heightMeasureSpec);
        Log.e(tag," onMeasure. width: "+width+" height: "+height);
        height = width;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        Log.e(tag," onSizeChanged. width: "+w+" height: "+h);
        initPath();
    }

    //---On Draw


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth/2,mHeight/2);
        //canvas.drawCircle(0,0,3/8f*mWidth,mPaint);
        //画中间的线

        //canvas.drawLine(0,-2/8f*mHeight,0,2/8f*mHeight,mPaint);
        //canvas.drawLine(-2/8f*mWidth,0,2/8f*mWidth,0,mPaint);
        // 画4个大圆
        // 一半的宽高

        float r = mWidth/8;//一般是45dp
        float w = mWidth/2;
        float h = mHeight/2;
        float angle = (float)Math.PI*2/8;
        canvas.drawCircle(0,0,r,mPaint);// A
        canvas.drawCircle(-w+r,-h+r,r,mPaint);// B
        canvas.drawCircle(w-r,-h+r,r,mPaint); // C
        canvas.drawCircle(w-r,h-r,r,mPaint);    //D
        canvas.drawCircle(-w+r,h-r,r,mPaint);   // E
        canvas.drawCircle(0,h-r,r,mPaint); // F
        //画斜线

        canvas.drawLine((float)Math.cos(angle)*r,(float)(-1*Math.sin(angle))*r,(float)(w-r-Math.cos(angle)*r),(float)((-h+r+Math.sin(angle)*r)),mPaint);
        Paint x = new Paint(mPaint);
        x.setStrokeCap(Paint.Cap.ROUND);
        x.setStrokeWidth(12);
        x.setColor(Color.RED);
        //画path

        //canvas.drawPath(pa,x);
        drawMyPath(canvas,pa,x,paflag);
        drawMyPath(canvas,pb,x,pbflag);
        drawMyPath(canvas,pc,x,pcflag);
        drawMyPath(canvas,pd,x,pdflag);
    }
    private void drawMyPath(Canvas canvas,Path pa,Paint x ,int flag)
    {
        if(flag ==PATH_DISABLE)
            return;

        pathMeasure.setPath(pa,false);
        float l =pathMeasure.getLength();
        float v = (float)valueAnimator.getAnimatedValue();
        float series =(float)valueAnimator.getAnimatedValue();
        if(flag==PATH_1_0)
            v= 1.0f-v;
        float startD = v*l;
        float stopD =(v+0.1f*series)*l;
        Path dst = new Path();
        pathMeasure.getSegment(startD,stopD,dst,true);
        canvas.drawPath(dst,x);
    }


    // 这个类控制Path方向以及是否显示
    class PathControl
    {
        public int status;
    }
}

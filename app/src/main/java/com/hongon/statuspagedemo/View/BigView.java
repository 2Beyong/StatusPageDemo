package com.hongon.statuspagedemo.View;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.hongon.statuspagedemo.R;

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

    private int paflag =PATH_0_1;
    private int pbflag =PATH_0_1;
    private int pcflag =PATH_0_1;
    private int pdflag =PATH_0_1;
    private int pcSubFlag =PATH_0_1;
    public void setFlag(int a,int b,int c,int d)
    {
        paflag =a;
        pbflag =b;
        pcflag =c;
        pdflag =d;
        Log.e("setFlag", "flag: "+a+" "+b+" "+c+" "+d);
    }

    //

    public void setFlag_5(int a,int b,int c,int d,int e)
    {
        paflag =a;
        pbflag =b;
        pcflag =c;
        pdflag =d;
        pcSubFlag =e;
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
    Path pcSub;
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
        float w = mWidth/2;
        float h = mHeight/2;
        float r = mWidth/8;
        pathMeasure = new PathMeasure();
        // path A
        PointF Z = new PointF(0,0);
        PointF A= new PointF(-w+r,-h+r);
        PointF B= new PointF(w-r,-h+r);
        PointF C= new PointF(w-r,h-r);
        PointF D= new PointF(-w+r,h-r);
        PointF E= new PointF(0,h-r);

        float angle = (float)Math.PI*2/8;
        float CosR_45 =(float)Math.cos(angle)*r;
        float SinR_45 = (float)Math.sin(angle)*r;


        pa = new Path();
        pa.moveTo(A.x + CosR_45,A.y +SinR_45);
        pa.lineTo(Z.x -CosR_45,Z.y-SinR_45);




        // pathB
        pb = new Path();
        pb.moveTo(B.x -CosR_45,B.y +SinR_45);
        pb.lineTo(Z.x+CosR_45,Z.y -SinR_45);


        // pathC
        pc = new Path();
        pc.moveTo(C.x -CosR_45,C.y -SinR_45);
        pc.lineTo(Z.x +CosR_45,Z.y+SinR_45);

        // pathD
        pd = new Path();

        pd.moveTo(D.x +CosR_45,D.y -SinR_45);
        pd.lineTo(Z.x -CosR_45,Z.y+SinR_45);

        pcSub = new Path();
        pcSub.moveTo(E.x+CosR_45,E.y-SinR_45);
        pcSub.lineTo(C.x/2,C.y/2);
        pcSub.lineTo(Z.x+CosR_45,Z.y+SinR_45);

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
        float angle = (float)Math.PI*2/8; // angle是45度
        canvas.drawCircle(0,0,r,mPaint);// A
        canvas.drawCircle(-w+r,-h+r,r,mPaint);// B
        canvas.drawCircle(w-r,-h+r,r,mPaint); // C
        canvas.drawCircle(w-r,h-r,r,mPaint);    //D
        canvas.drawCircle(-w+r,h-r,r,mPaint);   // E
        canvas.drawCircle(0,h-r,r,mPaint); // F
        //画斜线
        // A
        canvas.drawLine((float)Math.cos(5*angle)*r,(float)(Math.sin(5*angle))*r,(float)(-w+r+Math.cos(angle)*r),(float)((-h+r+Math.sin(angle)*r)),mPaint);
        // B
        canvas.drawLine((float)Math.cos(-angle)*r,(float)(Math.sin(-angle))*r,(float)(w-r-Math.cos(angle)*r),(float)((-h+r+Math.sin(angle)*r)),mPaint);
        // C
        canvas.drawLine((float)Math.cos(angle)*r,(float)(Math.sin(angle))*r,(float)(w-r-Math.cos(angle)*r),(float)((h-r-Math.sin(angle)*r)),mPaint);
        // D
        canvas.drawLine((float)Math.cos(3*angle)*r,(float)(Math.sin(3*angle))*r,(float)(-w+r+Math.cos(angle)*r),(float)((h-r-Math.sin(angle)*r)),mPaint);

        // sub C
        canvas.drawLine((float)(w-r)/2,(float)(h-r)/2,(float)(0+Math.cos(angle)*r),(float)((h-r-Math.sin(angle)*r)),mPaint);
        Paint x = new Paint(mPaint);
        x.setStrokeCap(Paint.Cap.ROUND);
        x.setStrokeWidth(4);
        x.setColor(Color.RED);
        x.setStyle(Paint.Style.FILL_AND_STROKE);
        //画path

        //canvas.drawPath(pa,x);
        drawMyPath(canvas,pa,x,paflag);
        drawMyPath(canvas,pb,x,pbflag);
        drawMyPath(canvas,pc,x,pcflag);
        drawMyPath(canvas,pd,x,pdflag);
        drawMyPath(canvas,pcSub,x,pcSubFlag);
    }
    private void drawMyPath(Canvas canvas,Path pa,Paint paint ,int flag)
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



        float[] mCurrentPostion = new float[2];
        float[] mTan=new float[2];

        pathMeasure.getPosTan(startD,mCurrentPostion,mTan);
        float degrees =(float)(Math.atan2(mTan[1],mTan[0])*180.0/Math.PI);




        canvas.drawCircle(mCurrentPostion[0],mCurrentPostion[1],12,paint);
    }


    // 这个类控制Path方向以及是否显示

}

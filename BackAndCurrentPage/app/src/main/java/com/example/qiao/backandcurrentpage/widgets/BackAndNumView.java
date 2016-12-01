package com.example.qiao.backandcurrentpage.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.qiao.backandcurrentpage.R;

/**
 * Created by qjb on 2016/11/17.
 */

public class BackAndNumView extends View {

    private Context mContext;

    private int mWidth;
    private int mHeight;

    private Paint mBgPaint;
    private Paint mBackPaint;
    private Paint mNumPaint;

    private int demoNumSize;
    private int numerNumSize;
    private int backAndNumColor;
    private int bgColor;
    private float viewThickness;
    private int pageDividerMargin;
    private int backTopLeftRightMargin;
    public BackAndNumView(Context context) {
        super(context);
    }

    public BackAndNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        analyzeAttr(context,attrs);
    }

    public BackAndNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        analyzeAttr(context,attrs);
    }

    private void analyzeAttr(Context context,AttributeSet attributeSet){
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.BackAndNumView);
        demoNumSize = a.getDimensionPixelSize(R.styleable.BackAndNumView_demoNumSize,
                context.getResources().getDimensionPixelOffset(R.dimen.page_demo_size));
        numerNumSize = a.getDimensionPixelSize(R.styleable.BackAndNumView_numerNum,
                context.getResources().getDimensionPixelOffset(R.dimen.page_numer_size));
        backAndNumColor = a.getColor(R.styleable.BackAndNumView_backAndNumColor,
                context.getResources().getColor(R.color.page_num_color));
        viewThickness = a.getFloat(R.styleable.BackAndNumView_viewThickness,
                context.getResources().getDimensionPixelOffset(R.dimen.page_divider_size));
        bgColor = a.getColor(R.styleable.BackAndNumView_bgcolor,context.getResources().getColor(R.color.white));
        a.recycle();
        init();
    }

    private void init(){
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(bgColor);

        mBackPaint = new Paint();

        mBackPaint.setColor(backAndNumColor);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setStrokeWidth(viewThickness);

        mNumPaint = new Paint();
        mNumPaint.setColor(backAndNumColor);
        mNumPaint.setStyle(Paint.Style.FILL);

        pageDividerMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.page_divider_margin);
        backTopLeftRightMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.page_back_top_margin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode==MeasureSpec.EXACTLY){
            mWidth = widthSpecSize;
        }else {
            if (widthSpecMode==MeasureSpec.AT_MOST){
                mWidth = 120;
            }
        }

        if (heightSpecMode==MeasureSpec.EXACTLY){
            mHeight = heightSpecSize;
        }else {
            if (heightSpecMode == MeasureSpec.AT_MOST){
                mHeight = 120;
            }
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    /*
    * 1、两个构造函数及初始化的方法
    * 2、onMeasure进行测量，处理好wrap和match
    * 3、onSizeChanged获取当前View的宽高
    * 4、onDraw进行绘画
    * 5、对外提供接口
    * */

    public String demoNumStr="";//分母
    public String numerNumStr="";//分子
    boolean isShowTop = true;
    public void setPageNum(int demoNum,int numerNum){
        this.isShowTop = false;
        this.demoNumStr = demoNum+"";
        this.numerNumStr = numerNum+"";
        invalidate();
    }

    public boolean getShowTop(){
        return isShowTop;
    }

    public void setBackTop(boolean isShowTop){
        this.isShowTop = isShowTop;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth/2,mHeight/2,Math.min(mWidth/2,mHeight/2),mBgPaint);
        if (isShowTop){
            drawBackTop(canvas);
        }else {
            drawNum(canvas);
        }
    }

    private void drawBackTop(Canvas canvas){//箭头居中显示
        //横条
        canvas.drawLine(mWidth/2-backTopLeftRightMargin,mHeight*0.3f,mWidth/2+backTopLeftRightMargin,mHeight*0.3f, mBackPaint);
        //左边
        canvas.drawLine(mWidth/2,mHeight*0.35f,mWidth/2-backTopLeftRightMargin,mHeight*0.35f+backTopLeftRightMargin, mBackPaint);
        //中间
        canvas.drawLine(mWidth/2,mHeight*0.35f,mWidth/2,mHeight*0.7f, mBackPaint);
        //右边
        canvas.drawLine(mWidth/2,mHeight*0.35f,mWidth/2+backTopLeftRightMargin,mHeight*0.35f+backTopLeftRightMargin, mBackPaint);
        //圆点
        canvas.drawCircle(mWidth/2,mHeight*0.35f,viewThickness/2,mBackPaint);
    }

    private void drawNum(Canvas canvas){
        mNumPaint.setTextSize(numerNumSize);

        canvas.drawText(numerNumStr,getNumStartX(numerNumStr),mHeight*0.4f,mNumPaint);
        canvas.drawLine(pageDividerMargin,mHeight*0.5f,mWidth-pageDividerMargin,mHeight*0.5f,mBackPaint);

        mNumPaint.setTextSize(demoNumSize);
        canvas.drawText(demoNumStr,getNumStartX(demoNumStr),mHeight*0.75f,mNumPaint);
    }

    private int getNumStartX(String numerNumStr){
        Rect rect = new Rect();
        mNumPaint.getTextBounds(numerNumStr,0,numerNumStr.length(),rect);
        int sizeWidth = rect.width();
        return (mWidth-sizeWidth)/2;
    }
}

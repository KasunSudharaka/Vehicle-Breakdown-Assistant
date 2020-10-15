package com.example.loginapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.animation.Interpolator;
import android.widget.TextView;

public class AnimateCounter {

private TextView mview;
private long mDuration;
private float mStartValue;
private float mEndingValue;
private int mPrecision;
private Interpolator mInterpolator;
private ValueAnimator mValueAnimator;
private AnimateCounterListner mListner;


public void execute(){

    mValueAnimator=ValueAnimator.ofFloat(mStartValue,mEndingValue);
    mValueAnimator.setDuration(mDuration);
    mValueAnimator.setInterpolator(mInterpolator);
    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {


            float current=Float.valueOf(mValueAnimator.getAnimatedValue().toString());
            mview.setText(String.format("%."+mPrecision+"f",current));
        }
    });

    mValueAnimator.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mListner!=null){

                mListner.onAnimateCounterEnd();
            }
        }
    });

    mValueAnimator.start();
}


public static class Builder{
    private long mDuration=2000;
    private float mStartValue=0;
    private float mEndingValue=10;
    private int mPrecision=0;
    private Interpolator mInterpolator=null;
    private TextView mView;

    public Builder (TextView view){

        if (view==null){

            throw new IllegalArgumentException("View cannot be null");
        }

        mView=view;
    }

    public Builder setCount(final int start, final int end){

        if (start==end){
            throw new IllegalArgumentException("Start and end must be different");

        }

        mStartValue=start;
        mEndingValue=end;
        mPrecision=0;
        return this;
    }

    public Builder setCounter(final float start,final float end,final int precision){

        if (Math.abs(start-end)<0.001){

            throw new IllegalArgumentException("Start and end must be different");

        }

        if (precision<0){

            throw new IllegalArgumentException("Precision can't be negative ");

        }

        mStartValue=start;
        mEndingValue=end;
        mPrecision=precision;
        return this;

    }

    public Builder setDuration(long duration){
        if (duration<=0){

            throw new IllegalArgumentException("Duration can't be negative ");


        }
    mDuration=duration;
        return this;
    }


    public Builder setInterpolator(Interpolator interpolator){

        mInterpolator=interpolator;
        return this;
    }

    public AnimateCounter build(){

        return new AnimateCounter(this);
    }
}

private AnimateCounter(Builder builder){

    mview=builder.mView;
    mDuration=builder.mDuration;
    mStartValue=builder.mStartValue;
    mEndingValue=builder.mEndingValue;
    mPrecision=builder.mPrecision;
    mInterpolator=builder.mInterpolator;

}

public void stop(){

    if (mValueAnimator.isRunning()){

        mValueAnimator.cancel();
    }
}

public void setAnimateCounterListner(AnimateCounterListner listner){

    mListner=listner;
}


public interface AnimateCounterListner{

    void onAnimateCounterEnd();
}

}

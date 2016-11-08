package com.volodia.beegame;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Volodia on 07.11.2016.
 */

public class BeeView extends ImageView implements IBee {

    private static final int QUEEN_HEALTH_BAR_WIDTH = 20;
    private static final int DRONE_HEALTH_BAR_WIDTH = 5;
    private static final int WORKER_HEALTH_BAR_WIDTH = 8;

    Bee bee;
    ValueAnimator animator;
    Paint paint;
    float barWidth;
    float barHeight = -1;

    final float[] from = new float[3],
            to = new float[3],
            hsv = new float[3];

    public BeeView(Context context, Bee bee) {
        super(context);
        this.bee = bee;
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        switch (bee.getBeeType()) {
            case QUEEN:
                setImageResource(R.drawable.bee_queen);
                barWidth = QUEEN_HEALTH_BAR_WIDTH;
                break;
            case DRONE:
                setImageResource(R.drawable.bee_drone);
                barWidth = DRONE_HEALTH_BAR_WIDTH;
                break;
            case WORKER:
                setImageResource(R.drawable.bee_worker);
                barWidth = WORKER_HEALTH_BAR_WIDTH;
                break;
        }
        barWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth, getResources().getDisplayMetrics());


        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        setLayoutParams(param);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);

        Color.colorToHSV(Color.GREEN, to);
        Color.colorToHSV(Color.RED, from);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        if (barHeight == -1) barHeight = MeasureSpec.getSize(widthMeasureSpec) - barWidth;
    }

    @Override
    public boolean hitBee() {
        boolean died = bee.hitBee();
        playHitAnim(died);
        return died;
    }

    @Override
    public void resurrect() {
        cancelAnim();
        bee.resurrect();
        setAlpha(1.0f);
        barHeight = getHeight() - barWidth;
        paint.setColor(Color.GREEN);
        invalidate();
    }

    @Override
    public int getLifespan() {
        return bee.getLifespan();
    }

    @Override
    public BeeType getBeeType() {
        return bee.getBeeType();
    }

    @Override
    public int getCurrentHealth() {
        return bee.getCurrentHealth();
    }

    @Override
    public int getPointsPerHit() {
        return bee.getPointsPerHit();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawRect(width - barWidth, height - barHeight, width, height, paint);
    }

    private void playHitAnim(final boolean died) {
        if (animator != null) {
            animator.cancel();
        }

        float valueFrom, valueTo;
        valueTo = (float) bee.getCurrentHealth() / bee.getLifespan();
        if (valueTo < 0) valueTo = 0;
        valueFrom = (float) (bee.getCurrentHealth() + bee.getPointsPerHit()) / bee.getLifespan();
        animator = ValueAnimator.ofFloat(valueFrom, valueTo);
        animator.setDuration(300);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue());
                barHeight = (getHeight() - barWidth) * value;
                hsv[0] = from[0] + (to[0] - from[0]) * value;
                hsv[1] = from[1] + (to[1] - from[1]) * value;
                hsv[2] = from[2] + (to[2] - from[2]) * value;
                paint.setColor(Color.HSVToColor(hsv));
                if (died) setAlpha(1 - animation.getAnimatedFraction());
                invalidate();
            }
        });

        animator.start();
    }

    public void cancelAnim(){
        if (animator != null) {
            animator.cancel();
        }
    }


}

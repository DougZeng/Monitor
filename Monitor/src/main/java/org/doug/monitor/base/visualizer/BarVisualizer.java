package org.doug.monitor.base.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by wesine on 2018/6/21.
 */

public class BarVisualizer extends BaseVisualizer {

    private float density = 50;
    private int gap;

    public BarVisualizer(Context context) {
        super(context);
    }

    public BarVisualizer(Context context,
                         @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarVisualizer(Context context,
                         @Nullable AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        this.density = 50;
        this.gap = 4;
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Sets the density to the Bar visualizer i.e the number of bars
     * to be displayed. Density can vary from 10 to 256.
     * by default the value is set to 50.
     *
     * @param density density of the bar visualizer
     */
    public void setDensity(float density) {
        this.density = density;
        if (density > 256) {
            this.density = 256;
        } else if (density < 10) {
            this.density = 10;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bytes != null) {
            float barWidth = getWidth() / density;
            float div = bytes.length / density;
            paint.setStrokeWidth(barWidth - gap);

            for (int i = 0; i < density; i++) {
                int bytePosition = (int) Math.ceil(i * div);
                int top = canvas.getHeight() +
                        ((byte) (Math.abs(bytes[bytePosition]) + 128)) * canvas.getHeight() / 128;
                float barX = (i * barWidth) + (barWidth / 2);
                canvas.drawLine(barX, canvas.getHeight(), barX, top, paint);
            }
            super.onDraw(canvas);
        }
    }
}

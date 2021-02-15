package net.rsnode.itschool5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private boolean active = true;
    private float x;
    private Graphics g;

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.g.destroy()
        x = event.getX();
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas cnv = getHolder().lockCanvas();
        this.x = cnv.getWidth() / 2.0f;
        getHolder().unlockCanvasAndPost(cnv);

        this.g = new Graphics(getContext(), getHolder());
        g.start();
       (new Thread() {
           @Override
           public void run() {
               while (active) {
                   try {
                       g.p1.setColor(Color.RED);
                       g.p2.setColor(Color.BLUE);
                       Thread.sleep(1000);
                       g.p1.setColor(Color.BLUE);
                       g.p2.setColor(Color.RED);
                       Thread.sleep(1000);
                   } catch (Exception e) {
                   }
               }
           }
       }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        active = false;
    }

    class Graphics extends Thread {
        private SurfaceHolder sh;
        private Paint bg = new Paint();
        public Paint p1 = new Paint();
        public Paint p2 = new Paint();

        public Graphics(Context context, SurfaceHolder surfaceHolder) {
            this.sh = surfaceHolder;
            this.bg.setColor(Color.GREEN);
            this.p1.setColor(Color.RED);
            this.p2.setColor(Color.BLUE);
        }

        @Override
        public void run() {
            while (active) {
                Canvas canvas = sh.lockCanvas();
                if (canvas != null) {
                    try {
                        canvas.drawRect(0, 0, x, canvas.getHeight(), this.p1);
                        canvas.drawRect(x + 1, 0, canvas.getWidth(), canvas.getHeight(), this.p2);
                    }
                    catch (Exception e) {
                    }
                    sh.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
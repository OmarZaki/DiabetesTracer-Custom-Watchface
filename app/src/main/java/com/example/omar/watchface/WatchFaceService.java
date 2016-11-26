package com.example.omar.watchface;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import java.util.concurrent.TimeUnit;

/**
 * Created by OMAR on 11/26/2016.
 */

public class WatchFaceService extends CanvasWatchFaceService {


    @Override
    public Engine onCreateEngine() {
        return new watchEngine();
    }

    private class watchEngine extends CanvasWatchFaceService.Engine{


        private final long TICK_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(1); // the ticks will be in milliseconds
        private WatchFace watchFace;
        /**
         * It will post a runnable only if the watch is visible and not in ambient mode( this has been specified in the TickIfNecessary() method) .
         */
        private Handler timeTick;// this is gonna used to schedule a periodic ticks.
        /**
         * The Runnable that the handler is going to be posted by the Handler . It invalidates the watch '
         * and schedules another run of itself on the handler with a delay of one second ( Since we want to tick every second) if necessary.
         *
         */
        private final Runnable timeRunnable = new Runnable(){
            @Override
            public void run() {
                oSecondTick();
                if(isVisible() && !isInAmbientMode()){
                    timeTick.postDelayed(this,TICK_PERIOD_MILLIS);
                }
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            /**
             * The engine provides an onCreate(SurfaceHolder holder) method
             * where you can define your watch face style and other graphical elements.
             * In defining the watch face style, you can customise how the UI elements
             * such as the battery indicator are drawn over the watch face or
             * how the cards are behaving in both normal and ambient mode.
             * In order to make use of all of these you must call the
             * setWatchFaceStyle (WatchFaceStyle watchFaceStyle) method.
             * Our SimpleEngine class will look like this:
             */
            setWatchFaceStyle(new WatchFaceStyle.Builder(WatchFaceService.this)               // WatchFaceStyle.Builder used to build UI for the watch face
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)                            // this will specify the first card peeked and shown on the watch will have a single line tail ( It will have a small height)
                    .setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_HIDDEN)                // when the watch enters in the ambient mode, no peek card will be visible.
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE) // the background of the peek card should only shown briefly, and only if the peek card  represents an interruptive notification
                    .setShowSystemUiTime(false)                             // we set the UI time to false because we will already show the time on the watch by drawing it onto the canvas.
                    .build());

            /**
             * Initiate the timeTick Handler.
             */
            timeTick = new Handler(Looper.myLooper());
            startTimerIfNecessary();
            /**
             * Initiate the timeTick Handler
             */
            watchFace = WatchFace.newInstance(WatchFaceService.this);

        }

        /**
         * This method is posting the runnable to the handler.
         */
        private void startTimerIfNecessary() {
            timeTick.removeCallbacks(timeRunnable);
            if(isVisible() && !isInAmbientMode()){
                timeTick.post(timeRunnable);

            }
        }

        /**
         * This method is invoking the invalidate method every second. to update the View.
         * Hint: invalidate() method recall the onDraw() method in order to update the elements of the View.
         *
         */
        private void oSecondTick() {
            invalidateIfNecessary();
        }

        /**
         * draw the view again if the watch is visible and not in the ambient mode .
         *
         */
        private void invalidateIfNecessary() {
            if (isVisible() && !isInAmbientMode()) {
                invalidate();
            }
        }

        /**
         * The most used callback of the watch face Engine, it will be called every time the watch face is invalidated.
         * Here we will define the draw logic of the watch face using the provided
         * canvas and Rect that defines the watch face bounds.
         *
         * @param canvas
         * @param bounds
         */
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            watchFace.draw(canvas, bounds);
        }

        /**
         * This callback will be invoked every minute when the Smart Watch in the AMBIENT MODE.
         * IT IS VERY IMPORTANT to consider that this callback method is called only while on the Ambient Mode.
         */
        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate(); //
        }

        /**
         * This is Callback method is called when the watch becomes visible or not.
         * If you decided to Override this method you have to call first the super.onVisibilityChanged(visible) ;
         *
         * @param visible
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            startTimerIfNecessary();
        }

        /**
         * This callback method is invoked when the device enter or exit the ambient mode.
         *
         * @param inAmbientMode
         */
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            if(inAmbientMode) {
                watchFace.setColor(Color.GRAY,Color.GRAY,Color.GRAY);
                watchFace.setImage(BitmapFactory.decodeResource(WatchFaceService.this.getResources(),R.drawable.icon_dose_gray));
                watchFace.setShowSeconds(false);
            }else{
                watchFace.setAntiAlias(true);
                watchFace.setColor(Color.WHITE,Color.RED,Color.CYAN);
                watchFace.setShowSeconds(true);
                watchFace.setImage(BitmapFactory.decodeResource(WatchFaceService.this.getResources(),R.drawable.dose_img));
            }

            invalidate();
            startTimerIfNecessary();
        }

        /**
         * called when the first, peeking card positions itself on the screen. The rect provides information about the position of the card when it's peeking at bottom and
         * allowing the watch face to be exposed.
         * Here you can change the watch face appearance depending on where the card it on the screen.
         *
         * @param rect
         */
        @Override
        public void onPeekCardPositionUpdate(Rect rect) {
            super.onPeekCardPositionUpdate(rect);
        }

        /**
         * Remove all the call backs from the Handler.
         */
        @Override
        public void onDestroy() {
            timeTick.removeCallbacks(timeRunnable);
            super.onDestroy();
        }



    }
}

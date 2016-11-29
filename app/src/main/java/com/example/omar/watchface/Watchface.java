package com.example.omar.watchface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by OMAR on 11/26/2016.
 */

public class WatchFace {

    /**
     * Text format string.
     */
    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d.%02d"; // for the ambient mode
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ".%02d"; // for the active mode
    private static final String DATE_FORMAT = "%02d.%02d.%d";
    //private static final String COUTDOWN_DOSE_FORMAT = "%02dh.%02dm";


    /**
     * The Paint class holds the style and color information about how to draw geometries, text and bitmaps.
     */
    private final Paint imagePaint;
    private final Paint timePaint; // for the time  with following format 20.30.22
    private final Paint datePaint; // for the date  with following format
    private final Paint dosePaint; // for the date  with following format
    private Bitmap image;
    private final Time time;
    private boolean shouldShowSeconds = true;

    /**
     * new Instance
     *
     * @param context
     * @return
     */
    public static WatchFace newInstance(Context context) {

        Paint imagePaint = new Paint();
        imagePaint.setColor(Color.RED);
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.dose_img);
        imagePaint.setAntiAlias(true); //Helper for setFlags(), setting or clearing the ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn, but is has no impact on the interior of the shape.

        Paint dosePaint = new Paint();
        dosePaint.setColor(Color.CYAN);
        dosePaint.setTextSize(context.getResources().getDimension(R.dimen.dose_size));
        dosePaint.setAntiAlias(true); //Helper for setFlags(), setting or clearing the ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn, but is has no impact on the interior of the shape.

        Paint timePaint = new Paint();
        timePaint.setColor(Color.WHITE);
        timePaint.setTextSize(context.getResources().getDimension(R.dimen.time_size));
        timePaint.setAntiAlias(true);

        Paint datePaint = new Paint();
        datePaint.setColor(Color.WHITE);
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);

        return new WatchFace(timePaint, datePaint, dosePaint, imagePaint, b, new Time());
    }

    /**
     * Constructor for watch face class
     *
     * @param timePaint
     * @param datePaint
     * @param dosePaint
     * @param imagePaint
     * @param image
     * @param time
     */
    WatchFace(Paint timePaint, Paint datePaint, Paint dosePaint, Paint imagePaint, Bitmap image, Time time) {

        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.dosePaint = dosePaint;
        this.imagePaint = imagePaint;
        this.image = image;

        this.time = time;
    }


    /**
     * actual drawing on the canvas.
     *
     * @param canvas
     * @param bounds
     */
    public void draw(Canvas canvas, Rect bounds) {
        time.setToNow();
        canvas.drawColor(Color.BLACK);

        String doseText = "4h.30m"; // should be coming from the phone later on.
        float doseXOffset = computeXOffset(doseText, dosePaint, bounds);
        float doseYOffset = computeDoseYOffset(doseText, dosePaint, bounds);
        canvas.drawText(doseText, doseXOffset, doseYOffset, dosePaint);


        float imageXOffset = computeXImageOffset(image, bounds);
        float imageYOffset = computeImageYOffset(this.image, bounds);
        canvas.drawBitmap(image, imageXOffset, imageYOffset, imagePaint);


        String timeText = String.format(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS, time.hour, time.minute, time.second);
        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = computeTimeYOffset(timeText, timePaint, bounds);
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);

        String dateText = String.format(DATE_FORMAT, time.monthDay, (time.month + 1), time.year);
        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeDateYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);

    }

    /**
     * compute the Xs and Ys for the drawn elements
     */
    private float computeXImageOffset(Bitmap image, Rect bounds) {
        float centerX = bounds.exactCenterX();
        float imageWidth = image.getWidth();
        return centerX - (imageWidth / 2.0f);
    }

    private float computeImageYOffset(Bitmap imageBitMap, Rect bounds) {
        float centerY = bounds.exactCenterY();
        int imgHeight = imageBitMap.getHeight();
        return (centerY) - (imgHeight);
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeTimeYOffset(String timeText, Paint timePaint, Rect watchBounds) {
        float centerY = watchBounds.exactCenterY();
        Rect textBounds = new Rect();
        timePaint.getTextBounds(timeText, 0, timeText.length(), textBounds);
        int textHeight = textBounds.height();
        return centerY + (centerY / 2) + (textHeight / 2.0f);
    }

    private float computeDateYOffset(String dateText, Paint datePaint) {
        Rect textBounds = new Rect();
        datePaint.getTextBounds(dateText, 0, dateText.length(), textBounds);
        return textBounds.height() + 10.0f;
    }

    private float computeDoseYOffset(String doseText, Paint dosePaint, Rect watchBounds) {
        float centerY = watchBounds.exactCenterY();
        Rect doseBounds = new Rect();
        dosePaint.getTextBounds(doseText, 0, doseText.length(), doseBounds);
        int textHeight = doseBounds.height();
        return centerY + textHeight;
    }

    /**
     * setters for the smart watch faces
     */
    public void setAntiAlias(boolean antiAlias) {
        timePaint.setAntiAlias(antiAlias);
        datePaint.setAntiAlias(antiAlias);
        dosePaint.setAntiAlias(antiAlias);
        imagePaint.setAntiAlias(antiAlias);
    }

    public void setColor(int color, int doseColor, int imageColor) {
        timePaint.setColor(color);
        datePaint.setColor(color);
        dosePaint.setColor(doseColor);
        dosePaint.setColor(imageColor);

    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}

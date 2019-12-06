package de.tudo.swipeit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.DrawableContainer;

public class TextDrawable extends DrawableContainer {

    private final String _mText;
    private final Paint _mPaint;
    private Point _mPosition;

    public TextDrawable(String text) {
        this._mText = text;

        _mPaint = new Paint();
        _mPaint.setColor(Color.WHITE);
        _mPaint.setTextSize(32f);
        _mPaint.setAntiAlias(true);
        _mPaint.setFakeBoldText(true);
//        _mPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        _mPaint.setStyle(Paint.Style.FILL);
        _mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(_mText, _mPosition.x, _mPosition.y, _mPaint);
    }

    public void setPosition(Point position){
        _mPosition = position;
    }
}

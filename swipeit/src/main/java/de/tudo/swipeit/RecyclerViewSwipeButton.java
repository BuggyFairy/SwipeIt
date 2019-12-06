package de.tudo.swipeit;

import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class RecyclerViewSwipeButton {

    public final ColorDrawable mColorBackground;
    public final Drawable mIcon;
    public final TextDrawable mText;
    private final SwipeButtonClickListener _mClickListener;
    private RectF _mClickRegion;

    public RecyclerViewSwipeButton(ColorDrawable colorBackground, Drawable icon, TextDrawable text, SwipeButtonClickListener clickListener) {
        this.mColorBackground = colorBackground;
        this.mIcon = icon;
        this.mText = text;
        this._mClickListener = clickListener;
    }

    public boolean onClick(float x, float y, int swipedItem){
        if( _mClickRegion != null && _mClickRegion.contains(x,y) ){
            _mClickListener.onClick(swipedItem);
            return true;
        }
        return false;
    }

    public void setClickRegion(RectF clickRegion){
        _mClickRegion = clickRegion;
    }
}
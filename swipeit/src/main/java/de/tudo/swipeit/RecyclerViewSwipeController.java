package de.tudo.swipeit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

public class RecyclerViewSwipeController extends ItemTouchHelper.SimpleCallback {

    private final RecyclerView _mRecyclerView;
    private final List<RecyclerViewSwipeButton> _mRecyclerViewSwipeButtons;
    private final int _mButtonWidth;
    private final Queue<Integer> _mOpenSwipedItems;
    private int _mSwipedItem;

    /**
     * Wird gebraucht, um nach dem swipe einen Click zu registrieren. Fragt man onClick im onTouchListener auf,
     * so kann er während des Swipens ausgelöst werden.
     * */
    private final GestureDetector _mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e){
            for(RecyclerViewSwipeButton button : _mRecyclerViewSwipeButtons){
                if(button.onClick(e.getX(), e.getY(), _mSwipedItem))
                    break;
            }
            return true;
        }
    };




    public RecyclerViewSwipeController(final Context context, final RecyclerView recyclerView, final int buttonWidth, final List<RecyclerViewSwipeButton> recyclerViewSwipeButtons) {
        super(0, LEFT | RIGHT);

        _mOpenSwipedItems = new LinkedList<Integer>(){
            @Override
            public boolean add(Integer integer){
                if( contains(integer) ){
                    return false;
                }
                else{
                    return super.add(integer);
                }
            }
        };

        _mRecyclerViewSwipeButtons = recyclerViewSwipeButtons;
        _mRecyclerView = recyclerView;
        _mButtonWidth = buttonWidth;
        _mSwipedItem = -1;
        _mGestureDetector = new GestureDetector(context, mGestureListener);


        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( _mSwipedItem < 0 ) return false;

                Point clickCoordinates = new Point( (int) motionEvent.getRawX(), (int) motionEvent.getRawY());

                RecyclerView.ViewHolder swipedView = _mRecyclerView.findViewHolderForAdapterPosition(_mSwipedItem);
                View itemView = swipedView.itemView;
                Rect visibleItemRect = new Rect();
                itemView.getGlobalVisibleRect(visibleItemRect);

                if( motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_MOVE){

                    if( visibleItemRect.top < clickCoordinates.y && visibleItemRect.bottom > clickCoordinates.y ){
                        _mGestureDetector.onTouchEvent(motionEvent);
                    }
                    else{
                        _mOpenSwipedItems.add(_mSwipedItem);
                        _mSwipedItem = -1;
                        resetSwipedItem();
                    }
                }
                return false;
            }
        };
        _mRecyclerView.setOnTouchListener(onTouchListener);

        attachToRecyclerView();
    }

    private synchronized void resetSwipedItem(){
        while( !_mOpenSwipedItems.isEmpty() ){
            Integer swipeItemBack = _mOpenSwipedItems.poll();
            if( swipeItemBack > -1 )
                _mRecyclerView.getAdapter().notifyItemChanged(swipeItemBack);
        }
    }

    private void attachToRecyclerView(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(_mRecyclerView);
    }



    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.1f * _mRecyclerViewSwipeButtons.size() * _mButtonWidth;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position  = viewHolder.getAdapterPosition();
        if( position != _mSwipedItem){
            _mOpenSwipedItems.add(position);
        }
        _mSwipedItem = position;
        resetSwipedItem();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if( dX > 0 ) return;

        View itemView = viewHolder.itemView;
        int pos = viewHolder.getAdapterPosition();
        if( pos < 0 ) {
            _mSwipedItem = pos;
            return;
        }

        float translationX = dX * _mRecyclerViewSwipeButtons.size() * _mButtonWidth / itemView.getWidth();

        if( actionState == ItemTouchHelper.ACTION_STATE_SWIPE ) {

            drawButtons(c, viewHolder, translationX);

        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX + 10, dY, actionState, isCurrentlyActive);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, float dX){
        if( dX < 0 ){

            View swipedView = viewHolder.itemView;

            float right = swipedView.getRight();

            for(RecyclerViewSwipeButton button : _mRecyclerViewSwipeButtons){
                float left = right - _mButtonWidth;
                RectF clickRegion = new RectF(left, swipedView.getTop(), right, swipedView.getBottom());
                button.setClickRegion(clickRegion);

                button.mColorBackground.setBounds( (int) left, swipedView.getTop() + 12, (int) right, swipedView.getBottom() - 12);

                int iconMargin = (int) ((float) (swipedView.getHeight() - button.mIcon.getIntrinsicHeight()) / 1.2);
                int iconTop = swipedView.getTop() + 20;
                int iconBottom = swipedView.getBottom() - 60;
                int iconLeft = (int) left + iconMargin;
                int iconRight = (int) right - iconMargin;
                button.mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                button.mText.setPosition( new Point( (int )(left + ((right - left) / 2)) , iconBottom + (iconMargin/2)) );

                button.mColorBackground.draw(c);
                button.mIcon.draw(c);
                button.mText.draw(c);

                right = left;
            }
        }
    }


}

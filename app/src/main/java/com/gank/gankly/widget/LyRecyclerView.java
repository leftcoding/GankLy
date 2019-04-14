package com.gank.gankly.widget;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.gank.gankly.ui.base.BaseHolder;

/**
 * reference https://github.com/moodstrikerdd/Test_Android/blob/53ecf70a131e553a68daad2b57bfe3f09722ddc2/sidesliplist/src/main/java/com/example/sidesliplist/MyRecyclerView.java
 * Create by LingYan on 2016-11-07
 */

public class LyRecyclerView extends RecyclerView {
    private int maxLength;
    private int mStartX = 0;
    private View itemLayout;
    private Rect mTouchFrame;
    private int xDown, xMove, yDown, yMove, mTouchSlop, xUp, yUp;
    private Scroller mScroller;
    private BaseHolder currentViewHolder;
    //需要滑动的item是不是当前的item
    private boolean isCurrentItem = true;
    private View otherItemView;
    private ILyRecycler mILyRecycler;
    private int position;
    private boolean intrList = false;

    public LyRecyclerView(Context context) {
        this(context, null);
    }

    public LyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //滑动到最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //初始化Scroller
        mScroller = new Scroller(context, new LinearInterpolator(context, null), true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //所有菜单不显示
                xDown = x;
                yDown = y;
                intrList = false;
                Rect frame = mTouchFrame;
                if (frame == null) {
                    mTouchFrame = new Rect();
                    frame = mTouchFrame;
                }
                int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                int count = getChildCount();
                BaseHolder viewHolder;
                int _scrollX;
                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    viewHolder = (BaseHolder) getChildViewHolder(child);
                    _scrollX = viewHolder.itemView.getScrollX();
                    if (child.getVisibility() == View.VISIBLE) {
                        child.getHitRect(frame);
                        if (frame.contains(x, y)) {
                            currentViewHolder = viewHolder;
                            itemLayout = viewHolder.itemView;
                            maxLength = viewHolder.getView().getMeasuredWidth();
                            position = mFirstPosition + i;
                        } else {
                            if (_scrollX != 0) {
                                hideMenu(viewHolder);
                                //屏蔽滑动（仿qq）
                                return false;
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (itemLayout == null) {
                    break;
                }
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                scrollX = itemLayout.getScrollX();
                if (Math.abs(dy) < mTouchSlop * 2 && Math.abs(dx) > mTouchSlop) {
                    intrList = true;
                    int newScrollX = mStartX - x;
                    if (newScrollX < 0 && scrollX <= 0) {
                        newScrollX = 0;
                    } else if (newScrollX > 0 && scrollX >= maxLength) {
                        newScrollX = 0;
                    }
                    itemLayout.scrollBy(newScrollX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                xUp = x;
                yUp = y;
                int _dx = xUp - xDown;
                int _dy = yUp - yDown;
                if (Math.abs(_dy) < mTouchSlop && Math.abs(_dx) < mTouchSlop) {
                    if (mILyRecycler != null) {
                        mILyRecycler.onClick(position);
                    }
                } else if (currentViewHolder != null) {
                    if (currentViewHolder.isShowing) {
                        hideMenu();
                    } else {
                        scrollX = itemLayout.getScrollX();
                        if (scrollX - maxLength / 5 > 0) {
                            mILyRecycler.removeRecycle(position);
                            mScroller.startScroll(scrollX, 0, -scrollX, 0);
                            currentViewHolder.isShowing = true;
                            isCurrentItem = true;
                        } else if (scrollX != 0) {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0);
                            isCurrentItem = true;
                        }
                    }
                    invalidate();
                }
                break;
            default:
                break;
        }
        mStartX = x;
        return intrList ? intrList : super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (isCurrentItem) {
                itemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            } else {
                otherItemView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 隐藏菜单
     */
    public void hideMenu() {
        hideMenu(currentViewHolder);
    }

    public void hideMenu(BaseHolder viewHolder) {
        if (viewHolder == null) {
            return;
        }
        viewHolder.isShowing = false;
        isCurrentItem = false;
        int scrollX = viewHolder.itemView.getScrollX();
        mScroller.startScroll(scrollX, 0, -scrollX, 0);
        otherItemView = viewHolder.itemView;
        invalidate();

        /**
         * 属性动画不成立，会有空白
         */
//        int[] location = new int[2];
//        myViewHolder.itemView.getLocationInWindow(location);
//        ValueAnimator animator = ValueAnimator.ofFloat(location[0],viewHolder.llMenu.getWidth());
//        animator.setDuration(500);
//        animator.setTarget(myViewHolder.llMenu);
//        animator.start();
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                myViewHolder.itemView.setTranslationX((Float) animation.getAnimatedValue());
//            }
//        });
    }

    public interface ILyRecycler {
        void removeRecycle(int pos);

        void onClick(int pos);
    }

    public void setILyRecycler(ILyRecycler lyRecycler) {
        mILyRecycler = lyRecycler;
    }

    public int getPosition() {
        return position;
    }
}
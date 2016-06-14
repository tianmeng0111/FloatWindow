package com.example.topview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/1/8.
 */
public class TopView extends LinearLayout {

    private final static String TAG = TopView.class.getSimpleName();
    
    private float rawX, rawY, x, y;

    private WindowManager.LayoutParams params;

    private WindowManager windowManager = (WindowManager)
            getContext().getApplicationContext().getSystemService(getContext().WINDOW_SERVICE);

    //是否自动停靠在屏幕边缘
    private boolean isAutoBerth = false;
    //屏幕宽
    private int screenWidth;
    //最小滑动距离
    private int scaledTouchSlop;

    private View contentView;
    private View miniView;

    public TopView(Context context) {
        super(context);
        this.setBackgroundColor(Color.TRANSPARENT);

        initTopView();
        initData(context);
    }

    private void initTopView() {

        /*注意：这里必须通过getApplicationContext获取到WindowManager，不能这样获取
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);*/
        params = new WindowManager.LayoutParams();

        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY//顶置
                | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//有这句才能onTouch

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //使底层的滑屏没有问题
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;// FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口

        params.gravity = Gravity.TOP|Gravity.LEFT;

        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色

        params.x = 0;
        params.y = 0;

//        params.alpha = 50;

        windowManager.addView(this, params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸点相对于屏幕左上角位置
        rawX = event.getRawX();
        rawY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //触摸点相对于this控件的位置
                x = event.getX();
                y = event.getY();
                if (isAutoBerth) {
                    this.getChildAt(0).getBackground().setAlpha(165);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "-----------move");
                updataMovePosition(event);
                if (isAutoBerth) {
                    this.getChildAt(0).getBackground().setAlpha(165);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                updataPosition(event);
                this.getChildAt(0).getBackground().setAlpha(255);
                break;
        }
        return true;
    }

    private void updataMovePosition(MotionEvent event) {
        if (Math.hypot(event.getY() - y, event.getX() - x) < scaledTouchSlop) {
            return;
        }
        params.x = (int) (rawX - x);
        params.y = (int) (rawY - y);
        windowManager.updateViewLayout(this, params);
    }

    private void updataPosition(MotionEvent event){
        if (Math.hypot(params.x, params.y) > scaledTouchSlop) {
            Log.e(TAG, "--------------触发");
        }

        if (Math.hypot(event.getY() - y, event.getX() - x) < scaledTouchSlop) {
            if (this.contentView != null && isAutoBerth) {
                setContentView(this.contentView);
                isAutoBerth = false;
            }
            return;
        }

        updataPosition();
    }

    private void updataPosition() {
        //this左上角距屏幕左上角的位置
        params.x = (int) (rawX - x);
        params.y = (int) (rawY - y);

        if (isAutoBerth) {
            if (params.x > screenWidth/2) {
                params.x = screenWidth - getChildAt(0).getWidth();
            } else {
                params.x = 0;
            }
            windowManager.updateViewLayout(this, params);
        }else {
            windowManager.updateViewLayout(this, params);
        }
        this.getChildAt(0).getBackground().setAlpha(255);
    }

    private void close() {

    }

    public void setAutoBerth(boolean flag) {
        this.isAutoBerth = flag;
    }

    private void initData(Context context) {
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.e(TAG, "scaledTouchSlop---------->>" + scaledTouchSlop);//32
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
        this.removeAllViews();
        this.addView(contentView);
    }

    public void setMiniView(View miniView) {
        this.miniView = miniView;
        this.removeAllViews();
        this.addView(miniView);
        this.setAutoBerth(true);
        this.updataPosition();
    }
}

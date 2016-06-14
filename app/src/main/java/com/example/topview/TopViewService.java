package com.example.topview;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/1/8.
 */
public class TopViewService extends Service {

    private final static String TAG = TopViewService.class.getSimpleName();

    private TopView topView;
    private View contentView;
    private View miniView;

    @Override
    public void onCreate() {
        super.onCreate();
        topView = new TopView(getApplicationContext());
        contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_topview, null);
        miniView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_miniview, null);
        topView.setContentView(contentView);
        ImageView imgClose = (ImageView) contentView.findViewById(R.id.img_close);
        ImageView imgMini = (ImageView) contentView.findViewById(R.id.img_mini);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "-------------------img on click");
                topView.removeAllViews();
                stopSelf();
            }
        });
        imgMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topView.setMiniView(miniView);
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "-------------------onStartCommand");
        if (topView.getChildCount() <= 0)
            topView.addView(contentView);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "-------------------onDestroy");
        super.onDestroy();
    }

}

package com.example.topview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTopView();

        Intent intent = new Intent(this, TopViewService.class);
        startService(intent);
    }

    private void initTopView() {

    }
}

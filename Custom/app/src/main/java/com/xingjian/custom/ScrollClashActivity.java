package com.xingjian.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by thinkpad on 2018/1/26.
 */

public class ScrollClashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        final ListView listView = (ListView) findViewById(R.id.lv_clash);
        ClashAdapter adapter = new ClashAdapter(this);
        listView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        final Display display = getWindowManager().getDefaultDisplay();
        layoutParams.height = display.getHeight() / 2;
        listView.setLayoutParams(layoutParams);

    }
}

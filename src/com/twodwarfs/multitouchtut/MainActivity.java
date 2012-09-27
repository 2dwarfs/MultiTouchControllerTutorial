package com.twodwarfs.multitouchtut;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MultiTouchView multiTouchView = (MultiTouchView) findViewById(R.id.canvas_view);
    
        Bitmap itemBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.logo)).getBitmap();
        multiTouchView.setPinchWidget(itemBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

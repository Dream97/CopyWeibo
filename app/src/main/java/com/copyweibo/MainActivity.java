package com.copyweibo;

import android.app.Activity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

/**
 * Created by asus on 2017/5/6.
 */

public class MainActivity extends Activity implements BottomNavigationBar.OnTabSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_bar);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.tab_home,"微博"))
                .addItem(new BottomNavigationItem(R.drawable.tab_msg,"消息"))
                .addItem(new BottomNavigationItem(R.drawable.write,"发表"))
                .addItem(new BottomNavigationItem(R.drawable.find,"发现"))
                .addItem(new BottomNavigationItem(R.drawable.me,"我"))
                .initialise();


        bottomNavigationBar.setTabSelectedListener(this);



     }

    /**
     * bottomBar的监听
     * @param position
     */
    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}

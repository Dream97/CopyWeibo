package com.copyweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by asus on 2017/4/26.
 */

public class FadeInActvity extends Activity{
    ImageView imageView ;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fade_in);
        imageView = (ImageView) findViewById(R.id.image_fade);
        //透明度渐变动画,透明度范围
        AlphaAnimation animation = new AlphaAnimation(0.1f,1.0f);
        //设置动画时长
        animation.setDuration(3000);
        //将组件与动画进行关联
        imageView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(FadeInActvity.this,OAuthActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}

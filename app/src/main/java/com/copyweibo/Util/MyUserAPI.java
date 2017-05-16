package com.copyweibo.Util;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.UsersAPI;

/**
 * Created by asus on 2017/5/6.
 */

public class MyUserAPI extends UsersAPI {
    private static MyUserAPI myUserAPI;
    private MyUserAPI(Context context, Oauth2AccessToken oauth2AccessToken){
        super(context,Constants.APP_KEY,oauth2AccessToken);
    };
    public static MyUserAPI getInstance(Context context, Oauth2AccessToken oauth2AccessToken)
    {
        if (myUserAPI==null)
        {
            myUserAPI = new MyUserAPI(context,oauth2AccessToken);
        }
        return myUserAPI;
    }
}

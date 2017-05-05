package com.copyweibo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.copyweibo.Util.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class OAuthActivity extends AppCompatActivity implements View.OnClickListener {

    //登录按钮
    private Button btnSsoLogIn;
    private Button btnWebLogIn;
    private Button btnAllInOneLogIn;
    //显示accessToken
    private TextView tvAccessToken;
    //用于显示用户具体信息的控件
    private TextView tvNickName;
    private TextView tvGender;
    private TextView tvLocation;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    //获取用户信息的接口（需要先把官方提供的weibosdk库引入到工程当中来）
    private UsersAPI mUsersAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);

        initView();
    }

    private void initView() {
        btnSsoLogIn = (Button) findViewById(R.id.logIn_btn_SsoLogin);
        btnSsoLogIn.setOnClickListener(this);
        btnWebLogIn = (Button) findViewById(R.id.logIn_btn_WebLogin);
        btnWebLogIn.setOnClickListener(this);
        btnAllInOneLogIn = (Button) findViewById(R.id.logIn_btn_AllInOneLogin);
        btnAllInOneLogIn.setOnClickListener(this);
        tvAccessToken = (TextView) findViewById(R.id.logIN_tv_accessToken);

        //用户信息
        tvNickName = (TextView) findViewById(R.id.logIn_tv_nickName);
        tvGender = (TextView) findViewById(R.id.logIn_tv_gender);
        tvLocation = (TextView) findViewById(R.id.logIn_tv_location);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //sso授权，仅客户端
            case R.id.logIn_btn_SsoLogin:
                startSsoWeiBoLogIn();
                break;
            //web授权
            case R.id.logIn_btn_WebLogin:
                startWebWeiBoLogIn();
                break;
            //自动检测，若有客户端，则用sso授权，若没有客户端则用web授权
            case R.id.logIn_btn_AllInOneLogin:
                startAllInOneWeiBoLogIn();
                break;
        }
    }

    /**
     * all in one 方式授权，自动检测
     */
    private void startAllInOneWeiBoLogIn() {
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * Web授权
     */
    private void startWebWeiBoLogIn() {
        mSsoHandler.authorizeWeb(new AuthListener());
    }

    /**
     * SSO授权，仅客户端
     */
    private void startSsoWeiBoLogIn() {
        mSsoHandler.authorizeClientSso(new AuthListener());
        //通过accessToken获取用户信息
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
//            System.out.println("onComplete(Bundle values)  ------>  "
//                    + bundle.toString());
            // onComplete(Bundle values) ------>
            // Bundle[ {_weibo_transaction = 1469413517894,
            // access_token = 2.00a64JBGyY87OCfa7707a82fzincGB,
            // refresh_token = 2.00a64JBGyY87OC11c02519480EWT1g,
            // expires_in = 2651682,
            // _weibo_appPackage = com.sina.weibo,
            // com.sina.weibo.intent.extra.NICK_NAME = 用户5513808278,
            // userName = 用户5513808278,
            // uid = 5513808278,
            // com.sina.weibo.intent.extra.USER_ICON = null} ]


            //从Bundle中解析Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
//            System.out.println("onComplete  mAccessToken  ------>  "
//                    + mAccessToken.toString());
            // onComplete mAccessToken ------>
            // uid: 5513808278,
            // access_token: 2.00a64JBGyY87OCfa7707a82fzincGB,
            // refresh_token: 2.00a64JBGyY87OC11c02519480EWT1g,
            // phone_num: ,
            // expires_in: 1472065200534

            if (mAccessToken.isSessionValid()) {//授权成功
                Toast.makeText(OAuthActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                //显示Access_Token
                tvAccessToken.setText("Access_token:\n" + mAccessToken.getToken());
                //获取用户具体信息
                getUserInfo();
            } else {
                /**
                 *  以下几种情况，您会收到 Code：
                 * 1. 当您未在平台上注册应用程序的包名与签名时；
                 * 2. 当您注册的应用程序包名与签名不正确时；
                 * 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                 */
                String code = bundle.getString("code");//直接从bundle里边获取
                if (!TextUtils.isEmpty(code)) {
                    Toast.makeText(OAuthActivity.this, "签名不正确", Toast.LENGTH_SHORT).show();
                }
            }
//            String phoneNum = mAccessToken.getPhoneNum();//通过手机短信授权登录时可以拿到，此demo未实现此种授权方式
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(OAuthActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(OAuthActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 获取用户个人信息
     */
    private void getUserInfo() {
        //获取用户信息接口
        mUsersAPI = new UsersAPI(OAuthActivity.this, Constants.APP_KEY, mAccessToken);
        System.out.println("mUsersAPI  ----->   " + mUsersAPI.toString());

        //调用接口
        long uid = Long.parseLong(mAccessToken.getUid());
        System.out.println("--------------uid-------------->    " + uid);
        mUsersAPI.show(uid, mListener);//将uid传递到listener中，通过uid在listener回调中接收到该用户的json格式的个人信息
    }
    /**
     * 实现异步请求接口回调，并在回调中直接解析User信息
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                //调用User#parse将JSON串解析成User对象
                User user = User.parse(response);
                String nickName = user.screen_name;
                tvNickName.setText("用户昵称： " + user.screen_name);
                tvGender.setText("用户性别： " + user.gender);
                tvLocation.setText("用户所在地： " + user.location);
//                Toast.makeText(LogInActivity.this, "用户的昵称： " + nickName, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         *如果运行测试的时候，登录的账号不是注册应用的账号，那么需要去：
         *开放平台-》管理中心-》应用信息-》测试信息-》添加测试账号（填写用户昵称）！
         * 否则便会抛出以下异常
         */
        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
            Toast.makeText(OAuthActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 1.仅sso授权时，当 SSO 授权 Activity 退出时，该函数被调用。
     * 2.仅
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SSO 授权回调
        //重要：发起sso登录的activity必须重写onActivtyResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}

package com.copyweibo.Util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.TextView;

import com.copyweibo.R;

/**
 * Created by asus on 2017/4/26.
 */

public class CommonTool {
    /**
     * 设置网络
     *
     * @param context
     */
    public static void checkNetWork(final Context context) {
        if (!NetWorkStatus(context)) {
            TextView msg = new TextView(context);
            msg.setText("请设置网络！");
            AlertDialog.Builder b = new AlertDialog.Builder(context).
                    setIcon(R.drawable.no_net_icon)
                    .setTitle("没有可用的网络")
                    .setMessage("是否对网络进行设置？");
            b.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //跳转到设置网络界面
                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            }).create().show();
        }
    }

    /**
     * 判断网络状态
     */
    public static boolean NetWorkStatus(Context context) {
        //获取系统的连接服务
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else {
            // 获取代表联网状态的NetWorkInfo对象,获取网络的连接情况
            NetworkInfo[] infos = connect.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo network : infos) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

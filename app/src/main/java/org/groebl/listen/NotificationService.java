package org.groebl.listen;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;


public class NotificationService extends NotificationListenerService {

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if(!sbn.isOngoing()) {

            String pack = sbn.getPackageName();
            Bundle extras = sbn.getNotification().extras;
            String textlines = null;

            String ticker = null;
            if (sbn.getNotification().tickerText != null) {
                ticker = sbn.getNotification().tickerText.toString();
            }

            String title = null;
            if (extras.get(Notification.EXTRA_TITLE) != null) {
                title = extras.get(Notification.EXTRA_TITLE).toString();
            }

            String title_big = null;
            if (extras.get(Notification.EXTRA_TITLE_BIG) != null) {
                title_big = extras.get(Notification.EXTRA_TITLE_BIG).toString();
            }

            String text = null;
            if (extras.get(Notification.EXTRA_TEXT) != null) {
                text = extras.get(Notification.EXTRA_TEXT).toString();
            }

            String text_big = null;
            if (extras.get(Notification.EXTRA_BIG_TEXT) != null) {
                title_big = extras.get(Notification.EXTRA_BIG_TEXT).toString();
            }

            String summary = null;
            if(extras.get(Notification.EXTRA_SUMMARY_TEXT) != null) {
                summary = extras.get(Notification.EXTRA_SUMMARY_TEXT).toString();
            }

            String subtext = null;
            if (extras.get(Notification.EXTRA_SUB_TEXT) != null) {
                summary = extras.get(Notification.EXTRA_SUB_TEXT).toString();
            }

            String infotext = null;
            if (extras.get(Notification.EXTRA_INFO_TEXT) != null) {
                infotext = extras.get(Notification.EXTRA_INFO_TEXT).toString();
            }


            CharSequence[] textline = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            if (textline != null) {
                textlines = String.valueOf(textline.length)+"<br>- ";
                textlines += TextUtils.join("<br>- ", textline);
            }


            String tag = sbn.getTag();
            //String key = sbn.getKey();
            Boolean clear = sbn.isClearable();
            Boolean ongoing = sbn.isOngoing();
            Long time1 = sbn.getNotification().when;
            Boolean time2 = extras.getBoolean(Notification.EXTRA_SHOW_WHEN);
            Integer getid = sbn.getId();
            //String groupkey = sbn.getGroupKey();
            Long time3 = sbn.getPostTime();


            Intent msgrcv = new Intent("Msg");
            msgrcv.putExtra("pack", pack);
            msgrcv.putExtra("ticker", ticker);
            msgrcv.putExtra("title", title);
            msgrcv.putExtra("title_big", title_big);
            msgrcv.putExtra("tag", tag);
            msgrcv.putExtra("text", text);
            msgrcv.putExtra("text_big", text_big);
            msgrcv.putExtra("clear", clear);
            msgrcv.putExtra("ongoing", ongoing);
            msgrcv.putExtra("summary", summary);
            msgrcv.putExtra("infotext", infotext);
            msgrcv.putExtra("textlines", textlines);
            msgrcv.putExtra("time1", time1);
            msgrcv.putExtra("time2", time2);
            msgrcv.putExtra("time3", time3);
            msgrcv.putExtra("subtext", subtext);
            msgrcv.putExtra("getid", getid);


            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        }

    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}

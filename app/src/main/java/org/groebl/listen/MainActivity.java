package org.groebl.listen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    TableLayout tab;

    public static Boolean set_notification = true;
    public static Boolean set_bluetooth = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TableLayout)findViewById(R.id.tab);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNoticeNotification, new IntentFilter("Msg"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNoticeBt, new IntentFilter("BT"));

        String enabledNotificationListeners = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(this.getPackageName())) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_blue:
                if(item.isChecked()) { set_bluetooth = false; item.setChecked(false); } else { set_bluetooth = true; item.setChecked(true); }
                showToast(set_bluetooth ? "Bluetooth-Änderungen werden angezeigt" : "Bluetooth-Änderungen werden nicht angezeigt");
                return true;
            case R.id.action_notification:
                if(item.isChecked()) { set_notification = false; item.setChecked(false); } else { set_notification = true; item.setChecked(true); }
                showToast(set_notification ? "Notifications werden angezeigt" : "Notifications werden nicht angezeigt");
                return true;
            case R.id.action_save:
                String output = "";
                for(int i=0; i< tab.getChildCount(); i++) {
                    output += ((TextView) ((TableRow) tab.getChildAt(i)).getChildAt(0)).getText().toString() + "\n";
                }

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, output);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            default:
               return super.onOptionsItemSelected(item);
        }

    }

    private void showToast(String message) { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); }

    private BroadcastReceiver onNoticeNotification= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(set_notification && !intent.getStringExtra("pack").equals("com.google.android.gms")) {
                String pack = intent.getStringExtra("pack");
                String ticker = intent.getStringExtra("ticker");
                String title = intent.getStringExtra("title");
                String title_big = intent.getStringExtra("title_big");
                String text = intent.getStringExtra("text");
                String text_big = intent.getStringExtra("text_big");
                String tag = intent.getStringExtra("tag");
                String summary = intent.getStringExtra("summary");
                String infotext = intent.getStringExtra("infotext");
                String textlines = intent.getStringExtra("textlines");
                String subtext = intent.getStringExtra("subtext");
                Integer getid = intent.getIntExtra("getid", 0);
                Long time1 = intent.getLongExtra("time1", 0);
                Boolean time2 = intent.getBooleanExtra("time2", false);
                Long time3 = intent.getLongExtra("time3", 0);

                String output = "<b>" + pack + "</b>";
                output += "<br>Titel: " + title;
                if (title_big != null) { output += " (" + title_big + ")"; }
                output += "<br>Text: " + text;
                if (text_big != null) { output += " (" + text_big + ")"; }


                /*
                String char_out = "";
                for (int i = 0; i < text.length(); i++) {
                    char_out = char_out + ":" + Integer.toHexString(text.charAt(i));
                }

                if (!char_out.equals("")) { output += "<br>Char: " + char_out; }
                */


                if (ticker != null) { output += "<br>Ticker: "+ ticker; }
                if (summary != null) { output += "<br>Summary: " + summary; }
                if (textlines != null) { output += "<br>Lines: " + textlines; }
                if (tag != null) { output += "<br>Tag: " + tag; }
                if (infotext != null) { output += "<br>Infotext: " + infotext; }
                if (subtext != null) { output += "<br>Subtext: " + subtext; }
                if (getid != 0) { output += "<br>ID: "+getid.toString(); }
                output += "<br>Time: " + (System.currentTimeMillis() - time1) + " / " + (System.currentTimeMillis() - time3) + " / " + time2;
                output += "<br>------------------";

                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B0719"));
                textview.setText(Html.fromHtml(output));
                tr.addView(textview);
                tab.addView(tr);
            }

        }
    };


    private BroadcastReceiver onNoticeBt= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(set_bluetooth) {
                String ticker = intent.getStringExtra("ticker");
                String content = intent.getStringExtra("content");

                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B07B9"));
                textview.setText(Html.fromHtml("<b>" + ticker + "</b> (" + content + ")<br>------------------"));
                tr.addView(textview);
                tab.addView(tr);
            }
        }
    };
}

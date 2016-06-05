package org.groebl.listen;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class BluetoothService extends BroadcastReceiver {

    final String TAG = "BTBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        String tickerText = null;
        String contentText = "Address: " + device.getAddress();

        String action = intent.getAction();
        Log.d(TAG, action);

        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
            Log.d(TAG, "Bond state changed to " + state);

            if (state == BluetoothDevice.BOND_BONDED) {
                tickerText = "Paired with " + device.getName();
            }
            else if (state == BluetoothDevice.BOND_BONDING) {
                tickerText = "Pairing with " + device.getName() + "...";
            }
            else if (state == BluetoothDevice.BOND_NONE) {
                tickerText = "Unpaired with " + device.getName();
            }
            else return;
        }
        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            Log.d(TAG, "Connected");
            tickerText = "Connected to " + device.getName();
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            Log.d(TAG, "Disconnected");
            tickerText = "Disconnected from " + device.getName();
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            Log.d(TAG, "Disconnect requested");
            tickerText = "Request disconnect from " + device.getName();
        }
        else return;


        Intent msgrcv = new Intent("BT");
        msgrcv.putExtra("ticker", tickerText);
        msgrcv.putExtra("content", device.getAddress());

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

    }


}

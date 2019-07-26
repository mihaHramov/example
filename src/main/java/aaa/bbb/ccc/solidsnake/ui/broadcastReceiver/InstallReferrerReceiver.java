package aaa.bbb.ccc.solidsnake.ui.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import aaa.bbb.ccc.solidsnake.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

public class InstallReferrerReceiver extends BroadcastReceiver {
    private static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String KEY_REFERRER = "referrer";
    public static final String REFERRER_DATA = "REFERRER_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ACTION_INSTALL_REFERRER.equals(intent.getAction())) {
            return;
        }

        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        Bundle extras = intent.getExtras();
        if (!sp.contains(REFERRER_DATA) && extras != null) {
            sp.edit().putString(REFERRER_DATA, (String) extras.get(KEY_REFERRER)).apply();
        }
    }
}

package net.crazystar.crazycall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author jay
 */
public class PhoneStateReceiver extends BroadcastReceiver {
    private static final String TAG = "CRAZY" + PhoneStateReceiver.class.getSimpleName();
    private String currentNumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony == null) {
            return;
        }
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        if ("android.intent.action.NEW_OUTGOING_CALL".equals(intent.getAction())) {
                            Bundle extras = intent.getExtras();
                            if (extras == null) {
                                return;
                            }
                            currentNumber = extras.getString("android.intent.extra.PHONE_NUMBER");
                            Log.d(TAG, "!!!!!!!!" + currentNumber);
                        } else {
                            Log.d(TAG, "!!!!!canStart:" + CrazyCallActivity.canStart);
                            if (!TextUtils.isEmpty(currentNumber) && CrazyCallActivity.canStart) {
                                CrazyCallActivity.canStart = false;
                                Log.d(TAG, "!!!!!start CrazyCallActivity!!!!!");
                                Intent crazyCall = new Intent(context, CrazyCallActivity.class);
                                crazyCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                crazyCall.putExtra("currentNumber", currentNumber);
                                currentNumber = null;
                                context.startActivity(crazyCall);
                            }
                        }
                        break;
                    default:
                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}

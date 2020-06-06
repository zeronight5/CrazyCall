package net.crazystar.crazycall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

/**
 * @author jay
 */
public class PhoneStateReceiver extends BroadcastReceiver {
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
                            System.out.println("!!!!!!!!" + currentNumber);
                        } else {
                            Intent crazyCall = new Intent(context, CrazyCallActivity.class);
                            crazyCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            crazyCall.putExtra("currentNumber", currentNumber);
                            context.startActivity(crazyCall);
                        }
                        break;
                    default:
                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}

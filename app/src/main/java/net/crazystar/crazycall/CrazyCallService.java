package net.crazystar.crazycall;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author jay
 */
public class CrazyCallService extends Service {
    private static final String TAG = CrazyCallService.class.getSimpleName();
    private PowerManager.WakeLock wakeLock;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager systemService = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (systemService != null) {
            wakeLock = systemService.newWakeLock(PowerManager.FULL_WAKE_LOCK, "net.crazystar.crazycall:CrazyCall");
            wakeLock.acquire(60*60*1000L);
        }
        registerPhoneStateReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
        }
        unregisterPhoneStateReceiver();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private PhoneStateReceiver phoneStateReceiver = new PhoneStateReceiver();

    private void registerPhoneStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(phoneStateReceiver, filter);
    }

    private void unregisterPhoneStateReceiver() {
        unregisterReceiver(phoneStateReceiver);
    }
}

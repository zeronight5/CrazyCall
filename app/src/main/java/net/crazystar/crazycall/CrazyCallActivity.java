package net.crazystar.crazycall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.disposables.Disposable;

/**
 * @author jay
 */
public class CrazyCallActivity extends AppCompatActivity {
    private static final String TAG = "CRAZY" + CrazyCallActivity.class.getSimpleName();

    private String currentNumber;
    private TextView countDownText;
    private AtomicInteger countDownNumber = new AtomicInteger(5);

    public static volatile boolean canStart = true;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    int i = countDownNumber.decrementAndGet();
                    countDownText.setText(getString(R.string.count_down, i));
                    if (i == 0) {
                        if (!TextUtils.isEmpty(currentNumber)) {
                            callPhone(currentNumber);
                            handler.removeCallbacksAndMessages(null);
                            CrazyCallActivity.this.finish();
                        }
                    } else {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
                default:
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_call);
        Log.d(TAG, "!!!!!!onCreate!!!!!!!");
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                currentNumber = extras.getString("currentNumber");
                ((TextView)findViewById(R.id.current_number)).setText(currentNumber);
                countDownText = findViewById(R.id.count_down);
                countDownNumber.set(5);
                countDownText.setText(getString(R.string.count_down, countDownNumber.get()));
                handler.sendEmptyMessageDelayed(0, 1000);
                ((Button)findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.removeCallbacksAndMessages(null);
                        Intent callService = new Intent(CrazyCallActivity.this, CrazyCallService.class);
                        CrazyCallActivity.this.stopService(callService);
                        Toast.makeText(CrazyCallActivity.this, "如有需要，请重新开启app", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            finish();
        }
    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "!!!!!!!!onDestroy!!!!!!!!!");
        canStart = true;
    }
}
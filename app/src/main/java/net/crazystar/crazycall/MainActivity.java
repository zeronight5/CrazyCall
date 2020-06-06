package net.crazystar.crazycall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

/**
 * @author jay
 */
public class MainActivity extends AppCompatActivity {

    private final RxPermissions rxPermissions = new RxPermissions(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Disposable subscribe = rxPermissions.request(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.PROCESS_OUTGOING_CALLS).subscribe(granted -> {
            if (granted) {
                Toast.makeText(MainActivity.this, R.string.crazy, Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        });
        EditText phoneNumber = findViewById(R.id.phone_number);
        Button call = findViewById(R.id.call);
        call.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(phoneNumber.getText())) {
                callPhone(phoneNumber.getText().toString());
                finish();
            } else {
                Toast.makeText(MainActivity.this, R.string.tip_input_phone_number, Toast.LENGTH_SHORT).show();
            }
        });

        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(v -> {
            Intent callService = new Intent(MainActivity.this, CrazyCallService.class);
            MainActivity.this.stopService(callService);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent callService = new Intent(MainActivity.this, CrazyCallService.class);
        this.startService(callService);
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

}
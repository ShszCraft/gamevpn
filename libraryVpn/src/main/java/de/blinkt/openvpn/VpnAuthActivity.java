package de.blinkt.openvpn;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by huangyifei on 16/11/15.
 */
public class VpnAuthActivity extends AppCompatActivity {
    public static final String KEY_CONFIG = "config";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    private String mConfig;
    private String mUsername;
    private String mPw;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfig = getIntent().getStringExtra(KEY_CONFIG);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mPw = getIntent().getStringExtra(KEY_PASSWORD);
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            startVpn();
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startVpn();
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void startVpn() {
        try {
            OpenVpnApi.startVpnInternal(this, mConfig, mUsername, mPw);
        } catch (RemoteException ignore) {
        }
    }
}

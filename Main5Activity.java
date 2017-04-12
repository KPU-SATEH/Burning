package com.dteviot.epubviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main5Activity extends Activity implements View.OnClickListener {

    Button btn_connect;
    TextView textView;

    private static final String TAG = "Main";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothService btService = null;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        textView = (TextView) findViewById(R.id.textView);

        Intent in = getIntent();
        String str = in.getExtras().getString("ImgFile");
        try {
            //textView.setText(str);
            Log.d("TEXT", "hahahaha");
            System.out.print("메인 5 에 있다 ");
        } catch (Exception e) {
        }

        btn_connect.setOnClickListener(this);

        // BluetoothService클래스 생성
        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

    }

    @Override
    public void onClick(View v) {
        if(btService.getDeviceState()) {
            // 블루투스가 지원 가능한 기기일 때
            btService.enableBluetooth();
        } else {
            finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT: // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // "확인"을 눌렀을 때
                    //btService.getDeviceState(data);
                }
                else {
                    // "취소"를 눌렀을 때
                    Log.d("TAG", "Bluetooth is not enabled");
                }
                break;
        }
    }
}

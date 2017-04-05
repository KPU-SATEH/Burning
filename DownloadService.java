package com.example.namju.njhj;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by namju on 2017-03-25.
 */

class DownloadService extends IntentService {
    int result = Activity.RESULT_CANCELED;

    public DownloadService() {
        super("DownloadService");
    }

    // 이 메소드에서, 웹에서 파일을 다운로드 함
    @Override
    protected void onHandleIntent(Intent intent) {
        Uri data = intent.getData();
        String urlPath = intent.getStringExtra("urlpath"); // urlpath !!!!!!!!!!!!!!!!!!!!!!!!!1
        String buffer = " ";

        InputStream stream = null;

        try {
            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            int i = 0;
            int next = -1;
            while ((next = reader.read()) != -1) {
                buffer += " " + (char)next;
                if (++i > 100) break;
            }
            result = Activity.RESULT_OK;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Messenger messenger = (Messenger) extras.get("MESSENGER");
            Message msg = Message.obtain();
            msg.arg1 = result;
            msg.obj = buffer;
            try {
                messenger.send(msg);
            } catch (android.os.RemoteException e) {
                Log.w(getClass().getName(), "Exception sending message", e);
            }
        }
    }
}

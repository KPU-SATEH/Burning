package com.example.namju.njhj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    TextView textView1; // 전자책 제목
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        textView1 = (TextView) findViewById(R.id.textView1);

        // Intent intent = getIntent(); // Main2Activity에서 리스트 목록에서 intent로 전자책 제목을 주면, 여기서 getIntent()로 받아서,
        // s = intent.getExtras().getString("KEY");
        s = "개미.epub";

        /*
        // http://computer.kevincrack.com/download.jsp?name=개미.epub을 인터넷에 쳐보면 개미.epub이 다운로드 받아짐!
        Intent i = new Intent(Intent.ACTION_VIEW); // Server
        Uri u = Uri.parse("http://computer.kevincrack.com/download.jsp?name=" + s);
        i.setData(u);
        startActivity(i);
        */
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Object path = message.obj;
            if (message.arg1 == RESULT_OK && path != null) {
                Toast.makeText(getApplicationContext(), "" + path.toString() + "을 다운로드하였음.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_LONG).show();
            }
        } ;
    };

    public void onClick(View view) {
        Intent intent = new Intent(this, DownloadService.class);
        Messenger messenger = new Messenger(handler);
        intent.putExtra("MESSENGER", messenger);
        // intent.setData(Uri.parse("http://computer.kevincrack.com/download.jsp?name=" + s));
        intent.setData(Uri.parse("http://www.naver.com/"));
        // intent.putExtra("urlpath", "http://computer.kevincrack.com/download.jsp?name=" + s);
        intent.putExtra("urlpath", "http://www.naver.com/");
        startService(intent);
    }
}

package com.example.namju.njhj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    long mStartTime, mEndTime;
    EpubXMLParser mXMLParser;

    Button aBtn;
    Button bBtn;
    Button cBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        aBtn = (Button) findViewById(R.id.aBtn);
        bBtn = (Button) findViewById(R.id.bBtn);
        cBtn = (Button) findViewById(R.id.cBtn);

        aBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                intent.putExtra("epubBook", aBtn.getText().toString());
                startActivity(intent);
            }
        });
        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                intent.putExtra("epubBook", bBtn.getText().toString());
                startActivity(intent);
            }
        });
        cBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                intent.putExtra("epubBook", cBtn.getText().toString());
                startActivity(intent);
            }
        });

                mStartTime = System.currentTimeMillis();
                mXMLParser = new EpubXMLParser("http://computer.kevincrack.com/epub_download.jsp", mHandler);
                Thread thread = new Thread(mXMLParser);
                thread.start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mEndTime = System.currentTimeMillis();
            Log.d("Taken Time", Long.toString((mEndTime - mStartTime) / 1000L));

            ArrayList<EpubDatas> dataList = mXMLParser.getResult();
            int dataListSize = dataList.size();
            Log.d("Data List Size", Integer.toString(dataListSize));
            for (int i=0; i<dataListSize; i++) {
                Log.d("XML Parsing Result", dataList.get(i).getEpub());
            }
            //aBtn.setText(dataList.get(0).getEpub());
            //bBtn.setText(dataList.get(1).getEpub());
            //cBtn.setText(dataList.get(2).getEpub());
        }
    };
}
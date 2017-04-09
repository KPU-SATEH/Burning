package com.dteviot.epubviewer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.dteviot.epubviewer.ListEpubActivity.FILENAME_EXTRA;
import static com.dteviot.epubviewer.ListEpubActivity.PAGE_EXTRA;

public class Main3Activity extends Activity implements TextToSpeech.OnInitListener {
    public static final String FILENAME_EXTRA = "FILENAME_EXTRA";
    public static final String PAGE_EXTRA = "PAGE_EXTRA";

    private TextToSpeech mTTS;
    static List<File> epubs;
    static List<String> names;
    ArrayAdapter<String> adapter;
    static File selected;
    Button refreshbtn;

    String File_Name = "";
    String File_extend = "";
    String Save_Path;
    String Save_folder = "/Epub";
    String bookName;
    private String mRootPath;


    //DownloadThread dThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("여기다 종훈아1");
        setContentView(R.layout.activity_main2);
        System.out.println("여기다 종훈아3");
        mTTS = new TextToSpeech(getApplicationContext(), this);
        Intent intent_book = getIntent();

        bookName = intent_book.getExtras().getString("BOOKNAME"); // 2Activity에서 책이름을 받아옴

        String encodeResult = null;
        try {
            encodeResult = URLEncoder.encode(bookName, "utf-8");
         } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
         }
        Intent i = new Intent(Intent.ACTION_VIEW); // Server
        Uri u = Uri.parse("http://computer.kevincrack.com/download.jsp?name=" + encodeResult);
        i.setData(u);
        startActivity(i);

        /*다운로드 경로를 */
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)){
            Save_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + Save_folder;
        }

        if ((epubs == null) || (epubs.size() == 0)) {
            epubs = epubList(Environment.getExternalStorageDirectory());
        }

        final ListView list = (ListView)findViewById(R.id.booklist);
        names = fileNames(epubs);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("here","here");
                /*잠시 주석처리
                Intent in = new Intent(Main3Activity.this, Main4Activity.class);
                in.putExtra("OPENBOOK", names.get(position));
                startActivity(in);*/


                String fileName = titleToFileName(((TextView)view).getText().toString());
                System.out.println("실험1" + fileName);
                Log.d("here","here3");
               // Intent intent = new Intent(Main3Activity.this, EpubWebView.class);
                Intent intent = new Intent();
                intent.putExtra(FILENAME_EXTRA, fileName);
                // set page to first, because ListChaptersActivity returns page to start at
                intent.putExtra(PAGE_EXTRA, 0);
                setResult(RESULT_OK, intent);
                System.out.println("화면의 아무곳이나 터치해");
                finish();
                System.out.println("화면의 아무곳이나 터치해3");
                Log.d("here","here4"+fileName);



            }
        });
        list.setAdapter(adapter);

        refreshbtn = (Button)findViewById(R.id.refreshlist);
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });


    }

    private String titleToFileName(String title) {
        File path = Environment
                .getExternalStorageDirectory();
        mRootPath = path.toString();

        return mRootPath + "/Download/" + title+".epub";
       // return title;
    }

    private List<String> fileNames(List<File> files){
        List<String> res = new ArrayList<String>();
        for (int i = 0; i<files.size(); i++){
            res.add(files.get(i).getName().replace(".epub",""));
        }
        return res;
    }

    private List<File> epubList(File dir) {
        List<File> res = new ArrayList<File>();

        if(dir.isDirectory()){
            File[] f = dir.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    if (f[i].isDirectory()) {
                        res.addAll(epubList(f[i]));
                    } else {
                        //String lowerCasedName = f[i].getName().toLowerCase();
                        //System.out.println("태스트13 "+ f[i]);
                        String aa =  f[i].toString();
                       // System.out.println("태스트14 "+ lowerCasedName);
                        if ( aa. startsWith("/storage/emulated/0/Download/")) {
                            if (aa.endsWith(".epub")) {
                                res.add(f[i]);
                                System.out.println("파이리 : " + aa);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    private void refreshList(){
        epubs = epubList(Environment.getExternalStorageDirectory());
        names.clear();
        names.addAll(fileNames(epubs));
        this.adapter.notifyDataSetChanged();
    }


    public void onInit(int i){
        mTTS.speak("여기는 어떻게 처리를 할까요", TextToSpeech.QUEUE_FLUSH, null);
    }



}


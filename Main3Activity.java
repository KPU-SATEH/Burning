package com.example.hyejin.njhj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity implements TextToSpeech.OnInitListener {

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

    //DownloadThread dThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mTTS = new TextToSpeech(getApplicationContext(), this);

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
                selected = epubs.get(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("bpath", selected.getAbsolutePath());
                setResult(Activity.RESULT_OK, resultIntent);

                // Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_LONG).show(); // 값 체크 및 수정용 필요시 사용할 예정.

                /*                 전자책 다운로드 로직                  */
                String temp = list.getItemAtPosition(position).toString()+".epub"; // 파일 이름
                String origin ="http://computer.kevincrack.com/download.jsp?name=";
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(origin+temp);
                i.setData(u);
                startActivity(i);


                /*              전자책 다운로드 로직 종료          */

                /*
               final String fileURL = origin+temp;
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
               final DownloadFilesTask downloadTask = new DownloadFilesTask(Main2Activity.this);
                downloadTask.execute(fileURL);
                */
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

/*
        aBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        i=0;
                    }
                };
                if(i==1){
                    handler.postDelayed(r, 300);
                    String sld = aBtn.getText().toString();
                    mTTS.speak(sld, TextToSpeech.QUEUE_FLUSH, null);

                }else if(i==2){
                    i=0;
                    Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                    startActivity(intent);
                }
            }
        });
*/
    }
    /*
        private class DownloadFilesTask extends AsyncTask<String, String, Long> {
            private Context context;



            public DownloadFilesTask(Context context) {
                this.context = context;
            }

            protected Long doInBackground(String... string_url) {
                long FileSize = -1;
                InputStream input = null;
                OutputStream output = null;
                URLConnection connection = null;

                try {
                    URL url = new URL(string_url[0]);
                    connection = url.openConnection();
                    connection.connect();

                    FileSize = connection.getContentLength();

                    input = new BufferedInputStream(url.openStream(), 8192);
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    outputFile = new File(path, "hi");
                    output = new FileOutputStream(outputFile);

                    output.flush();
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error:", e.getMessage());
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }
                }
                return FileSize;

            }
        }
    */
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
                        String lowerCasedName = f[i].getName().toLowerCase();
                        if (lowerCasedName.endsWith(".epub")) {
                            res.add(f[i]);

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
        mTTS.speak("전자책 목록 화면입니다. 원하는 전자책을 선택해주세요.", TextToSpeech.QUEUE_FLUSH, null);
    }
/*
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            String sld = cBtn.getText().toString();
            mTTS.speak(sld, TextToSpeech.QUEUE_FLUSH, null);
        }
        return true;
    }*/
}

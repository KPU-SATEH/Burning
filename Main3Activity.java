package com.example.namju.njhj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class Main3Activity extends AppCompatActivity {

    TextView textView1; // 전자책 제목
    String epubName;
    String encodeResult;
    WebView webView;

    LayoutInflater inflater;
    List<RowData> contentDetails;
    public static final String BOOK_NAME = "books/novel.epub"; // 변경 !!!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        textView1 = (TextView) findViewById(R.id.textView1);

        Intent intent = getIntent();
        epubName = intent.getExtras().getString("EPUBNAME");
        textView1.setText(epubName);

        /*
        encodeResult = null;
        try {
            encodeResult = URLEncoder.encode(epubName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_VIEW); // Server
        Uri u = Uri.parse("http://computer.kevincrack.com/download.jsp?name=" + encodeResult);
        i.setData(u);
        startActivity(i);
        */

        inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        contentDetails = new ArrayList<RowData>();
        AssetManager assetManager = getAssets();

        try {
            InputStream epubInputStream = assetManager.open(BOOK_NAME);
            Book book = (new EpubReader()).readEpub(epubInputStream);
            // logContentsTable(book.getTableOfContents().getTocReferences(), 0);
        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
        }
        CustomAdapter adapter = new CustomAdapter(this, R.layout.list, R.id.title, contentDetails);
        setListAdapter(adapter);
        getListView().setTextFilterEnabled(true);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Object path = message.obj;
            if (message.arg1 == RESULT_OK && path != null) {
                //Toast.makeText(getApplicationContext(), "" + path.toString() + "을 다운로드하였음.", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "다운로드하였음.", Toast.LENGTH_LONG).show();
/*
                EpubReader epubReader = new EpubReader();
                Book book = null;
                try {
                    book = epubReader.readEpub(new FileInputStream("개미.epub")); // 여기에 경로가 필요할 듯!
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<String> titles = book.getMetadata().getTitles();
                System.out.println("book title:" + (titles.isEmpty() ? "book has no title" : titles.get(0)));
*/

            }
            else {
                Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_LONG).show();
            }
        } ;
    };

    public void onClick(View view) {
        encodeResult = null;
        try {
            encodeResult = URLEncoder.encode(epubName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_VIEW); // Server
        Uri u = Uri.parse("http://computer.kevincrack.com/download.jsp?name=" + encodeResult);
        i.setData(u);
        startActivity(i);

        // 디바이스에 Download파일안에 다운받아짐
        /* EPUB OPEN */
        webView = (WebView) findViewById(R.id.webView); // ??




















/*
        EpubReader epubReader = new EpubReader();
        Book book = null;
        try {
            book = epubReader.readEpub(new FileInputStream(epubName)); // 여기에 경로가 필요할 듯!
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> titles = book.getMetadata().getTitles();
        //System.out.println("book title:" + (titles.isEmpty() ? "book has no title" : titles.get(0)));
        Log.d("BookTITLE", titles.get(0));
*/

        /*
        encodeResult = null;
        try {
            encodeResult = URLEncoder.encode(epubName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String urlResult = "http://computer.kevincrack.com/download.jsp?name=" + encodeResult;

        Intent intent = new Intent(this, MyIntentService.class);
        Messenger messenger = new Messenger(handler);
        intent.putExtra("MESSENGER", messenger);
        //intent.setData(Uri.parse("http://computer.kevincrack.com/download.jsp"));
        intent.setData(Uri.parse(urlResult));
        //intent.putExtra("urlpath", "http://computer.kevincrack.com/download.jsp");
        intent.putExtra("urlpath", urlResult);
        startService(intent);
        */
    }
}
/*
class Book {
    private ZipFile mZip;

    public Book(String fileName) {
        try {
            mZip = new ZipFile(fileName);
        } catch (IOException e) {
            //Log.e(Globals.TAG, "Error opening file", e);
        }
    }

    public InputStream fetchFromZip(String fileName) {
        InputStream in = null;
        ZipEntry containerEntry = mZip.getEntry(fileName);
        if (containerEntry != null) {
            try {
                in = mZip.getInputStream(containerEntry);
            } catch (IOException e) {
                //Log.e(Globals.TAG, "Error reading zip file " + fileName, e);
            }
        }
        return in;
    }
}
*/














private class CustomAdapter extends ArrayAdapter<RowData> {
    public CustomAdapter(Context context, int resource, int textViewResourceId, List<RowData> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    private class ViewHolder {
        private View row;
        private TextView titleHolder = null;
        public ViewHolder(View row) {
            super();
            this.row = row;
        }
        public TextView getTitle() {
            if (null == titleHolder)
                titleHolder = (TextView) row.findViewById(R.id.title);
            return titleHolder;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TextView title = null;
        RowData rowData = getItem(position);
        if (null == convertView) {
           convertView = inflater.inflate(R.layout.list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        title = holder.getTitle();
        title.setText(rowData.getTitle());
        return convertView;
    }
}
    private void logContentsTable(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null)
            return;
        for (TOCReference tocReference:tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            RowData row = new RowData();
            row.setTitle(tocString.toString());
            row.setResource(tocReference.getResource());
            contentDetails.add(row);
            logContentsTable(tocReference.getChildren(), depth + 1);
        }
    }

private class RowData {
    private String title;
    private Resource resource;

    public RowData() {
        super();
    }

    public String getTitle() { return title; }
    public Resource getResource() { return resource;}
    public void setTitle(String title) { this.title = title;}
    public void setResource(Resource resource) { this.resource = resource;}
}


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        RowData rowData = contentDetails.get(position);
        Intent intent = new Intent(MicroEpubReaderActivity.this, ContentViewActivity.class);
        intent.putExtra("display", new String(rowData.getResource().getData()));
        startActivity(intent);

    }

}










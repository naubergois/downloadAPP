package br.nauber.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button parseButton;
    ListView listView;
    private String myFileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        parseButton=(Button) findViewById(R.id.btnParse);
        listView=(ListView) findViewById(R.id.listView);

        parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseAplications parseAplications=new ParseAplications(myFileContents);
                parseAplications.process();
                ArrayAdapter<Application> arrayAdapter=new ArrayAdapter<Application>(MainActivity.this,R.layout.list_item,parseAplications.getAplcations());
                listView.setAdapter(arrayAdapter);

            }
        });



        DownloadData downloadData=new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... params) {
            myFileContents=downLoadXMLFile(params[0]);
            if(myFileContents==null){
                Log.d("Download Data","Erro Downloading");
            }
            return myFileContents;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DownloadData", "Result was " + s);

        }

        private String downLoadXMLFile(String urlPath){
            StringBuffer tempBuffer=new StringBuffer();

            try{
                URL url=new URL(urlPath);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                int response=urlConnection.getResponseCode();
                Log.d("DownloadData","The response code was "+response);
                InputStream is=urlConnection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);


                int charRead;
                char[] inputBuffer=new char[500];
                while(true){
                    charRead=isr.read(inputBuffer);
                    if(charRead<=0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer,0,charRead));
                }

                return  tempBuffer.toString();

            }catch (IOException e){
                Log.d("DownloadData","IO Exception "+e.getMessage());
            }catch (SecurityException e){
                Log.d("DownloadData","Security Exception :"+e.getMessage());
            }

            return null;

        }
    }
}

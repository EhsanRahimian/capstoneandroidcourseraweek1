package com.nicootech.capstoneandroidcourserafirstweek;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button searchBtn;
    private EditText search_text;
    private RecyclerView recyclerView;
    private TextView results;


    private ArrayList<Album> albums;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = (Button) findViewById(R.id.btn_search);
        search_text = (EditText) findViewById(R.id.search_input);
        searchBtn.setOnClickListener(this);

        results = (TextView) findViewById(R.id.results);
        recyclerView = (RecyclerView) findViewById(R.id.album_list);


        ////
        LinearLayoutManager manager = new LinearLayoutManager(this);
        AlbumAdapter adapter = new AlbumAdapter(this,albums);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter( adapter );

        ////












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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                if (!TextUtils.isEmpty(search_text.getText().toString())) {
                    String searchTerm = search_text.getText().toString();
                    searchByAlbumName(searchTerm);

                } else {
                    Toast.makeText(this, "Please input a search term", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void searchByAlbumName(String searchTerm) {
        if (getConnectionStatus()) {
            new MyAsyncTask().execute(getSptifyUrl(searchTerm));
        } else {
            // prompt user for connection.
        }

    }

    private String getSptifyUrl(String searchTerm) {
        StringBuilder sb = new StringBuilder("https://api.spotify.com/v1/search?q=");
        sb.append(searchTerm.trim()).append("&type=album");
        return sb.toString();
    }

    private boolean getConnectionStatus() {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            connected = true;
        }
        return connected;
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        private String search_term;

        @Override
        protected Boolean doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection;
            JSONObject jsonObject = null;

            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    String response = readStream(urlConnection.getInputStream());
                    try {
                        jsonObject = new JSONObject(response);
                        albums = Album.parse(jsonObject);
                        Log.i("ehsan", "album size: " + albums.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (albums!= null) {
                for (Album a : albums) {
                    Log.i("ehsan", "album name: " + a.getAlbum_name() );
                }
            }
            return albums != null;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            // Hide keyboard
            hideKeyboard();
            if (success) {
                results.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                AlbumAdapter adapter = new AlbumAdapter(MainActivity.this, albums);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
    public void hideKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

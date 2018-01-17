package com.ms.henrique.openair.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.henrique.openair.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.search_button);
        final Spinner spNumber = (Spinner) findViewById(R.id.spinnerNumber);

        List<String> list = new ArrayList<String>();
        for(int i = 1; i <= 88; i++) {
            String item = String.valueOf(i);
            list.add(item);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spNumber.setAdapter(dataAdapter);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratePerson(String.valueOf(spNumber.getSelectedItem()));
            }
        });
    }

    //
    public void GeneratePerson(String N) {
        String requestUrl = "https://swapi.co/api/people/"+N+"/?format=json";


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("getProfileInfo", "FAIL");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final TextView nomeUser = (TextView) findViewById(R.id.user_name);
                final TextView HomeWo = (TextView) findViewById(R.id.user_state);
                final TextView BirthY = (TextView) findViewById(R.id.nick_name);
                final TextView filmss = (TextView) findViewById(R.id.password);


                final String jsonData = response.body().string();
                Log.i("getProfileInfo", jsonData);
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CardView userCard = (CardView) findViewById(R.id.card_user_info);
                            try {
                                JSONObject rootObj = new JSONObject(jsonData);


                                if (rootObj != null) {

                                    nomeUser.setText(getText(R.string.user_nametxt)+": "+ rootObj.get("name").toString());
                                    HomeWo.setText(getText(R.string.locationtxt)+": "+rootObj.get("homeworld").toString());
                                    BirthY.setText(getText(R.string.brr_ntxt)+": "+rootObj.get("birth_year").toString());
                                    filmss.setText(getText(R.string.films)+": "+rootObj.get("films").toString());

                                    userCard.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                 userCard.setVisibility(View.GONE);
                                Log.e("bad", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
        //
    }
}

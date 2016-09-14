package com.qube.qube.project4.main.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qube.qube.project4.R;
import com.qube.qube.project4.main.gson.MarvelCharacter;
import com.qube.qube.project4.main.gson.MarvelSearchResult;
import com.qube.qube.project4.main.mainmenu.MainMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView_photo, mImageView_backspace;
    private TextView mTextView_category;
    private CardView mCardView_next_level;
    private RecyclerView mRecyclerView_answer, mRecyclerView_user_choice;
    private AnswerRecyclerViewAdapter mAdapter_answer;
    private UserChoiceRecyclerViewAdapter mAdapter_user_choice;
    private LinearLayoutManager mLinearLayoutManager_answer;
    private GridLayoutManager mGridLayoutManager_user_choice;
    private Toolbar mToolbar;
    private ArrayList<MarvelCharacter> mCharacters;
    private int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //403 stands for the X-Men series in the api call
        String query = "403";
        MarvelAPI task = new MarvelAPI();
        task.execute(query);

        mCharacters = new ArrayList<>();

        //Toolbar set up
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Answer RecyclerView set up
        mRecyclerView_answer = (RecyclerView) findViewById(R.id.rv_main_answer);
        mLinearLayoutManager_answer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_answer.setLayoutManager(mLinearLayoutManager_answer);
        mAdapter_answer = new AnswerRecyclerViewAdapter(this, "");
        mRecyclerView_answer.setAdapter(mAdapter_answer);

        //UserChoice RecyclerView set up
        mRecyclerView_user_choice = (RecyclerView) findViewById(R.id.rv_main_user_choice);
        mGridLayoutManager_user_choice = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        mRecyclerView_user_choice.setLayoutManager(mGridLayoutManager_user_choice);
        mAdapter_user_choice = new UserChoiceRecyclerViewAdapter(this, "",mAdapter_answer);
        mRecyclerView_user_choice.setAdapter(mAdapter_user_choice);

        //This will bring you back to MainMenu
        mImageView_backspace = (ImageView) findViewById(R.id.iv_backspace);
        mImageView_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainMenu.class));
            }
        });

        //This CardView will bring you to the next level of the game
        mCardView_next_level = (CardView) findViewById(R.id.cv_next_level);
        mCardView_next_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will increment the level
                level += 1;
                /*This keeps the game in check when loading the next level by checking if we've reached
                the end of the game*/
                if (level < mCharacters.size()) {
                    mAdapter_answer.mUserInput.clear();
                    mAdapter_answer.answerUpdateName(mCharacters.get(level).getName());
                    mAdapter_user_choice.userUpdateName(mCharacters.get(level).getName());
                    mImageView_photo.setImageResource(MainActivity.this.getResources().getIdentifier(mCharacters.get(level).getName().toLowerCase().replaceAll(" ", ""), "drawable", MainActivity.this.getPackageName()));
                } else {
                    Toast.makeText(MainActivity.this, "You've reached the end of the game. CONGRATS!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class MarvelAPI extends AsyncTask<String, Void, MarvelSearchResult> {
        @Override
        protected MarvelSearchResult doInBackground(String... params) {
            String name;
            try {
                URL url = new URL(getUrlString(params[0]));
                Log.d("MainActivity", "doInBackground: url = " + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                name = getInputData(inputStream);

                Gson gson = new Gson();

                return gson.fromJson(name, MarvelSearchResult.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MarvelSearchResult marvelSearchResult) {
            super.onPostExecute(marvelSearchResult);

            /*take marvelSearchResult, find the first person in it, put the name in mCharacters,
            then tell the mAdapter you updated the data by calling notifyDataSetChanged()*/
            if (marvelSearchResult != null) {
                mCharacters.clear();
                mCharacters.addAll(marvelSearchResult.getData().getResults());
                //This shuffles the Arraylist of MarvelCharacter and scrambles the order of the levels
                Collections.shuffle(mCharacters);
                mAdapter_answer.answerUpdateName(mCharacters.get(level).getName());
                mAdapter_user_choice.userUpdateName(mCharacters.get(level).getName());

                mImageView_photo = (ImageView) findViewById(R.id.iv_hero);
                mImageView_photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mImageView_photo.setImageResource(MainActivity.this.getResources().getIdentifier(mCharacters.get(level).getName().toLowerCase().replaceAll(" ", ""), "drawable", MainActivity.this.getPackageName()));
            }

        }

        private String getInputData(InputStream inStream) throws IOException {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

            String character;

            while ((character = reader.readLine()) != null) {
                builder.append(character);
            }

            return builder.toString();
        }

        private String getUrlString(String query)
                throws UnsupportedEncodingException, NoSuchAlgorithmException {
            final String URL = "http://gateway.marvel.com/v1/public/characters?series=%s&ts=%d&limit=%d&apikey=%s&hash=%s";

            query = URLEncoder.encode(query, "UTF-8");
            long ts = System.currentTimeMillis();
            long limit = 31;
            String publicKey = getString(R.string.public_api_key);
            String privateKey = getString(R.string.private_api_key);

            return String.format(URL, query, ts, limit, publicKey, getMd5(ts, privateKey, publicKey));
        }

        private String getMd5(long timeStamp, String privateKey, String publicKey)
                throws UnsupportedEncodingException, NoSuchAlgorithmException {

            String inputString = timeStamp + privateKey + publicKey;
            byte[] inputBytes = inputString.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(inputBytes);

            // see http://stackoverflow.com/a/15485672
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String conversion = Integer.toString(b & 0xFF, 16);
                while (conversion.length() < 2) {
                    conversion = "0" + conversion;
                }
                sb.append(conversion);
            }
            return sb.toString();
        }

    }
}

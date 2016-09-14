package com.qube.qube.project4.main.mainmenu;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qube.qube.project4.R;
import com.qube.qube.project4.main.main.MainActivity;

import java.io.IOException;

public class MainMenu extends AppCompatActivity {
    private CardView mCardViewPlay;
    private ImageView mImageViewLogo, mImageViewChocobo, mImageViewBabyChocobo, mImageViewDino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //When you click the X-Men Logo my custom easter egg plays a sound
        mImageViewLogo = (ImageView) findViewById(R.id.iv_xMenLogo);
        mImageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mImageViewDino = (ImageView) findViewById(R.id.iv_dinosaur);
                mImageViewDino.setImageResource(MainMenu.this.getResources().getIdentifier("trex", "drawable", MainMenu.this.getPackageName()));

                mImageViewChocobo = (ImageView) findViewById(R.id.iv_chocobo);
                mImageViewChocobo.setImageResource(MainMenu.this.getResources().getIdentifier("ic_chocobo", "drawable", MainMenu.this.getPackageName()));

                Toast.makeText(MainMenu.this, "A Chocobo Appears...! In MarveL?", Toast.LENGTH_SHORT).show();


                mImageViewChocobo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mImageViewBabyChocobo = (ImageView) findViewById(R.id.iv_baby_chocobo);
                        mImageViewBabyChocobo.setImageResource(MainMenu.this.getResources().getIdentifier("ic_chocobo", "drawable", MainMenu.this.getPackageName()));

                        Toast.makeText(MainMenu.this, "Awww... A Baby Chocobo!", Toast.LENGTH_SHORT).show();

                        final MediaPlayer mp = new MediaPlayer();

                        if (mp.isPlaying()) {
                            mp.stop();
                        }
                        try {
                            mp.reset();
                            AssetFileDescriptor afd;
                            afd = getApplicationContext().getAssets().openFd("ChocoboCall.mp3");
                            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });


        mCardViewPlay = (CardView) findViewById(R.id.cv_play);
        mCardViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, MainActivity.class));
            }
        });
    }
}

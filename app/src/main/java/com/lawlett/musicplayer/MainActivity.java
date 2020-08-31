package com.lawlett.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    List<Recipe> mListRecipe=new ArrayList<>();
    String lastPage;
    String image;

private final String urlHome = "http://hotcharts.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.title_img);
//        new ParsImage().execute();
        new ParsMusic().execute();

    }


    public class ParsImage extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(urlHome).get();
                Elements els = doc.select("div[class=b_nav_stations  clearfix]");
                     image="http://hotcharts.ru/"+els.select("li[class=hidden-phone]>a >img").attr("src");
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             Glide.with(MainActivity.this).load(image).into(imgView);
                         }
                     });

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
    public class ParsMusic extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc= Jsoup.connect(urlHome).get();
                final Elements els = doc.select("div[class=jp-jplayer]");
                final String uri =els.select("audio").attr("");
                Log.e("music", "doInBackground: "+uri );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.setDataSource(uri);
                            mp.prepare();
                            mp.start();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }


    private void itemRecipes(String url) {
        try {
            String imgRecipe, nameRecipe, lingPageRecipe;
            Document doc = Jsoup.connect(url).get();
            Elements els = doc.select("div[class=items] >div[class=row] >div[class=col span_6 tourItem]");
            for (Element el : els) {
                imgRecipe = "http://wowbody.com.ua" + el.select("<img").attr("src");
                nameRecipe = el.select("div[class=title] > a").text();
                lingPageRecipe = el.select("div[class=title] > a").attr("href");

                Log.e("recipe", "itemRecipes: "+imgRecipe+" "+nameRecipe+" "+lingPageRecipe );

                mListRecipe.add(new Recipe(imgRecipe,nameRecipe,lingPageRecipe));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
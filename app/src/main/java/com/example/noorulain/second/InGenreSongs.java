package com.example.noorulain.second;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Noorulain on 23-01-2016.
 */
public class InGenreSongs extends Fragment {

   // private int position;
   // private ArrayList<Song> genresList;
    private ListView listview;

    ArrayList<Song> genresList;

    private long genreId;
    private String genName;

    private TextView tv1;
    private TextView tv2;
    String trialid;




    public InGenreSongs() {

    }

    public static InGenreSongs newInstance() {
        InGenreSongs fragment = new InGenreSongs();
        return fragment;

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        Toast.makeText(getContext(), "from Ingenresongs Fragment", Toast.LENGTH_SHORT).show();
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);

        genName = getArguments().getString("GenreName");

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_one);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(genName);



        tv1 = (TextView) view.findViewById(R.id.genre_song_name);
        tv2 = (TextView) view.findViewById(R.id.genre_song_artist);


        genreId = getArguments().getLong("GenreId");


        genresList = new ArrayList<Song>();


        getGenreList(genreId);


        Adapterr_genre customAdapter = new Adapterr_genre(getContext(), genresList);
        listview.setAdapter(customAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("P", position);
                i.putExtra("list", genresList);
                i.putExtra("f", true);
                startActivity(i);*/


            }
        });




        return view;
    }


    public void getGenreList(long genreId) {


        //retrieve song info
        ContentResolver musicResolver = getContext().getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
       // Uri genreUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        //for no of songs in a genre
        //String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);
        Cursor musicCursor = musicResolver.query(uri, null, null, null, null);



        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns


            int genresongColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Media.DISPLAY_NAME);
            int genresongartistColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Media.ARTIST);
            int trial = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Genres.Members.AUDIO_ID);
            do {
                //add songs to list
//for no of songs in a genre
                String genresongtitle = musicCursor.getString(genresongColumn);
                String genresongrtist = musicCursor.getString(genresongartistColumn);
                 trialid = musicCursor.getString(trial);

               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                genresList.add(new Song(genreId, null, -1,genresongtitle,genresongrtist));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }



}



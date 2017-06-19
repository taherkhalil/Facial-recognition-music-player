package com.example.noorulain.second;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Noorulain on 24-01-2016.
 */
public class InAlbumSongs extends Fragment {

    private ListView listview;
    ArrayList<Song> albumsList;
    private long albumId;
    private String albName;

    private TextView tv1;
    private TextView tv2;


    public InAlbumSongs() {

    }

    public static InAlbumSongs newInstance() {
        InAlbumSongs fragment = new InAlbumSongs();
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        Toast.makeText(getContext(), "from Inalbumsongs Fragment", Toast.LENGTH_SHORT).show();
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);

        albName = getArguments().getString("AlbumName");
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_one);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(albName);


        tv1 = (TextView) view.findViewById(R.id.album_song_name);
        tv2 = (TextView) view.findViewById(R.id.album_song_artist);


        albumId = getArguments().getLong("AlbumId");


        albumsList = new ArrayList<Song>();

        getAlbumsList(albumId);



        Adapterr_album customAdapter = new Adapterr_album(getContext(), albumsList);
        listview.setAdapter(customAdapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        return view;
    }




    public void getAlbumsList(long albumId) {

        //retrieve song info
        ContentResolver musicResolver = getContext().getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Uri genreUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        //for no of songs in a genre
        String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};
        //String albid = String.valueOf(albumId);
      //  Uri uri = MediaStore.Audio.Albums.getContentUri("external",albumId); // getContentUri("external", genreId);
        Uri albumUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String here = MediaStore.Audio.Media.ALBUM_ID + "=" + albumId;

        Cursor musicCursor = musicResolver.query(albumUri, null, here, null, null);



        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns


            int albumsongColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Media.DISPLAY_NAME);
            int albumsongartistColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Media.ARTIST);


            do {
                //add songs to list
//for no of songs in a genre
                String albumsongtitle = musicCursor.getString(albumsongColumn);
                String albumsongrtist = musicCursor.getString(albumsongartistColumn);

               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                albumsList.add(new Song(albumId, null, null, albumsongtitle, albumsongrtist));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }




}

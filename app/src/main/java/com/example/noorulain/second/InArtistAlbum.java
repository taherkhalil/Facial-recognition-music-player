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
public class InArtistAlbum extends Fragment {

    private ListView listview;
    ArrayList<Song> artistsList;
    private long artistId;
    private String artistName;

    private TextView tv1;
    private TextView tv2;


    public InArtistAlbum() {

    }

    public static InArtistAlbum newInstance() {
        InArtistAlbum fragment = new InArtistAlbum();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);



        View view = inflater.inflate(R.layout.fragment_one, container, false);

        Toast.makeText(getContext(), "from Inalbumsongs Fragment", Toast.LENGTH_SHORT).show();
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);


        artistName = getArguments().getString("ArtistName");
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_one);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(artistName);


        tv1 = (TextView) view.findViewById(R.id.artist_album_name);
        tv2 = (TextView) view.findViewById(R.id.artist_album_nofsongs);

        artistId = getArguments().getLong("ArtistId");


        artistsList = new ArrayList<Song>();

        getArtistList(artistId);

        Adapterr_artist customAdapter = new Adapterr_artist(getContext(), artistsList);
        listview.setAdapter(customAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



        return view;
    }



    public void getArtistList(long artistId) {
        //retrieve song info
        ContentResolver musicResolver = getContext().getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri genreUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Uri genreUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Uri uri = MediaStore.Audio.Artists.Albums.getContentUri("external",artistId);

        String here = MediaStore.Audio.Media.ARTIST_ID + "=" +artistId;

        Cursor musicCursor = musicResolver.query(uri, null, null, null, null);

        //for no of songs in a genre
        // String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int artistalbumsColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Artists.Albums.ALBUM);

            int aralbumsongColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS);

           /* int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int keyColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST_KEY);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int countColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);*/

            do {



                //add songs to list

                String thistitle = musicCursor.getString(artistalbumsColumn);
                String thiscount = musicCursor.getString(aralbumsongColumn);

               /* long thisId = musicCursor.getLong(idColumn);
                String thisKey = musicCursor.getString(keyColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String count = musicCursor.getString(countColumn);*/


               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                artistsList.add(new Song(artistId,null, null, null,thistitle,thiscount));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }




}

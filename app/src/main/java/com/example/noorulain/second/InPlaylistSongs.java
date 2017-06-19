package com.example.noorulain.second;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
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
 * Created by Noorulain on 28-01-2016.
 */
public class InPlaylistSongs extends Fragment {

    private ListView listview;
    ArrayList<Song> playList;
    private long playlistId;
    private String playname;

    private TextView tv1;
    private TextView tv2;


    public InPlaylistSongs() {



    }

    public static InPlaylistSongs newInstance() {
        InPlaylistSongs fragment = new InPlaylistSongs();
        return fragment;

    }


    onPositionSelectedInPlaylist mCallback;
    onPositionSelectedInPlaylist4list mcallback;


    public interface onPositionSelectedInPlaylist {
        void OnPositionSelectedInPlaylist(int position);
    }

    public interface onPositionSelectedInPlaylist4list {
        void OnPositionSelectedInPlaylist4list(ArrayList<Song> list,long id);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onPositionSelectedInPlaylist) context;
            mcallback = (onPositionSelectedInPlaylist4list) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("---", "fragment outdetach");

    }

    @Override
    public void onDestroyView() {
        Log.i("---", "fragment out destroyed");
        super.onDestroyView();
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        Toast.makeText(getContext(), "frrrrrrrrrrrrommmmmmmm", Toast.LENGTH_SHORT).show();
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);

        playname = getArguments().getString("PlayName");

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_one);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(playname);


        tv1 = (TextView) view.findViewById(R.id.playlist_song_name);
        tv2 = (TextView) view.findViewById(R.id.playlist_song_artist);


        playlistId = getArguments().getLong("PlayId");




        playList = new ArrayList<Song>();

        getPlayList(playlistId);
        mcallback.OnPositionSelectedInPlaylist4list(playList, playlistId);


        Adapterr_playlist customAdapter = new Adapterr_playlist(getActivity(), playList);
        listview.setAdapter(customAdapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mCallback.OnPositionSelectedInPlaylist(position);



            }
        });


        return view;

    }




    public void getPlayList(long playlistId) {


        //retrieve song info
        ContentResolver musicResolver = getActivity().getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Uri genreUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        //for no of songs in a genre
        //String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};






        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        Cursor musicCursor = musicResolver.query(uri, null, null, null, null);

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns


            int playstsongColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.DISPLAY_NAME);
            int playstsongartistColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.ARTIST);
            int trial = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.AUDIO_ID);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);

            do {
                //add songs to list
//for no of songs in a genre
                String playsongtitle = musicCursor.getString(playstsongColumn);
                String playsongrtist = musicCursor.getString(playstsongartistColumn);
                String trialid = musicCursor.getString(trial);

                long id = musicCursor.getLong(idColumn);

                long albumId = musicCursor.getLong(musicCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_ID));


                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                playList.add(new Song(playlistId, null, -1, playsongtitle, playsongrtist, trialid, id,albumArtUri));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }




    }
/*
    public void PlaySongsFromAPlaylist(int playlistId){

        ContentResolver musicResolver = getActivity().getContentResolver();

        String[] ARG_STRING = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                android.provider.MediaStore.MediaColumns.DATA};


        Uri membersUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);

        Cursor songsWithingAPlayList = musicResolver.query(membersUri, ARG_STRING, null, null, null);

        int theSongIDIwantToPlay = 0; // PLAYING FROM THE FIRST SONG
        if(songsWithingAPlayList != null)
        {
            songsWithingAPlayList.moveToPosition(theSongIDIwantToPlay);
            String DataStream = songsWithingAPlayList.getString(4);
            PlayMusic(DataStream);
            songsWithingAPlayList.close();
        }
    }*/



}

package com.example.noorulain.second;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Noorulain on 27-01-2016.
 */
public class Playlist extends Fragment {


    private boolean happyDetected = false;
    private boolean surpriseDetected = false;



    private String nameOfPlaylist; // created after face detection

    private long thisplaylistid = -1;

    private boolean exists = false;

    private ArrayList<Song> songsList;

    private ArrayList<Emotions> emoList;

    private boolean playlist_created=false;








    private ArrayList<Song> playlists;

    InPlaylistSongs frag = new InPlaylistSongs();

    public Playlist() {
    }

    onPositionSelectedPlaylist mCallback;

    public interface onPositionSelectedPlaylist {
        void OnPositionSelectedPlaylist(long playid, String name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onPositionSelectedPlaylist) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("---", "ragment outdetach");

    }

    @Override
    public void onDestroyView() {
        Log.i("---", "fragment out destroyed");
        super.onDestroyView();
    }

    public static Playlist newInstance() {
        Playlist fragment = new Playlist();
        /*Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        fragment.setArguments(bundle);*/
        return fragment;

    }

    ListView listview;Adapterr_playlist customAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_one, container, false);
          listview = (ListView) view.findViewById(R.id.frag_list_view);


        playlists = new ArrayList<Song>();

        getPlaylist();

          customAdapter = new Adapterr_playlist(this.getContext(), playlists);
        listview.setAdapter(customAdapter);

        MainActivity activity = (MainActivity) getActivity();
        songsList = activity.getsonglist();
        emoList = activity.getemolist();


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    dostuff();


            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Position:fragment genre " + position, Toast.LENGTH_LONG).show();

                Song p = playlists.get(position);
                long playid = p.getPlaylistId();
                String playname = p.getPlaylistName();

            /*    Fragment frag = new InPlaylistSongs();
                Bundle bundle = new Bundle();
                bundle.putLong("PlayId", playid);
                bundle.putString("PlayName", playname);
                frag.setArguments(bundle);

                FragmentManager f = getChildFragmentManager();
                FragmentTransaction t = f.beginTransaction();
                t.replace(R.id.container, frag);
       // t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.addToBackStack(null);*/
                // t.commit();


                mCallback.OnPositionSelectedPlaylist(playid, playname);


            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Song p = playlists.get(position);
                long playid = p.getPlaylistId();

                ContentResolver resolver = getContext().getContentResolver();
                Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;


                Uri deleteUri = ContentUris.withAppendedId(uri, playid);
                Log.i("TAG", "REMOVING Existing Playlist: " + playid);

                // delete the playlist
                resolver.delete(deleteUri, null, null);
                Toast.makeText(getActivity(), "Playlist removed", Toast.LENGTH_LONG).show();

                //customAdapter.notifyDataSetChanged();



                customAdapter.notifyDataSetChanged();
                //((Adapterr_playlist)listview.getAdapter()).notifyDataSetChanged();

                return true;
            }
        });



        return view;

    }




    public void getPlaylist() {

        ContentResolver musicResolver = getActivity().getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] proj = {"*"};
        String where = MediaStore.Audio.Playlists._ID + " = 18698";
        Cursor musicCursor = musicResolver.query(uri, proj, null, null, null);

        String[] proj2 = {MediaStore.Audio.Playlists.Members.DISPLAY_NAME};

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns


            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Playlists._ID);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Playlists.NAME);

            do {


                long thisId = musicCursor.getLong(idColumn);

                Uri aweuri = MediaStore.Audio.Playlists.Members.getContentUri("external", thisId);

                Cursor tempcursor = musicResolver.query(aweuri, proj2, null, null, null);
                long count =tempcursor.getCount();
                Log.i("Number of songs ", count + "");
                tempcursor.close();


                String thisTitle = musicCursor.getString(titleColumn);
               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                playlists.add(new Song(thisId, thisTitle,count, null, null ,null,-1,null));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }
    }



    public void dostuff(){

        playlist_created=false;

        //assigning values to emoList
        for(int i = 0; i<songsList.size();i++)
        {//getsonglist through intent as a string using gson
            String currentGenre = songsList.get(i).getgenre();

            if(currentGenre.equals("Rock"))
            {
                emoList.get(i).setHappy(8);
                emoList.get(i).setSurprise(4);
                emoList.get(i).setExcited(6);

                //a random number generator for assigning values
                //can be manipulated to hav values to the upperset of some value
                //fr eg: above 5 for happy in rock genre
            }
            if(currentGenre.equals("Blues"))
            {
                emoList.get(i).setHappy(5);
                emoList.get(i).setSurprise(2);
                emoList.get(i).setExcited(4);


            }
            //same for the rest

        }
        happyDetected = true;
        surpriseDetected = true;

        if (happyDetected) {
            createPlaylist("HAPPY");

            if (!exists) {

                for (int i = 0; i < emoList.size(); i++) {
                    int happy = emoList.get(i).getHappy();
                    int surprise = emoList.get(i).getSurprise();
                    int excited = emoList.get(i).getExcited();

                    //some condition that decides which playlist the song will go into

                    if (happy == 8 && surprise == 4 && excited == 6) {
                        long tkey = songsList.get(i).getId();
                        addtoPlaylist(tkey);
                    }


                    //get songid of that song and pass it to addtoplaylist method

                }


            }



        }
        else if (surpriseDetected)
        {
            createPlaylist("SURPRISED");
            if(!exists) {
               // getGenreSongList(bluesId);
                //getGenreSongList(clubId);

                for (int i = 0; i < emoList.size(); i++) {
                    int happy = emoList.get(i).getHappy();
                    int surprise = emoList.get(i).getSurprise();
                    int excited = emoList.get(i).getExcited();

                    //some condition that decides which playlist the song will go into

                    if (happy == 5 && surprise == 2 && excited == 4) {
                        long tkey = songsList.get(i).getId();
                        addtoPlaylist(tkey);
                    }


                    //get songid of that song and pass it to addtoplaylist method

                }




            }
        }




        playlist_created=true;

    }







    public void createPlaylist(String name) {

        ContentResolver resolver = getContext().getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursr = resolver.query(uri, null, null, null, null);
        if (cursr != null && cursr.moveToFirst()) {
            //get columns

            int ci = cursr.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.NAME);

            do {

                String d = cursr.getString(ci);
                if (d.equals(name)) {
                    Toast.makeText(getContext(), "Playlist " + name + " already exists!", Toast.LENGTH_LONG).show();
                    exists = true;
                    break;
                }
            }
            while (cursr.moveToNext());
            cursr.close();
        }


        if (!exists) {


            ContentValues values = new ContentValues();


            values.put(MediaStore.Audio.Playlists.NAME, name);
            // Uri newPlaylistUri = resolver.insert(uri, values);
            resolver.insert(uri, values);

            nameOfPlaylist = name; //is used in addtoPlaylist

            Log.i("TAG", "created");

        }
    }





    public void addtoPlaylist(long songid) {

        thisplaylistid = getplaylistidhere();

        ContentResolver resolver = getContext().getContentResolver();
        //use above fetched playlist id to insert songs
        Log.i("TAG", " 1234767647 " + thisplaylistid);
        Uri ri = MediaStore.Audio.Playlists.Members.getContentUri("external", thisplaylistid);
        Cursor crsr = resolver.query(ri, null, null, null, null);

        int playorder=1;
        int q = 0;


        if (crsr != null && crsr.moveToFirst()) {
            //get columns

            int id = crsr.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.PLAY_ORDER);


            do {

                q = crsr.getInt(id);



            }
            while (crsr.moveToNext());

            crsr.close();
        }
        playorder = q + 1;


        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, playorder);
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songid);


        resolver.insert(ri, values);
    }


    public long getplaylistidhere() {

        //get the play;ist id of just created playlist using its name
        ContentResolver resolver = getContext().getContentResolver();

        // long audioId = getIntent().getLongExtra("audioid",0);

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] proj = {MediaStore.Audio.Playlists.NAME};

        //  String where = MediaStore.Audio.Playlists.NAME + " = " + nameOfPlaylist;

        if(thisplaylistid==-1){
            Cursor cursr = resolver.query(uri, null, null, null, null);


            if (cursr != null && cursr.moveToFirst()) {
                //get columns

                int ci = cursr.getColumnIndexOrThrow
                        (MediaStore.Audio.Playlists.NAME);

                int id = cursr.getColumnIndexOrThrow
                        (MediaStore.Audio.Playlists._ID);


                do {

                    String d = cursr.getString(ci);

                    if (d.equals(nameOfPlaylist)) {


                        thisplaylistid = cursr.getLong(id);
                        Log.i("TAG", "  " + thisplaylistid);
                        break;
                    } else {
                        continue;
                    }


                }
                while (cursr.moveToNext());

                cursr.close();
            }

        }

        return thisplaylistid;
    }




}

package com.example.noorulain.second;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;



import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Noorulain on 29-01-2016.
 */
public class PlaylistCreator extends FragmentActivity {


    private boolean happyDetected = false;
    private boolean surpriseDetected = false;

    private long rockId,popId,poprockId,bluesId,clubId;

    private ArrayList<Song> rockList;

    private String audioid;

    private String nameOfPlaylist; // created after face detection

    private long thisplaylistid = -1;

    private boolean exists = false;

    private ArrayList<Song> songsList;

    private ArrayList<Emotions> emoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getGenreList();

        //getting songslist from main
       /* String json = getIntent().getStringExtra("songslist");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Song>>() {}.getType();
        songsList = gson.fromJson(json, type);*/


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
            //same for the rest

        }



        //retriving values and adding to playlist
       /* for(int i = 0; i<emoList.size();i++)
        {
            int happy = emoList.get(i).getHappy();
            int surprise = emoList.get(i).getSurprise();
            int excited = emoList.get(i).getExcited();

            //some condition that decides which playlist the song will go into
            //get audioid of that song and pass it to addtoplaylist method
            //audioid by passing its genreid to the method below
        }*/



        happyDetected = getIntent().getBooleanExtra("happy",false);
        surpriseDetected = getIntent().getBooleanExtra("surprised",false);

        if (happyDetected)
        {
            createPlaylist("HAPPY");

           /* getGenreSongList(rockId);
            getGenreSongList(popId);
            getGenreSongList(poprockId);*/



            for(int i = 0; i<emoList.size();i++)
            {
                int happy = emoList.get(i).getHappy();
                int surprise = emoList.get(i).getSurprise();
                int excited = emoList.get(i).getExcited();

                //some condition that decides which playlist the song will go into




                //get audioid of that song and pass it to addtoplaylist method
                //audioid by passing its genreid to the method below
            }







        }
        else if (surpriseDetected)
        {
            createPlaylist("SURPRISED");
            if(!exists) {
                getGenreSongList(bluesId);
                getGenreSongList(clubId);
            }
        }




    }

    public void getGenreList() {

        ContentResolver musicResolver = getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri genreUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(genreUri, null, null, null, null);

        //for no of songs in a genre
        String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};


        if (musicCursor != null && musicCursor.moveToFirst()) {


            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Genres._ID);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Genres.NAME);

            do {


                long genreId = musicCursor.getLong(idColumn);
                String genreTitle = musicCursor.getString(titleColumn);

//for no of songs in a genre
                Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                Cursor tempcursor = musicResolver.query(uri, proj2, null, null, null);
                long count =tempcursor.getCount();
                Log.i("Number of songs ", count + "");
                tempcursor.close();


                    if(genreTitle.equals("Rock"))
                    {
                        rockId = genreId;
                    }

                    if(genreTitle.equals("Pop"))
                    {
                        popId = genreId;
                    }

                if(genreTitle.equals("Pop/Rock"))
                {
                    poprockId = genreId;
                }

                if(genreTitle.equals("Blues"))
                {
                    bluesId = genreId;
                }

                if(genreTitle.equals("Club"))
                {
                    clubId = genreId;
                }

            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }

    public void getGenreSongList(long genreId) {


        //retrieve song info
        ContentResolver musicResolver = this.getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Uri genreUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        //for no of songs in a genre
        //String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);
        Cursor musicCursor = musicResolver.query(uri, null, null, null, null);



        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns


           /* int genresongColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Media.DISPLAY_NAME);
            int genresongartistColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Media.ARTIST);*/
            int auid = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Genres.Members.AUDIO_ID);
            do {
                //add songs to list
//for no of songs in a genre
               // String genresongtitle = musicCursor.getString(genresongColumn);
                //String genresongrtist = musicCursor.getString(genresongartistColumn);
                audioid = musicCursor.getString(auid);

                addtoPlaylist(audioid);

               /* String thisAlbum = musicCursor.getString(albumColumn);*/
               // genresList.add(new Song(genreId, null, -1,genresongtitle,genresongrtist));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }

    public void createPlaylist(String name) {

        ContentResolver resolver = getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursr = resolver.query(uri, null, null, null, null);
        if (cursr != null && cursr.moveToFirst()) {
            //get columns

            int ci = cursr.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.NAME);

            do {

                String d = cursr.getString(ci);
                if (d.equals(name)) {
                    Toast.makeText(this, "Playlist: " + name + " already exists!", Toast.LENGTH_LONG).show();
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





    public void addtoPlaylist(String titlekey) {

thisplaylistid = getplaylistidhere();

       ContentResolver resolver = getContentResolver();
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
        values.put(MediaStore.Audio.Playlists.Members.TITLE_KEY, titlekey);


        resolver.insert(ri, values);
    }


    public long getplaylistidhere() {

        //get the play;ist id of just created playlist using its name
        ContentResolver resolver = getContentResolver();

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


//do it later
   /* public void removePlaylist() {




        ContentResolver resolver = getContentResolver();
*//*
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] proj = {MediaStore.Audio.Playlists._ID};

          String where = MediaStore.Audio.Playlists._ID + " = " + playid;

        Cursor cur = r.query(uri,proj,where,null,null);

        if (cur != null && cur.moveToFirst()) {

            int id = cur.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists._ID);

        }

        r.delete(uri, MediaStore.Audio.Playlists._ID + " = " + playid, null);*//*
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        Uri deleteUri = ContentUris.withAppendedId(uri, playid);
        Log.i("TAG", "REMOVING Existing Playlist: " + playid);

        // delete the playlist
        resolver.delete(deleteUri, null, null);

        Log.i("TAG","Problem");

    }
*/
   /* public void removeFromPlaylist() {

        ContentResolver resolver = getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", YOUR_PLAYLIST_ID);
        resolver.delete(uri, MediaStore.Audio.Playlists.Members._ID +" = "+audioId, null);
    }

*/

} //Class ENDS!!
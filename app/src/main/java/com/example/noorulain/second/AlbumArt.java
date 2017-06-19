package com.example.noorulain.second;

/**
 * Created by Noorulain on 08-01-2016.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
//import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Noorulain on 06-01-2016.
 */
public class AlbumArt extends Fragment {



    public static AlbumArt newInstance() {
        AlbumArt fragment = new AlbumArt();
        /*Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        fragment.setArguments(bundle);*/
        return fragment;
    }


    private ArrayList<Song> songList;
    private ArrayList<Emotions> emoList;

   // Bitmap bit=null;
  //  Bitmap bitbig=null;

    public AlbumArt() {
    }


    //for passing song position
    OnPositionSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnPositionSelectedListener {
        void onPositionSelected(int position);
    }

    //for songlist
    onsongsetlistListener mcallback;

    // Container Activity must implement this interface
    public interface onsongsetlistListener {
         void onsonglistSelected(ArrayList<Song> sl,ArrayList<Emotions> el);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnPositionSelectedListener) context;
            mcallback= (onsongsetlistListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("---", "ragment outdetach");
       // bit.recycle();


    }

    @Override
    public void onDestroyView() {
        Log.i("---", "fragment out destroyed");
       // bit.recycle();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_music, container, false);
        final GridView gridview = (GridView) view.findViewById(R.id.gridview);


        songList = new ArrayList<Song>();
        emoList = new ArrayList<Emotions>();


        getSongList();



        //sort alphabetically by title
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getAlbum().compareTo(b.getAlbum());
            }
        });
//send songslist to main
        mcallback.onsonglistSelected(songList,emoList);

        Adapterr customAdapter = new Adapterr(this.getContext(), songList);
        gridview.setAdapter(customAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Position:fragment " + position, Toast.LENGTH_LONG).show();


                mCallback.onPositionSelected(position);

            }
        });


        return view;


    }




    public void getSongList() {
        //retrieve song info
        String genre_name = null;
        long genre_id = 0;
        ContentResolver musicResolver = getActivity().getContentResolver();

        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Albums.ALBUM_ART};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);


        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        //iteration over results


        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            try {
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Albums.ALBUM);


                do {
                    long albumId = musicCursor.getLong(musicCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));


                    //add songs to list


                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                    // bit = null;
                    // bitbig=null;


                /*try

                {

                    bit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), albumArtUri);
                   // bitbig=bit;
                    bit = Bitmap.createScaledBitmap(bit, 150, 150, true);
                   // bitbig = Bitmap.createScaledBitmap(bit, 550, 550, true);


                } catch (IOException e) {
                    e.printStackTrace();
                }
*/


                    long musicId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String thisAlbum = musicCursor.getString(albumColumn);


                    int nwmusicId = (int) musicId;
                    Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", nwmusicId);
                    Cursor genresCursor = musicResolver.query(uri, null, null, null, null);


                    if (genresCursor != null && genresCursor.moveToFirst()) {
                        int genre_column_index = genresCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                        int genre_id_column = genresCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);

                        try {
                            do {
                                genre_name = genresCursor.getString(genre_column_index);
                                genre_id = genresCursor.getLong(genre_id_column);


                            } while (genresCursor.moveToNext());
                        } finally {
                            genresCursor.close();
                        }

                    }


                    songList.add(new Song(musicId, thisTitle, thisArtist, thisAlbum, albumArtUri, genre_name, genre_id));
                    emoList.add(new Emotions(0, 0, 0));

                }
                while (musicCursor.moveToNext());
            } finally {
                musicCursor.close();
            }
        }

    }

}

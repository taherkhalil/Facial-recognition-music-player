package com.example.noorulain.second;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Noorulain on 24-01-2016.
 */
public class Artist extends Fragment {


    private ArrayList<Song> artistsList;

    public Artist(){

    }

    public static Artist newInstance() {
        Artist fragment = new Artist();
        /*Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        fragment.setArguments(bundle);*/
        return fragment;
    }

    onPositionSelectedArtist mCallback;

    public interface onPositionSelectedArtist {
        void OnPositionSelectedArtist(long artistid, String artistName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onPositionSelectedArtist) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("---", "Artistfragment out:detach");

    }

    @Override
    public void onDestroyView() {
        Log.i("---", "Artistfragment out:destroyed");
        super.onDestroyView();
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);

        artistsList = new ArrayList<Song>();

        getArtistList();

        Adapterr_artist customAdapter = new Adapterr_artist(this.getContext(), artistsList);
        listview.setAdapter(customAdapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Position:fragment artist " + position, Toast.LENGTH_LONG).show();

                Song a = artistsList.get(position);
                long artistid = a.getartistid();
                String artistName = a.getartisttitle();

              /*  Intent f = new Intent(getActivity(),InArtistAlbum.class);
                f.putExtra("ArtistId",artistid);
                f.putExtra("ArtistName",artistName);
                startActivity(f);
                */

                mCallback.OnPositionSelectedArtist(artistid, artistName);
            }
        });


        return view;

    }

    public void getArtistList() {
        //retrieve song info
        ContentResolver musicResolver = getActivity().getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri genreUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Uri genreUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(genreUri, null, null, null, null);

        //for no of songs in a genre
        // String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int keyColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST_KEY);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int countColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);

            do {



                //add songs to list

                long thisId = musicCursor.getLong(idColumn);
                String thisKey = musicCursor.getString(keyColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String count = musicCursor.getString(countColumn);

                String x=null;
                String y=null;


               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                artistsList.add(new Song(thisId,thisKey, thisTitle, count,x,y));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }



}

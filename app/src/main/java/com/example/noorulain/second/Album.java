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
public class Album extends Fragment {


    private ArrayList<Song> albumsList;

    public Album(){

    }

    public static Album newInstance() {
        Album fragment = new Album();
        /*Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        fragment.setArguments(bundle);*/
        return fragment;
    }

    onPositionSelectedAlbum mCallback;

    public interface onPositionSelectedAlbum {
        void OnPositionSelectedAlbum(long albid, String albName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onPositionSelectedAlbum) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("---", "Albumfragment out:detach");

    }

    @Override
    public void onDestroyView() {
        Log.i("---", "Albumfragment out:destroyed");
        super.onDestroyView();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.fragment_one, container, false);
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);

        albumsList = new ArrayList<Song>();

        getAlbumsList();

        Adapterr_album customAdapter = new Adapterr_album(this.getContext(), albumsList);
        listview.setAdapter(customAdapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Position:fragment album " + position, Toast.LENGTH_LONG).show();

                Song a = albumsList.get(position);
                long albid = a.getalbumid();
                String albName = a.getalbumtitle();
/*
                Intent f = new Intent(getActivity(),InAlbumSongs.class);
                f.putExtra("AlbumId",albid);
                f.putExtra("AlbumName",albName);
                startActivity(f);*/

                mCallback.OnPositionSelectedAlbum(albid, albName);

            }
        });


        return view;



    }

    public void getAlbumsList() {
        //retrieve song info
        ContentResolver musicResolver = getActivity().getContentResolver();

        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri genreUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(genreUri, null, null, null, null);

        //for no of songs in a genre
       // String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int countColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.NUMBER_OF_SONGS);



            do {



                //add songs to list

                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String count = musicCursor.getString(countColumn);


               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                albumsList.add(new Song(thisId, thisTitle, count,null,null));







            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }
}

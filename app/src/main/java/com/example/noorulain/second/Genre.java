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
 * Created by Noorulain on 23-01-2016.
 */
public class Genre extends Fragment {


    private ArrayList<Song> genresList;

    public Genre(){

    }

    public static Genre newInstance() {
        Genre fragment = new Genre();
        /*Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        fragment.setArguments(bundle);*/
        return fragment;
    }


    onPositionSelectedGenre mCallback;

    public interface onPositionSelectedGenre {
        void OnPositionSelectedGenre(long genid, String genName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onPositionSelectedGenre) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("---", "Genrefragment out:detach");

    }

    @Override
    public void onDestroyView() {
        Log.i("---", "Genrefragment out:destroyed");
        super.onDestroyView();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        final ListView listview = (ListView) view.findViewById(R.id.frag_list_view);

        genresList = new ArrayList<Song>();

        getGenreList();

        Adapterr_genre customAdapter = new Adapterr_genre(this.getContext(), genresList);
        listview.setAdapter(customAdapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Position:fragment genre " + position, Toast.LENGTH_LONG).show();

                Song g = genresList.get(position);
                long genid = g.getgenreid();
                String genName = g.getgenretitle();
/*

                Intent f = new Intent(getActivity(),InGenreSongs.class);
                f.putExtra("GenreId",genid);
                f.putExtra("GenreName",genName);
                startActivity(f);
*/

                mCallback.OnPositionSelectedGenre(genid, genName);


            }
        });


        return view;

    }



    public void getGenreList() {
        //retrieve song info
        ContentResolver musicResolver = getActivity().getContentResolver();

       // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri genreUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(genreUri, null, null, null, null);

        //for no of songs in a genre
        String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Genres._ID);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Genres.NAME);



            do {



                //add songs to list

                long thisId = musicCursor.getLong(idColumn);

//for no of songs in a genre
                Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", thisId);

                Cursor tempcursor = musicResolver.query(uri, proj2, null, null, null);
                long count =tempcursor.getCount();
                Log.i("Number of songs ", count + "");
tempcursor.close();




                String thisTitle = musicCursor.getString(titleColumn);
               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                genresList.add(new Song(thisId, thisTitle, count,null,null));







            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }


    }





}

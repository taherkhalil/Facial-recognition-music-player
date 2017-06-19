package com.example.noorulain.second;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Noorulain on 23-01-2016.
 */
public class Adapterr_genre extends BaseAdapter {

    private ArrayList<Song> genresList;
    private LayoutInflater layoutinflater;


    public Adapterr_genre(Context c, ArrayList<Song> genresList) {

        layoutinflater =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.genresList=genresList;
    }



    @Override
    public int getCount() {
        return genresList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        ViewHolder Viewholder;
        if(convertView == null){
            Viewholder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.fragment_genre_singleelement, parent, false);

            Viewholder.genreName = (TextView) convertView.findViewById(R.id.genre_title);
            Viewholder.genreNo = (TextView) convertView.findViewById(R.id.genre_nofitems);

            Viewholder.genreSongName = (TextView) convertView.findViewById(R.id.genre_song_name);
            Viewholder.genreSongArtist = (TextView) convertView.findViewById(R.id.genre_song_artist);

            convertView.setTag(Viewholder);


        }else
        {

            Viewholder = (ViewHolder)convertView.getTag();
        }

        String genrenamedanger = genresList.get(position).getgenretitle();
        String genreNodanger = String.valueOf(genresList.get(position).getnumber());


    if(genrenamedanger!=null && genreNodanger!=null)

    {
        Viewholder.genreName.setVisibility(View.VISIBLE);
        Viewholder.genreNo.setVisibility(View.VISIBLE);

        Viewholder.genreName.setText(genrenamedanger);
        Viewholder.genreNo.setText(genreNodanger);
    }

        String genresongnamedanger = genresList.get(position).getgenresong();
        String genresongartistdanger = genresList.get(position).getgenresongartist();

        if(genresongnamedanger!=null)
        {
            Viewholder.genreSongName.setVisibility(View.VISIBLE);
            Viewholder.genreSongArtist.setVisibility(View.VISIBLE);

            Viewholder.genreSongName.setText(genresongnamedanger);
            Viewholder.genreSongArtist.setText(genresongartistdanger);

        }


        return convertView;



    }


    static class ViewHolder{

        TextView genreName;
        TextView genreNo;

        TextView genreSongName;
        TextView genreSongArtist;



    }



}

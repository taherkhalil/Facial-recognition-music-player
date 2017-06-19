package com.example.noorulain.second;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Noorulain on 24-01-2016.
 */
public class Adapterr_artist extends BaseAdapter {



    private ArrayList<Song> artistsList;
    private LayoutInflater layoutinflater;


    public Adapterr_artist(Context c, ArrayList<Song> albumsList) {

        layoutinflater =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.artistsList=albumsList;
    }



    @Override
    public int getCount() {
        return artistsList.size();
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
            convertView = layoutinflater.inflate(R.layout.fragment_artist_singleelement, parent, false);

            Viewholder.artistName = (TextView) convertView.findViewById(R.id.artist_title);
            Viewholder.artistNofalbum = (TextView) convertView.findViewById(R.id.artist_no_of_albums);

            Viewholder.artistalbumName = (TextView) convertView.findViewById(R.id.artist_album_name);
            Viewholder.artistalbumnofsongs = (TextView) convertView.findViewById(R.id.artist_album_nofsongs);

            convertView.setTag(Viewholder);


        }else
        {

            Viewholder = (ViewHolder)convertView.getTag();
        }

        String albumnamedanger = artistsList.get(position).getartisttitle();
        String albumNosongsdanger = artistsList.get(position).getnoofalbumsofArtist();


        if(albumnamedanger!=null && albumNosongsdanger!=null)

        {
            Viewholder.artistName.setVisibility(View.VISIBLE);
            Viewholder.artistNofalbum.setVisibility(View.VISIBLE);

            Viewholder.artistName.setText(albumnamedanger);
            Viewholder.artistNofalbum.setText(albumNosongsdanger);
        }

        String artistalbumnamedanger = artistsList.get(position).getartistalbum();
        String artistalbumnofsongsdanger = artistsList.get(position).getartistalbumsongs();

        if(artistalbumnamedanger!=null && artistalbumnofsongsdanger!=null)
        {
            Viewholder.artistalbumName.setVisibility(View.VISIBLE);
            Viewholder.artistalbumnofsongs.setVisibility(View.VISIBLE);

            Viewholder.artistalbumName.setText(artistalbumnamedanger);
            Viewholder.artistalbumnofsongs.setText(artistalbumnofsongsdanger);

        }

        return convertView;

    }

    static class ViewHolder{

        TextView artistName;
        TextView artistNofalbum;

        TextView artistalbumName;
        TextView artistalbumnofsongs;



    }


}

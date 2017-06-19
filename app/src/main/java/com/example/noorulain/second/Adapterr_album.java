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
public class Adapterr_album extends BaseAdapter {


    private ArrayList<Song> albumsList;
    private LayoutInflater layoutinflater;


    public Adapterr_album(Context c, ArrayList<Song> albumsList) {

        layoutinflater =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.albumsList=albumsList;
    }



    @Override
    public int getCount() {
        return albumsList.size();
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
            convertView = layoutinflater.inflate(R.layout.fragment_album_singleelement, parent, false);

            Viewholder.albumName = (TextView) convertView.findViewById(R.id.album_title);
            Viewholder.albumNofsongs = (TextView) convertView.findViewById(R.id.album_nofitems);

            Viewholder.albumSongName = (TextView) convertView.findViewById(R.id.album_song_name);
            Viewholder.albumSongArtist = (TextView) convertView.findViewById(R.id.album_song_artist);

            convertView.setTag(Viewholder);


        }else
        {

            Viewholder = (ViewHolder)convertView.getTag();
        }

        String albumnamedanger = albumsList.get(position).getalbumtitle();
        String albumNosongsdanger = albumsList.get(position).getcount();


        if(albumnamedanger!=null && albumNosongsdanger!=null)

        {
            Viewholder.albumName.setVisibility(View.VISIBLE);
            Viewholder.albumNofsongs.setVisibility(View.VISIBLE);

            Viewholder.albumName.setText(albumnamedanger);
            Viewholder.albumNofsongs.setText(albumNosongsdanger);
        }

        String albumsongnamedanger = albumsList.get(position).getalbumsong();
        String albumsongartistdanger = albumsList.get(position).getalbumsongartist();

        if(albumsongnamedanger!=null && albumsongartistdanger!=null)
        {
            Viewholder.albumSongName.setVisibility(View.VISIBLE);
            Viewholder.albumSongArtist.setVisibility(View.VISIBLE);

            Viewholder.albumSongName.setText(albumsongnamedanger);
            Viewholder.albumSongArtist.setText(albumsongartistdanger);

        }


        return convertView;
    }

    static class ViewHolder{

        TextView albumName;
        TextView albumNofsongs;

        TextView albumSongName;
        TextView albumSongArtist;



    }
}

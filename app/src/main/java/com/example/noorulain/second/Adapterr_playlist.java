package com.example.noorulain.second;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Noorulain on 27-01-2016.
 */
public class Adapterr_playlist extends BaseAdapter {

    private ArrayList<Song> playlists;
    private LayoutInflater layoutinflater;


    public Adapterr_playlist(Context c, ArrayList<Song> playlists) {

        layoutinflater =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.playlists=playlists;
    }



    @Override
    public int getCount() {
        return playlists.size();
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
            convertView = layoutinflater.inflate(R.layout.fragment_playlist_singleelement, parent, false);

            Viewholder.playlistName = (TextView) convertView.findViewById(R.id.playlist_title);
            Viewholder.NofSongs = (TextView) convertView.findViewById(R.id.playlist_nofitems);

            Viewholder.playstSongName = (TextView) convertView.findViewById(R.id.playlist_song_name);
            Viewholder.playstSongArtist = (TextView) convertView.findViewById(R.id.playlist_song_artist);

            convertView.setTag(Viewholder);


        }else
        {

            Viewholder = (ViewHolder)convertView.getTag();
        }

        String playlistnamed = playlists.get(position).getPlaylistName();
        String playlistsongs = String.valueOf(playlists.get(position).getPlaylistCount());


        if(playlistnamed!=null && playlistsongs!=null)

        {
            Viewholder.playlistName.setVisibility(View.VISIBLE);
            Viewholder.NofSongs.setVisibility(View.VISIBLE);

            Viewholder.playlistName.setText(playlistnamed);
            Viewholder.NofSongs.setText(playlistsongs);
        }

        String playstsongnamedanger = playlists.get(position).getPlaylistSong();
        String playstsongartistdanger = playlists.get(position).getPlaylistSongArtist();


        if(playstsongnamedanger!=null)
        {
            Viewholder.playstSongName.setVisibility(View.VISIBLE);
            Viewholder.playstSongArtist.setVisibility(View.VISIBLE);

            Viewholder.playstSongName.setText(playstsongnamedanger);
            Viewholder.playstSongArtist.setText(playstsongartistdanger);

        }

        notifyDataSetChanged();

        return convertView;



    }


    static class ViewHolder{

        TextView playlistName;
        TextView NofSongs;

        TextView playstSongName;
        TextView playstSongArtist;



    }


}

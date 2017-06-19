package com.example.noorulain.second;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noorulain on 06-01-2016.
 */
public class Adapterr extends BaseAdapter {


    private LayoutInflater layoutinflater;

    private Context x;


    private ArrayList<Song> songs;


    public Adapterr(Context c, ArrayList<Song> theSongs) {
        this.x=c;
        this.songs = theSongs;
        layoutinflater =(LayoutInflater)x.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return songs.size();
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
            convertView = layoutinflater.inflate(R.layout.music_single, parent, false);
            Viewholder.albART = (ImageView)convertView.findViewById(R.id.ALBUMart);
            Viewholder.musicName = (TextView)convertView.findViewById(R.id.music_name);
            Viewholder.musicAuthor = (TextView)convertView.findViewById(R.id.music_author);
            Viewholder.musicGenre = (TextView)convertView.findViewById(R.id.music_genre);



            convertView.setTag(Viewholder);
        }else{

            Viewholder = (ViewHolder)convertView.getTag();
        }

       /* Song currSong = songs.get(position);

        Bitmap albbit = currSong.getalbumbitmap150();

        if(albbit!=null)
        {
            Viewholder.albART.setImageBitmap(songs.get(position).getalbumbitmap150());
        }
        else
        {
            Viewholder.albART.setImageResource(R.drawable.defaultalbart);
        }*/

        Viewholder.musicName.setText(songs.get(position).getTitle());
        Viewholder.musicAuthor.setText(songs.get(position).getAlbum());
        Viewholder.musicGenre.setText(songs.get(position).getgenre());
        Picasso.with(x).load(songs.get(position).getalbumarturi())
                .error(R.drawable.defaultalbart)
                .resize(150,150)
                .into(Viewholder.albART);






        return convertView;
    }

    static class ViewHolder{
        ImageView albART;
        TextView musicName;
        TextView musicAuthor;
        TextView musicGenre;


    }



}


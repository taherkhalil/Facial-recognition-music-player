package com.example.noorulain.second;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Noorulain on 08-01-2016.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,MediaPlayer.OnSeekCompleteListener{




    private final IBinder musicBind = new MusicBinder();

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;


    //notification area
    private String songTitle="";
    private String songArtist="";
    private static final int NOTIFY_ID=1;


    //shuffle
    private boolean shuffle=false;
    private Random rand;

    //oncompletionlistener
    private boolean completed = false;

    //onprepared for seekbar
    private boolean prepd = false;



    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return musicBind;
    }


    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }


    public void onCreate(){
        //create the service
        super.onCreate();
        completed = false;
        fornext = false;

//initialize position
        songPosn=0;
//create player
        player = new MediaPlayer();


        initMusicPlayer();

        //shuffle
        rand= new Random();
    }


    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            completed = true;
            mp.reset();
            playNext();

            if(completed){
            sendMessage();}
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        //start playback

        mp.start();



        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setTicker(songArtist)
                .setOngoing(true)
                .setContentTitle(songTitle)
                .setContentText(songArtist);



        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);

//for seekbar
        if(isPng())
        {
            prepd=true;
            sendDuration4Seekbar();
        }


    }

    private boolean from_all_songs =false;

    public boolean getFrom_all_songs(){
        return from_all_songs;
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;

        if(songs.size()>100)
        {
            from_all_songs=true;
        }
        else
        {
            from_all_songs=false;
        }

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if(!isPng()){
            go();

        }


    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

   public void setSong(int songIndex) {
       songPosn = songIndex;
   }

    public int getIndex(){
        return songPosn;
    }



    public void playSong(){
        //play a song


        if (from_all_songs) {
            //playSong();

            player.reset();

            //get song
            Song playSong = songs.get(songPosn);

            //get songtitle for notification area
            songTitle=playSong.getTitle();
            songArtist=playSong.getArtist();

//get id
            long currSong = playSong.getId();
//set uri
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    currSong);

            try{
                player.setDataSource(getApplicationContext(), trackUri);
            } catch(Exception e){
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }

            player.prepareAsync();




        }
        else{
            //playSongfromPLaylist(true);


            Song playSong;
            player.reset();



            if(!auto_play_all) {
                //get song
                playSong = songs.get(songPosn);
                completed = true;
                fornext = true;
            }
            else
            {

                if(!completed && !fornext)
                {
                    songPosn = 0;

                }
                playSong = songs.get(songPosn);
            }

            //get songtitle for notification area
            songTitle=playSong.getPlaylistSong();
            songArtist=playSong.getPlaylistSongArtist();

//get id
            long currSong = playSong.getiid();
//set uri
       /* Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);*/

            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist_id);

            Uri trackUri = ContentUris.withAppendedId(
                    uri,
                    currSong);

            try{
                player.setDataSource(getApplicationContext(), trackUri);
            } catch(Exception e){
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }

            player.prepareAsync();





        }







    }



    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;

        fornext=true;
        playSong();
    }

    private boolean fornext = false;

    //skip to next
    public void playNext(){
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;
        }
        else{
            songPosn++;
            if(songPosn>=songs.size()) songPosn=0;
        }
        fornext=true;
        playSong();

    }


    public void setShuffle(){
        if(shuffle) {
            shuffle=false;
        }
        else {
            shuffle=true;
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }






    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name:SongCompleted");
        // You can also include some extra data.
       // intent.putExtra("message", "This is my message!");
        intent.putExtra("Completed",completed);
        intent.putExtra("whichListToUse",from_all_songs);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendDuration4Seekbar() {
        Log.d("sender", "Broadcasting message:durationforseekbar");
        Intent intent = new Intent("custom-event-name:SongDuration");
        // You can also include some extra data.
        // intent.putExtra("message", "This is my message!");
       // intent.putExtra("Duration",duration);
        intent.putExtra("Prepared",prepd);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private long playlist_id;

    public void setPlaylistId(long playlist_id)
    {
        this.playlist_id=playlist_id;
    }

    private boolean auto_play_all = false;
    public void setAutoPlayAll(boolean auto_play_all)
    {
        this.auto_play_all=auto_play_all;
    }

    public void setcompleted(boolean completed)
    {
        this.completed=completed;
    }
    public void setfornext(boolean fornext)
    {
        this.fornext=fornext;
    }


}

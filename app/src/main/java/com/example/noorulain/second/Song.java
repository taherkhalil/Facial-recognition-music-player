package com.example.noorulain.second;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Noorulain on 08-01-2016.
 */
public class Song {

    //Songs

    private long id;
    private String title;
    private String artist;
    private String album;
   // private Bitmap bmscaled=null;

    private Uri albumArturi;

    //private Bitmap bm=null;
    private String genre;
    private long genre_id;



    public Song(long songid, String songtitle, String songartist,String songAlbum,
                Uri albumarturi,  String gen,long genn) {

        this.id = songid;
        this.title = songtitle;
        this.artist = songartist;
        this.album = songAlbum;

       // this.bm=bm2;
       // this.bmscaled=bm1;

        this.albumArturi=albumarturi;

        this.genre = gen;
        this.genre_id = genn;


    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }


    public String getAlbum() {
        return album;
    }

    public Uri getalbumarturi()
    {
        //bmscaled = Bitmap.createScaledBitmap(bmscaled, 150, 150, true);
        return albumArturi;
    }

   /* public Bitmap getalbumbitmap550()
    {
        //bm = Bitmap.createScaledBitmap(bm, 550, 550, true);
        return bm;
    }*/

    public String getgenre() {
        return genre;
    }

    public long getgenre_id(){
        return genre_id;
    }




    //genre

    private long genreid;
    private String genretitle;
    private long numbr;
    private String genresong;
    private String genresongartist;

    public Song(long gid,String gtitle,long num,String genre_song,String genreartist)
    {
        this.genreid=gid;
        this.genretitle=gtitle;
        this.numbr=num;
        this.genresong=genre_song;
        this.genresongartist=genreartist;

    }


    public long getgenreid(){
        return genreid;
    }

    public String getgenretitle(){
        return genretitle;
    }

    public long getnumber(){
        return numbr;
    }

    public String getgenresong(){
        return genresong;
    }
    public String getgenresongartist(){
        return genresongartist;
    }



    //albums

    private long albumid;
    private String albumtitle;
    private String count;
    private String albumsong;
    private String albumsongartist;

    public Song(long aid,String atitle,String num,String album_song,String album_artist)
    {
        this.albumid=aid;
        this.albumtitle=atitle;
        this.count=num;
        this.albumsong=album_song;
        this.albumsongartist=album_artist;

    }


    public long getalbumid(){
        return albumid;
    }

    public String getalbumtitle(){
        return albumtitle;
    }

    public String getcount(){
        return count;
    }

    public String getalbumsong(){
        return albumsong;
    }
    public String getalbumsongartist(){
        return albumsongartist;
    }


//artist

    private long artistid;

    private String artistkey;

    private String artisttitle;
    private String artistNofAlbums;
    private String artistalbums;
    private String artistalbumsongs;

    public Song(long arid, String arkey, String artitle, String noalbums,String albs, String albsong)
    {
        this.artistid=arid;
        this.artistkey=arkey;
        this.artisttitle=artitle;
        this.artistNofAlbums=noalbums;
        this.artistalbums=albs;
        this.artistalbumsongs=albsong;

    }


    public long getartistid(){
        return artistid;
    }

    public String getartistkey(){
        return artistkey;
    }

    public String getartisttitle(){
        return artisttitle;
    }

    public String getnoofalbumsofArtist(){
        return artistNofAlbums;
    }

    public String getartistalbum(){
        return artistalbums;
    }
    public String getartistalbumsongs(){
        return artistalbumsongs;
    }



    //playlists

    private long playlistId;
    private String playlistName;
    private long playlistcount;

    private String playlistSong;
    private String playlistSongArtist;
    private String audio_id;
    private long iid;

    private Uri album_art_uri_playlist;

    public Song(long playid, String name, long count,String plsong, String plartist, String audioid,
                long iid,Uri albarturiplay) {


        this.playlistId = playid;
        this.playlistName = name;
        this.playlistcount = count;
        this.playlistSong = plsong;
        this.playlistSongArtist = plartist;
        this.audio_id=audioid;
        this.iid=iid;
        this.album_art_uri_playlist=albarturiplay;

    }

    public long getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public long getPlaylistCount() {
        return playlistcount;
    }


    public String getPlaylistSong() {



        return playlistSong;
    }


    public String getPlaylistSongArtist() {
        return playlistSongArtist;
    }

    public String getAudioId() {
        return audio_id;
    }

    public long getiid() {
        return iid;
    }

    public Uri getalbum_art_uri_playlist(){
        return album_art_uri_playlist;
    }

}


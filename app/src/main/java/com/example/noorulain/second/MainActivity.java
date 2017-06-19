package com.example.noorulain.second;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.MediaController.MediaPlayerControl;


import com.musixmatch.lyrics.MissingPluginException;
import com.musixmatch.lyrics.musiXmatchLyricsConnector;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
//make a single layout which displays Inalbum,Ingenre,Inartist Songs, use this layout
//as a new activity instead of the new being called now

//deduce the adapters into a common adapter which displays all d fragments
//fr trying leavve albumart fragment for now.

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                MediaPlayerControl,
                                                                AlbumArt.OnPositionSelectedListener,
                                                                AlbumArt.onsongsetlistListener,
                                                                Playlist.onPositionSelectedPlaylist,
                                                                InPlaylistSongs.onPositionSelectedInPlaylist,
                                                                InPlaylistSongs.onPositionSelectedInPlaylist4list,
                                                                Genre.onPositionSelectedGenre,
                                                                Album.onPositionSelectedAlbum,
                                                                Artist.onPositionSelectedArtist{

    private SlidingUpPanelLayout mLayout;

    //music Service var
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    //for media controls
    private boolean paused = false, playbackPaused = true;

    ImageView prev = null;
    ImageView play = null;
    ImageView next = null;

    ImageView inpane_prev = null;
    ImageView inpane_play = null;
    ImageView inpane_next = null;

    //List of songs
    ArrayList<Song> songList;

    ArrayList<Emotions> emoList;

    ImageView albartinpane = null;
    ImageView albartonpane = null;
    TextView onPaneSongName;
    TextView onPaneSongAlbum;


    //shared preference
    public static final String PREFS_NAME = "MyPrefsFile";

    private boolean firstLaunch = true;
    private int pos; //restore pref
    private boolean fL;

    private int currentPos;
    private int currSongpositionnext = 0;
    private int currSongpositionprev = 0;
    private int pos4SharedPref = 0;

    //fwd and rewind
    private int seekFwdTime = 5000; //milliseconds
    private int seekRevTime = 5000; //milliseconds

   //seekbar
    private SeekBar seekBar;
    private Handler mHandler ;
    private double startTime = 0;
    private double finalTime = 0;
    private TextView strt;
    private TextView etime;

    //seekbar shared pref
    private int duration = 0;

    private Adapterr_playlist customadapter;


    private ArrayList<Song> templist;








    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initializeLayouts();


        //Play function
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClkd();
            }
        });

        //next function
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextClkd();
            }
        });

        //forward function
        next.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nextOnlongClkd();
                return true;
            }
        });

        //prev function
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevClkd();
            }
        });

        //rewind function
        prev.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                prevOnLongClkd();
                return true;
            }
        });

        inpane_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClkd();
            }
        });

        inpane_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevClkd();
            }
        });

        inpane_prev.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                prevOnLongClkd();
                return true;
            }
        });



        inpane_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextClkd();

            }
        });

        inpane_next.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){
                nextOnlongClkd();
                return true;
            }
        });


       //Sliding Pane
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("TAG", "onPanelSlide, offset " + slideOffset);
                prev.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i("TAG", "onPanelExpanded");

                prev.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i("TAG", "onPanelCollapsed");
                prev.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);


            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i("TAG", "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i("TAG", "onPanelHidden");
            }
        });

        //SEEKBAR
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                Log.i("TAG", "Changing seekbar's progress");

                if (isPlaying() && fromUser)
                {
                    int seekpos = seekBar.getProgress();
                    seekTo(seekpos);
                }
                else
                {
                    mHandler.removeCallbacks(UpdateSongTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });


        firstLaunch=false; //for shared pref
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();



            }
        });*/




    } //oncreate ends


    //from Artist Fragment

    @Override
    public void OnPositionSelectedArtist(long artistid, String artistName) {

        Fragment frag = new InArtistAlbum();
        Bundle bundle = new Bundle();
        bundle.putLong("ArtistId", artistid);
        bundle.putString("ArtistName", artistName);
        frag.setArguments(bundle);

        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.here, frag);
        t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.addToBackStack(null);
        t.commit();

    }


    //from Album Fragment

    @Override
    public void OnPositionSelectedAlbum(long albid, String albName) {

        Fragment frag = new InAlbumSongs();
        Bundle bundle = new Bundle();
        bundle.putLong("AlbumId", albid);
        bundle.putString("AlbumName", albName);
        frag.setArguments(bundle);

        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.here, frag);
        t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.addToBackStack(null);
        t.commit();

    }

//from Genre fragment
    @Override
    public void OnPositionSelectedGenre(long genid, String genName) {

        Fragment frag = new InGenreSongs();
        Bundle bundle = new Bundle();
        bundle.putLong("GenreId", genid);
        bundle.putString("GenreName", genName);
        frag.setArguments(bundle);

        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.here, frag);
        t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.addToBackStack(null);
        t.commit();

    }



    //from Playlist fragments
    @Override
    public void OnPositionSelectedPlaylist(long playid, String name) {

        Fragment frag = new InPlaylistSongs();
        Bundle bundle = new Bundle();
        bundle.putLong("PlayId", playid);
        bundle.putString("PlayName", name);
        frag.setArguments(bundle);

        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.here, frag);
        t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.addToBackStack(null);
        t.commit();

    }





    private long playlistid;

    @Override
    public void OnPositionSelectedInPlaylist4list(ArrayList<Song> list,long id) {

        this.playList = list;
        playlistid=id;
    }

    private boolean auto_play_all= false;

    @Override
    public void OnPositionSelectedInPlaylist(int position) {

        musicSrv.setList(playList);
        musicSrv.setPlaylistId(playlistid);
        musicSrv.setSong(position);
        musicSrv.setAutoPlayAll(false);

        play.setImageResource(R.drawable.pausefinal);
        playbackPaused = false;

        musicSrv.playSong();


        Song currSong = playList.get(position);
        //Bitmap alb = currSong.getalbumbitmap550();
        //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

        //for details on the sliding pane

        //albartonpane.setImageBitmap(alb);
        Picasso.with(getApplicationContext())
                .load(playList.get(position).getalbumarturi())
                .resize(50, 60)
                .into(albartonpane);

        onPaneSongName.setText(playList.get(position).getPlaylistSong());
        onPaneSongAlbum.setText(playList.get(position).getPlaylistSongArtist());

        Picasso.with(getApplicationContext())
                .load(playList.get(position).getalbumarturi())
                .error(R.drawable.defaultalbart)
                .resize(600,700)
                .into(albartinpane);


    }

    public ArrayList<Song> getsonglist() {
        return songList;
    }

    public ArrayList<Emotions> getemolist(){
        return emoList;
    }



    //from Albumart Fragment! Grid listeners
    public void onsonglistSelected(ArrayList<Song> sl,ArrayList<Emotions> el){
        this.songList=sl;
        this.emoList=el;

    }

    public void onPositionSelected(int position) {

        Toast.makeText(this, "Position:main  onpositionselected" + position, Toast.LENGTH_LONG).show();

        pos4SharedPref = position;
        try {

musicSrv.setList(songList);

            musicSrv.setSong(position);
            musicSrv.playSong();

            play.setImageResource(R.drawable.pausefinal);
            playbackPaused = false;

           /* Song currSong = songList.get(position);
            Bitmap alb = currSong.getalbumbitmap550();
            alb = Bitmap.createScaledBitmap(alb, 50, 60, true);*/

            //for details on the sliding pane
           // albartonpane.setImageBitmap(alb);

            Picasso.with(getApplicationContext())
                    .load(songList.get(position).getalbumarturi())
                    .resize(50,60)
                    .into(albartonpane);


            onPaneSongName.setText(songList.get(position).getTitle());
            onPaneSongAlbum.setText(songList.get(position).getAlbum());

            //for song details in d sliding pane

            Picasso.with(getApplicationContext())
                    .load(songList.get(position).getalbumarturi())
                   // .load("http://www.google.com/images"+"taylor swift")
                    .error(R.drawable.defaultalbart)
                    .resize(600,700)
                    .into(albartinpane);
           /* Bitmap albbit = currSong.getalbumbitmap550();

            if (albbit != null) {
                albartinpane.setImageBitmap(songList.get(position).getalbumbitmap550());

            } else {
                albartinpane.setImageResource(R.drawable.defaultalbart);
            }*/
        } catch (NullPointerException p) {
            Toast.makeText(this, "Position main: " + p, Toast.LENGTH_LONG).show();
        }
    }
//Fragment listener ends!!!


    public void initializeLayouts() {

        //Media PLayer controls
        prev = (ImageView) findViewById(R.id.prev_button);
        play = (ImageView) findViewById(R.id.play_button);
        next = (ImageView) findViewById(R.id.next_button);

        inpane_prev = (ImageView) findViewById(R.id.inpane_prev);
        inpane_play = (ImageView) findViewById(R.id.inpane_play);
        inpane_next = (ImageView) findViewById(R.id.inpane_next);

        //seekbar
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        strt = (TextView) findViewById(R.id.starttime);
        etime = (TextView) findViewById(R.id.endtime);
        seekBar.setClickable(false);  //check

        //onPane details
        albartonpane = (ImageView) findViewById(R.id.slide_song_art);
        onPaneSongName = (TextView) findViewById(R.id.slide_song_title);
        onPaneSongAlbum = (TextView) findViewById(R.id.slide_song_album);

        //image view for albumart in d sliding pane
        albartinpane = (ImageView) findViewById(R.id.pane_song_art);

        //Sliding Pane init layout
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        //Handler for seekbar ,Playback in background
        mHandler = new Handler();




    }


    public void playClkd() {

        if (!playbackPaused) {

            play.setImageResource(R.drawable.play_final);
            pause();
            mHandler.removeCallbacks(UpdateSongTime);

        } else if (paused) {

            play.setImageResource(R.drawable.pausefinal);
            seekTo(currentPos);

            musicSrv.go();
            mHandler.postDelayed(UpdateSongTime, 100);
            paused = false;
            currentPos = 0;
            playbackPaused = false;

        } else {

            play();
            mHandler.postDelayed(UpdateSongTime, 100);
        }

    }

    public void play() {

        try {

            musicSrv.setSong(pos);

            if (playbackPaused) {

                play.setImageResource(R.drawable.pausefinal);

                musicSrv.playSong();
                playbackPaused = false;

            }
        } catch (NullPointerException p) {
            Toast.makeText(this, "Position main: " + p, Toast.LENGTH_LONG).show();
        }

    }
    


    public void nextClkd() {

        currSongpositionprev = -1;

        musicSrv.playNext();
        play.setImageResource(R.drawable.pausefinal);
        currSongpositionnext = musicSrv.getIndex();

        if(musicSrv.getFrom_all_songs()) {
            try {
               /* Song currSong = songList.get(currSongpositionnext);
                Bitmap alb = currSong.getalbumbitmap550();
                alb = Bitmap.createScaledBitmap(alb, 50, 60, true);*/

                //for details on the sliding pane

                //albartonpane.setImageBitmap(alb);

                Picasso.with(getApplicationContext())
                        .load(songList.get(currSongpositionnext).getalbumarturi())
                        .resize(50, 60)
                        .into(albartonpane);


                onPaneSongName.setText(songList.get(currSongpositionnext).getTitle());
                onPaneSongAlbum.setText(songList.get(currSongpositionnext).getAlbum());

                //for song details in d sliding pane

                Picasso.with(getApplicationContext())
                        .load(songList.get(currSongpositionnext).getalbumarturi())
                        .error(R.drawable.defaultalbart)
                        .resize(600,700)
                        .into(albartinpane);


               /* Bitmap albbit = currSong.getalbumbitmap550();

                if (albbit != null) {
                    albartinpane.setImageBitmap(songList.get(currSongpositionnext).getalbumbitmap550());

                } else {
                    albartinpane.setImageResource(R.drawable.defaultalbart);
                }*/
            } catch (NullPointerException p) {

            }
        }
        else
        {
            try {
                Song currSong = playList.get(currSongpositionnext);
                //Bitmap alb = currSong.getalbumbitmap550();
                //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

                //for details on the sliding pane

                Picasso.with(getApplicationContext())
                        .load(playList.get(currSongpositionnext).getalbum_art_uri_playlist())
                        .resize(50, 60)
                        .into(albartonpane);


                onPaneSongName.setText(playList.get(currSongpositionnext).getPlaylistSong());
                onPaneSongAlbum.setText(playList.get(currSongpositionnext).getPlaylistSongArtist());

                //for song details in d sliding pane
                Picasso.with(getApplicationContext())
                        .load(playList.get(currSongpositionnext).getalbum_art_uri_playlist())
                        .error(R.drawable.ic_launcher)
                        .resize(600,700)
                        .into(albartinpane);

            } catch (NullPointerException p) {

            }
        }


    }

    public void nextOnlongClkd() {

        Log.i("TAG", "looong");

        int x = getCurrentPosition();
        int x2 = x + seekFwdTime;

        if (x2 < getDuration()) {
            seekTo(x2);
        } else {
            seekTo(getDuration());
        }
    }

    public void prevClkd() {

        currSongpositionnext = -1;
        musicSrv.playPrev();
        play.setImageResource(R.drawable.pausefinal);
        currSongpositionprev = musicSrv.getIndex();

        if (musicSrv.getFrom_all_songs()) {

            try {
              //  Song currSong = songList.get(currSongpositionprev);
                //Bitmap alb = currSong.getalbumbitmap550();
                //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

                //for details on the sliding pane
               // albartonpane.setImageBitmap(alb);

                Picasso.with(getApplicationContext())
                        .load(songList.get(currSongpositionprev).getalbumarturi())
                        .resize(50, 60)
                        .into(albartonpane);

                onPaneSongName.setText(songList.get(currSongpositionprev).getTitle());
                onPaneSongAlbum.setText(songList.get(currSongpositionprev).getAlbum());

                //for song details in d sliding pane
                /*Bitmap albbit = currSong.getalbumbitmap550();

                if (albbit != null) {
                    albartinpane.setImageBitmap(songList.get(currSongpositionprev).getalbumbitmap550());

                } else {
                    albartinpane.setImageResource(R.drawable.defaultalbart);
                }*/

                Picasso.with(getApplicationContext())
                        .load(songList.get(currSongpositionprev).getalbumarturi())
                        .error(R.drawable.defaultalbart)
                        .resize(600,700)
                        .into(albartinpane);


            } catch (NullPointerException p) {

            }
        } else {
            try {
                Song currSong = playList.get(currSongpositionprev);
                //Bitmap alb = currSong.getalbumbitmap550();
                //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

                //for details on the sliding pane
               // albartonpane.setImageBitmap(alb);

                Picasso.with(getApplicationContext())
                        .load(playList.get(currSongpositionprev).getalbum_art_uri_playlist())
                        .resize(50,60)
                        .into(albartonpane);

                onPaneSongName.setText(playList.get(currSongpositionprev).getPlaylistSong());
                onPaneSongAlbum.setText(playList.get(currSongpositionprev).getPlaylistSongArtist());

                //for song details in d sliding pane
                Picasso.with(getApplicationContext())
                        .load(playList.get(currSongpositionprev).getalbum_art_uri_playlist())
                        .error(R.drawable.ic_launcher)
                        .resize(600, 700)
                        .into(albartinpane);

            } catch (NullPointerException p) {

            }
        }


    }

    public void prevOnLongClkd() {

        Log.i("TAG", "looong");

        int y = getCurrentPosition();
        int y2 = y - seekRevTime;

        if (y2 >= 0) {
            seekTo(y2);
        } else {
            seekTo(0);
        }

    }





    //for seekbar initialization
    private BroadcastReceiver mMessageReceiverDuration = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean prepared = intent.getBooleanExtra("Prepared",false);
            if(prepared) {
                seekactive();
            }
        }
    };


private boolean frm_all_songs=false;
//for next song details after oncompleion from service
    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean completed = intent.getBooleanExtra("Completed",false);
             frm_all_songs = intent.getBooleanExtra("whichListToUse",false);
            if(completed){
                onSongCompletion();
            }
        }
    };

    //after a song gets completed
    public void onSongCompletion() {


        int songPos = musicSrv.getIndex();
        if(songPos>0) {

            pos4SharedPref = songPos;

            if (frm_all_songs) {

                try {
                   // Song currSong = songList.get(songPos);
                    //Bitmap alb = currSong.getalbumbitmap550();
                    //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

                    //for details on the sliding pane
                    //albartonpane.setImageBitmap(alb);

                    Picasso.with(getApplicationContext())
                            .load(songList.get(songPos).getalbumarturi())
                            .resize(50, 60)
                            .into(albartonpane);

                    onPaneSongName.setText(songList.get(songPos).getTitle());
                    onPaneSongAlbum.setText(songList.get(songPos).getAlbum());

                    //for song details in d sliding pane

                    Picasso.with(getApplicationContext())
                            .load(songList.get(songPos).getalbumarturi())
                            .error(R.drawable.defaultalbart)
                            .resize(600,700)
                            .into(albartinpane);
                    /*Bitmap albbit = currSong.getalbumbitmap550();

                    if (albbit != null) {
                        albartinpane.setImageBitmap(songList.get(songPos).getalbumbitmap550());

                    } else {
                        albartinpane.setImageResource(R.drawable.defaultalbart);
                    }*/
                } catch (NullPointerException p) {

                }
            } else { //after song completion in auto_play playlist
                try {
                    Song currSong = playList.get(songPos);
                    // Bitmap alb = currSong.getalbumbitmap550();
                    //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

                    //for details on the sliding pane
                    //albartonpane.setImageBitmap(alb);

                    Picasso.with(getApplicationContext())
                            .load(playList.get(songPos).getalbum_art_uri_playlist())
                            .resize(50, 60)
                            .into(albartonpane);

                    onPaneSongName.setText(playList.get(songPos).getPlaylistSong());
                    onPaneSongAlbum.setText(playList.get(songPos).getPlaylistSongArtist());

               //for song details in d sliding pane
                    Picasso.with(getApplicationContext())
                            .load(playList.get(songPos).getalbum_art_uri_playlist())
                            .error(R.drawable.ic_launcher)
                            .resize(600,700)
                            .into(albartinpane);

                } catch (NullPointerException p) {

                }
            }
        }
    }



    //functions coming from service
    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {

        currentPos= getCurrentPosition();
        musicSrv.pausePlayer();
        playbackPaused=true;
        paused=true;
    }


    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;

    }

    @Override
    public void seekTo(int pos)
    {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }


    //MediaPlayerControl overrides
    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }



    //Connection to service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);

            musicBound = true;
            restorePref();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    // Restore preferences
    public void restorePref() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        pos = settings.getInt("Position", 0);
        fL = settings.getBoolean("Launch", true);

        int dur = settings.getInt("songDuration", 0);

        //details on pane

        if (!fL) {
            /*Song currSong = songList.get(pos);
            Bitmap alb = currSong.getalbumbitmap550();

            alb = Bitmap.createScaledBitmap(alb, 50, 60, true);*/



        //onpane

            //albartonpane.setImageBitmap(alb);

            Picasso.with(getApplicationContext())
                    .load(songList.get(pos).getalbumarturi())
                    .resize(50, 60)
                    .into(albartonpane);



            onPaneSongName.setText(songList.get(pos).getTitle());
            onPaneSongAlbum.setText(songList.get(pos).getAlbum());

        //inpane
           // albartinpane.setImageBitmap(alb);

            Picasso.with(getApplicationContext())
                    .load(songList.get(pos).getalbumarturi())
                    .error(R.drawable.defaultalbart)
                    .resize(600,700)
                    .into(albartinpane);


        }

        int sT = 0;
        etime.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) dur),
                        TimeUnit.MILLISECONDS.toSeconds((long) dur) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) dur)))
        );

        strt.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) sT),
                TimeUnit.MILLISECONDS.toSeconds((long) sT) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sT))));


    }


    @Override
    public void onDestroy() {
      //ending service
        unbindService(musicConnection);
        stopService(playIntent);
        musicSrv=null;

        //setting position in sharedprf

        if(currSongpositionnext==-1)
        {
         pos4SharedPref = currSongpositionprev;
        }
        else if(currSongpositionprev==-1)
        {
            pos4SharedPref = currSongpositionnext;
        }

        //firstLaunch=false;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Position", pos4SharedPref);
        editor.putBoolean("Launch", firstLaunch);
        editor.putInt("songDuration", duration);

        // Commit the edits!
        editor.apply();


        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiverDuration);


        mHandler.removeCallbacks(UpdateSongTime);


        super.onDestroy();
    }




    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiverDuration);




        paused = true;



        //setting position in sharedprf

        if(currSongpositionnext==-1)
        {
            pos4SharedPref = currSongpositionprev;
        }
        else if(currSongpositionprev==-1)
        {
            pos4SharedPref = currSongpositionnext;
        }

        //firstLaunch=false;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Position", pos4SharedPref);
        editor.putBoolean("Launch", firstLaunch);
        editor.putInt("songDuration", duration);

        // Commit the edits!
        editor.apply();


        super.onPause();

    }



    @Override
    public void onResume() {

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name:SongCompleted"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiverDuration, new IntentFilter("custom-event-name:SongDuration"));

        Toast.makeText(getApplicationContext(), "from face class", Toast.LENGTH_SHORT).show();




        super.onResume();



    }

    @Override
    public void onStop(){
        super.onStop();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_end:
               ///stopService(playIntent);
                //musicSrv = null;
                break;
            //for shuffle
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;

            //to face recognition activity
            case R.id.toFaceRec:
                Intent f = new Intent(this,Rex.class);
                startActivityForResult(f,1);
                //1. send if playlist is created or not to rex :for creating playlist after face
                //detection for the first time.

                //2. for creating playlist for the first time face is detected
                //put a button on d camera interface saying create playlist
                //make the button invisible as sson as clicked and recognize face
                //get the emotion and check if playlist exists
                //fetch all the playlist creation code there i.e dostuff() from playlist frag!

                //wont need to do first if button on camera interface

            break;

        }
         return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                boolean smiledetected = data.getBooleanExtra("SmileDetected", false);
                String playlist = data.getStringExtra("Playlist");

                if (smiledetected) {
                    long play_id_accessed = get_playlist_id(playlist);
                    getList_for_playlist(play_id_accessed);
                    musicSrv.setList(playList);
                    musicSrv.setPlaylistId(play_id_accessed);
                    musicSrv.setAutoPlayAll(true);
                    play.setImageResource(R.drawable.pausefinal);

                    musicSrv.setcompleted(false);
                    musicSrv.setfornext(false);


                    musicSrv.playSong();
                    playbackPaused = false;

                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                    int position = musicSrv.getIndex();

                    //Song currSong = playList.get(position);
                    //Bitmap alb = currSong.getalbumbitmap550();
                    //alb = Bitmap.createScaledBitmap(alb, 50, 60, true);

                    //for details on the sliding pane

                    Picasso.with(getApplicationContext())
                            .load(playList.get(position).getalbum_art_uri_playlist())
                            .resize(50,60)
                            .into(albartonpane);

                    //albartonpane.setImageBitmap(alb);
                    onPaneSongName.setText(playList.get(position).getPlaylistSong());
                    onPaneSongAlbum.setText(playList.get(position).getPlaylistSongArtist());

                    //inpane

                    Picasso.with(getApplicationContext())
                            .load(playList.get(position).getalbum_art_uri_playlist())
                            .error(R.drawable.ic_launcher)
                            .resize(600, 700)
                            .into(albartinpane);
                }
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch(position){
                case 0:
                    //return PlaceholderFragment.newInstance(position + 1);
                    fragment = new AlbumArt();
                    break;
                case 1:
                    //return PlaceholderFragment.newInstance(position + 1);
                    fragment = new Playlist();
                    break;

                case 2:
                    fragment = new Genre();
                    break;
                    //return PlaceholderFragment.newInstance(position + 1);

                case 3:
                    fragment = new Album();
                    break;
                    //return PlaceholderFragment.newInstance(position + 1);
                case 4:
                    fragment = new Artist();
                    break;
                    //return PlaceholderFragment.newInstance(position + 1);
            }
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALL SONGS";
                case 1:
                    return "PLAYLISTS";
                case 2:
                    return "GENRES";
                case 3:
                    return "ALBUMS";
                case 4:
                    return "ARTISTS";
            }
            return null;
        }



    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {


        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //seekbar code
    public void seekactive(){


        startTime = getCurrentPosition();
        finalTime = getDuration();
        duration = getDuration();

        seekBar.setMax((int) finalTime);

        etime.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        strt.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );

        seekBar.setProgress((int) startTime);
        mHandler.removeCallbacks(UpdateSongTime);
        mHandler.postDelayed(UpdateSongTime, 100);

    }

    //seekbar position updater
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = getCurrentPosition();
            finalTime = getDuration();

            finalTime = finalTime - startTime;
            strt.setText(String.format("%d min, %d sec",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            etime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
            );

            seekBar.setProgress((int)startTime);
            mHandler.postDelayed(this, 100);
        }
    };


    /*private BroadcastReceiver mMessageReceiverEmo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean smiledetected = intent.getBooleanExtra("SmileDetected", false);
            String playlist = intent.getStringExtra("Playlist");

            if(smiledetected){
                long play_id_accessed = get_playlist_id(playlist);
                getList_for_playlist(play_id_accessed);
                musicSrv.setList(playList);
                musicSrv.setPlaylistId(play_id_accessed);
                musicSrv.setAutoPlayAll(true);
                musicSrv.playSong();
            }
        }
    };*/

    private long play_id_for_auto_play = -1;

    public long get_playlist_id(String name) {

        //get the play;ist id of just created playlist using its name
        ContentResolver resolver = getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] proj = {
                        MediaStore.Audio.Playlists.NAME,
                        MediaStore.Audio.Playlists._ID
                        };

        if (play_id_for_auto_play == -1) {

            Cursor cursr = resolver.query(uri, proj, null, null, null);


            if (cursr != null && cursr.moveToFirst()) {
                //get columns

                int ci = cursr.getColumnIndexOrThrow
                        (MediaStore.Audio.Playlists.NAME);
                int id = cursr.getColumnIndexOrThrow
                        (MediaStore.Audio.Playlists._ID);

                do {
                    String d = cursr.getString(ci);
                    if (d.equals(name)) {
                        play_id_for_auto_play = cursr.getLong(id);
                        Log.i("TAG", "  " + play_id_for_auto_play);
                        break;

                    } else {
                        continue;
                    }
                }
                while (cursr.moveToNext());
                cursr.close();
            }
        }

        return play_id_for_auto_play;
    }

    private ArrayList<Song>  playList = new ArrayList<Song>();

    public void getList_for_playlist(long playlistId) {


        //retrieve song info
        ContentResolver musicResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        Cursor musicCursor = musicResolver.query(uri, null, null, null, null);

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        //iteration over results
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns


            int playstsongColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.DISPLAY_NAME);
            int playstsongartistColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.ARTIST);
            int trial = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Playlists.Members.AUDIO_ID);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);

            do {
                //add songs to list
//for no of songs in a genre
                String playsongtitle = musicCursor.getString(playstsongColumn);
                String playsongrtist = musicCursor.getString(playstsongartistColumn);
                String trialid = musicCursor.getString(trial);

                long id = musicCursor.getLong(idColumn);

                long albumId = musicCursor.getLong(musicCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_ID));


                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

               /* String thisAlbum = musicCursor.getString(albumColumn);*/
                playList.add(new Song(playlistId, null, -1, playsongtitle, playsongrtist, trialid, id,albumArtUri));


            }
            while (musicCursor.moveToNext());

            musicCursor.close();
        }




    }



}  //class ends
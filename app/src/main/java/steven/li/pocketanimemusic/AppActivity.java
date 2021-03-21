package steven.li.pocketanimemusic;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import API.AnimeThemeAPI;
import Model.Anime;
import Model.MusicPlayerModel;
import Model.Song;
import steven.li.pocketanimemusic.service.mediaplayer.MusicPlayerService;
import steven.li.pocketanimemusic.ui.Browse.BrowseFragment;
import steven.li.pocketanimemusic.ui.List.MyListFragment;
import steven.li.pocketanimemusic.ui.Play.MusicPlayerFragment;

public class AppActivity extends AppCompatActivity implements AnimeThemeAPI.OnAnimesListener {

    Fragment listFragment;
    Fragment playFragment;
    Fragment browseFragment;
    Toolbar actionBar;
    private AnimeViewModel model;

    private MusicPlayerService musicPlayer;
    private Intent musicPlayerIntent;
    private boolean serviceBound = false;

    private Handler handler;
    private Runnable musicRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        listFragment = new MyListFragment();
        browseFragment = new BrowseFragment();
        playFragment = new MusicPlayerFragment();
        // Init the first fragment as listFragment
        loadFragment(listFragment);
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        // Set the modelView to set the animeList later
        model = new ViewModelProvider(this).get(AnimeViewModel.class);
        // Set the view model where the information of music player is stored
        new ViewModelProvider(this).get(MusicPlayerViewModel.class);

        // On the select listener, add the method that will change the current fragment
        navView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));

        /*
        Default configuration from BottomNavActivity
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_list, R.id.navigation_play, R.id.navigation_browse)
                .build();
        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        */

        // Get the the name which will use in the API
        String pseudo = getIntent().getStringExtra("anilistName");
        // Call the API to get the anilist list of the user
        new AnimeThemeAPI(pseudo,this).execute();
        // Register Notification event
        registerNotificationPlayReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(musicPlayerIntent == null){
            musicPlayerIntent = new Intent(this,MusicPlayerService.class);
            bindService(musicPlayerIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicPlayerIntent);
        }
        musicRun = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                        if(musicPlayer.isPlaying()){
                            MusicPlayerViewModel mpV =  new ViewModelProvider(AppActivity.this).get(MusicPlayerViewModel.class);
                            mpV.setCurrentPosition(musicPlayer.getPosition());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        new Thread(musicRun).start();
    }

    private boolean updateMainFragment(int itemId) {
        Fragment fragment = null;
        switch (itemId){
            case R.id.navigation_list:
                fragment = listFragment;
                break;
            case R.id.navigation_browse:
                fragment = browseFragment;
                break;
            case R.id.navigation_play:
                fragment = playFragment;
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onAnimesCompleted(ArrayList<Anime> animeList) {
        model.setAnimeList(animeList);
    }


    private ServiceConnection musicServiceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
            musicPlayer = binder.getService();
            serviceBound = true;
            Toast.makeText(AppActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = true;
            Toast.makeText(AppActivity.this, "Service Unbound", Toast.LENGTH_SHORT).show();
        }
    };

    private BroadcastReceiver notificationPlay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MusicPlayerModel mpModel = (MusicPlayerModel) intent.getSerializableExtra(MusicPlayerModel.INTENT_PARAM);
            new ViewModelProvider(AppActivity.this).get(MusicPlayerViewModel.class).setMusicPlayer(mpModel);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationPlay);
    }

    private void registerNotificationPlayReceiver(){
        IntentFilter filter = new IntentFilter(MusicPlayerService.NOTIFICATION_PLAY);
        registerReceiver(notificationPlay, filter);
    }
}
package steven.li.pocketanimemusic;

import android.content.Intent;

import android.os.Bundle;
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
}
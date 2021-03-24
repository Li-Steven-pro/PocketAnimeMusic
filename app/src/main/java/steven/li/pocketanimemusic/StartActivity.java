package steven.li.pocketanimemusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.util.List;

import API.AnilistAPI;
import API.AnimeThemesMoeAPI;
import API.JikanAPI;
import API.ThemeMoeAPI;
import Model.Anime;
import Model.MalAnime;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button getAnilistBtn;
    EditText anilistPseudo;
    List<MalAnime> malAnimeList;
    List<Anime> animeList;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getAnilistBtn = findViewById(R.id.getAnilistPseudoBtn);
        // Set listener
        getAnilistBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Get user input
        anilistPseudo = findViewById(R.id.anilistPseudo);
        // Send request and check if the user exists
        /*
        Test API
        new AnimeThemesMoeAPI(this).search("Bakemonogatari", new AnimeThemesMoeAPI.OnAnimesListener() {
            @Override
            public void onAnimesCompleted(List<Anime> list) {
                animeList = list;
            }
        });

        new ThemeMoeAPI(this).getAniList("Railex", new ThemeMoeAPI.OnThemeMoeListener() {
            @Override
            public void onListCompleted(List<MalAnime> animes) {
                malAnimeList = animes;
            }
        });
        */
        new AnilistAPI(this).checkUserExist(anilistPseudo.getText().toString());
    }


}
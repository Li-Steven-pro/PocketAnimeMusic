package steven.li.pocketanimemusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import API.AnilistAPI;
import API.AnimeThemeAPI;

public class MainActivity extends AppCompatActivity {
    Button buttonEvent;
    AnilistAPI anilistAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*buttonEvent = findViewById(R.id.button);
        ListView listView = findViewById(R.id.list);
        VideoView videoView = findViewById(R.id.videoView);
        buttonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user inputs
                EditText textPseudo = findViewById(R.id.pseudoAnilist);
                AnilistAPI anilistAPI = new AnilistAPI(getApplicationContext());
                anilistAPI.getUserList(textPseudo.getText().toString());

                AnimeThemeAPI animeThemeAPI = new AnimeThemeAPI(textPseudo.getText().toString(),listView,videoView);
                animeThemeAPI.execute();
            }
        });*/
    }


}
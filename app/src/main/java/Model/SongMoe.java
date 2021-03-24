package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongMoe {
    private int id;
    private String title;
    private List<Artist> artists;

    public SongMoe(){ }

    public void setFromAnimeThemesMoe(JSONObject song) {
        try {
            this.id = song.getInt("id");
            this.title = song.getString("title");
            this.artists = new ArrayList<>();
            JSONArray jArray = (JSONArray) song.getJSONArray("videos");
            for(int i = 0; i < jArray.length() ; i++){
                Artist artist = new Artist();
                artist.setFromAnimeThemesMoe((JSONObject) jArray.get(i));
                artists.add(artist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

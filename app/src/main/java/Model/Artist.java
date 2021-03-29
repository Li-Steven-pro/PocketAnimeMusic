package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private int id;
    private String name;
    private String cover;
    private List<Anime> animes;
    public Artist(){ }

    public void setFromAnimeThemesMoe(JSONObject artist) {
        try {
            this.id = artist.getInt("id");
            this.name = artist.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFromAnimeTheme(JSONObject artist){
        try{
            this.id = artist.getInt("artist_id");
            this.name = artist.getString("name");
            this.cover = artist.getString("cover");
            animes = new ArrayList<>();
            JSONArray jArray = (JSONArray) artist.getJSONArray("title");
            if (jArray != null){
                for(int i = 0; i < jArray.length() ; i++){
                    animes.add(new Anime((JSONObject) jArray.getJSONObject(i)));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

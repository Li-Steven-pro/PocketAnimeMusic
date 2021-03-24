package Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Artist {
    private int id;
    private String name;

    public Artist(){ }

    public void setFromAnimeThemesMoe(JSONObject artist) {
        try {
            this.id = artist.getInt("id");
            this.name = artist.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

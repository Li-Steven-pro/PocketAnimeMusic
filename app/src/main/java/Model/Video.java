package Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Video {
    private int id;
    private String basename;
    private String link;

    public Video(){ }

    public void setFromAnimeThemesMoe(JSONObject video){
        try {
            this.id = video.getInt("id");
            this.basename = video.getString("basename");
            this.link = video.getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

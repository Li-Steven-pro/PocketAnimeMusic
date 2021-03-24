package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MalAnime {
    int mal_id;
    String url;
    String image_url;
    String title;
    HashMap<String, SimpleSong> themes;

    public MalAnime(){ }

    public void setFromThemeMoe(JSONObject malanime){
        try {
            this.mal_id =  malanime.getInt("malID");
            this.title = malanime.getString("name");
            this.themes = new HashMap<>();
            JSONArray jArray = (JSONArray) malanime.getJSONArray("themes");
            for(int i = 0; i < jArray.length() ; i++){
                JSONObject theme = jArray.getJSONObject(i);
                String name = theme.getString("themeName");
                String type = theme.getString("themeType");
                String link = theme.getJSONObject("mirror").getString("mirrorURL");
                SimpleSong song = new SimpleSong(name, type, link);
                themes.put(name, song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public int getMal_id() {
        return mal_id;
    }

    public void setMal_id(int mal_id) {
        this.mal_id = mal_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

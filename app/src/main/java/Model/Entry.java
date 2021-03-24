package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Entry {
    private int id;
    private String episodes;
    private List<Video> videos;

    public Entry(){ }

    public void setFromAnimeThemesMoe(JSONObject entry) {
        try {
            this.id = entry.getInt("id");
            this.episodes = entry.getString("episodes");
            this.videos = new ArrayList<Video>();
            JSONArray jArray = (JSONArray) entry.getJSONArray("videos");
            for(int i = 0; i < jArray.length() ; i++){
                Video video = new Video();
                video.setFromAnimeThemesMoe((JSONObject) jArray.get(i));
                videos.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

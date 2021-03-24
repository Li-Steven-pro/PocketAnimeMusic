package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Theme {
    public static final String OP = "OP";
    public static final String ED = "ED";
    private int id;
    private int sequence;
    private String type;
    private List<SongMoe> songs;
    private List<Entry> entries;

    public Theme(){
    }


    public void setFromAnimeThemesMoe(JSONObject theme) {

        try {
            this.id = theme.getInt("id");
            this.type = theme.getString("type");
            this.sequence = theme.getInt("sequence");
            songs = new ArrayList<>();

            JSONArray jArray = theme.getJSONArray("song");
            for(int i = 0; i < jArray.length() ; i++){
                SongMoe song = new SongMoe();
                song.setFromAnimeThemesMoe((JSONObject) jArray.get(i));
                songs.add(song);
            }

            entries = new ArrayList<>();
            jArray = theme.getJSONArray("entries");
            for(int i = 0; i < jArray.length() ; i++){
                Entry entry = new Entry();
                entry.setFromAnimeThemesMoe((JSONObject) jArray.get(i));
                entries.add(entry);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

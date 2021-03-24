package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Anime implements Serializable {
    private int mal_id;
    private List<String> titles;
    private String imageURL;
    private int year;
    private List<Song> listSong;
    private List<Theme> themes;

    public int getMal_id() {
        return mal_id;
    }

    public List<String> getTitles() {
        return titles;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getYear() {
        return year;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public List<Theme> getThemes(){
        return themes;
    }


    public Anime(){}
    public Anime(JSONObject jsonobject){
        titles = new ArrayList<String>();
        listSong = new ArrayList<Song>();
        try {
            //mal id
            mal_id = jsonobject.getInt("mal_id");

            // Titles
            JSONArray jArray = (JSONArray) jsonobject.getJSONArray("title");
            if (jArray != null){
                for(int i = 0; i < jArray.length() ; i++){
                    titles.add(jArray.getString(i));
                }
            }

            // imageURL
            imageURL = jsonobject.getString("cover");

            // year
            year = jsonobject.getInt("year");

            // list song
            JSONArray jArraySong = (JSONArray) jsonobject.getJSONArray("themes");
            if (jArraySong != null){
                for(int i = 0; i < jArraySong.length() ; i++){
                    listSong.add(new Song((JSONObject) jArraySong.get(i)));
                }
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void setFromAnimeThemesMoe(JSONObject anime){
        try {
            mal_id = anime.getInt("id");
            titles = new ArrayList<String>();
            titles.add(anime.getString("name"));
            year = anime.getInt("year");

            themes = new ArrayList<Theme>();
            JSONArray jArrayThemes = (JSONArray) anime.getJSONArray("themes");
            if (jArrayThemes != null){
                for(int i = 0; i < jArrayThemes.length() ; i++){
                    Theme theme = new Theme();
                    theme.setFromAnimeThemesMoe((JSONObject) jArrayThemes.get(i));
                    themes.add(theme);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle (){
        return titles.get(0);
    }

}

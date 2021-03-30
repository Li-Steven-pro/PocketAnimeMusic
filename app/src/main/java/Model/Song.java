package Model;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Song implements Serializable {

    String id;
    String title;
    String type;
    String artist;
    List<Link> listLink = new ArrayList<Link>();

     public Song(JSONObject jsonObject){
        try{
            id = jsonObject.getString("theme_id");
            title = jsonObject.getString("title");
            type = jsonObject.getString("type");
            artist = jsonObject.getString("artist");
            JSONArray jArrayLink = (JSONArray) jsonObject.getJSONArray("mirrors");
            if (jArrayLink != null){
                for(int i = 0; i < jArrayLink.length() ; i++){
                    listLink.add(new Link((JSONObject) jArrayLink.get(i)));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getWebmURL(){
        if (listLink != null){
            return listLink.get(0).getWebmLink();
        }
        return "";
    }

    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj.getClass() != this.getClass()){
            return false;
        }else{
            if (((Song) obj).getTitle().equals(this.getTitle()) && ((Song) obj).getArtist().equals(this.getArtist())){
                return true;
            }
            else{
                return false;
            }
        }
    }
}


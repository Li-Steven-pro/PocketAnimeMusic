package Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Link implements Serializable {

    private String quality;
    private String webmLink;
    private String audioLink;

    public String getQuality() {
        return quality;
    }

    public String getWebmLink() {
        return webmLink;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public Link(JSONObject jsonObject){
        try{
            quality = jsonObject.getString("quality");
            webmLink = jsonObject.getString("mirror");
            audioLink = jsonObject.getString("audio");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

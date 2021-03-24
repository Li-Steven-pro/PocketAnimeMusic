package API;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.Anime;
import Model.Artist;
import steven.li.pocketanimemusic.AppActivity;

public class AnimeThemesMoeAPI
{
    private final Context context;
    /**
     * Implement this method as callback when calling the AnimeThemeAPI
     */
    
    OnAnimesListener animesListener;
    public interface OnAnimesListener{
        void onAnimesCompleted(List<Anime> animeList);
    }

    String base_url = "https://staging.animethemes.moe/api/";

    public AnimeThemesMoeAPI(Context context){
        this.context = context;
    }

    public void search(String name, OnAnimesListener listener){
        String url = base_url + "search?q="+name;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // The user exists and send pseudo using intent and begin the main activity
                try {
                    JSONArray results = response.getJSONObject("search").getJSONArray("anime");
                    List<Anime> animes = new ArrayList<>();
                    for(int i = 0; i < results.length() ; i++){
                        Anime anime = new Anime();
                        anime.setFromAnimeThemesMoe((JSONObject) results.get(i));
                        animes.add(anime);
                    }
                    listener.onAnimesCompleted(animes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show the user that he puts invalid anilist pseudo
                Toast.makeText(context, "Anime Theme moe api", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}

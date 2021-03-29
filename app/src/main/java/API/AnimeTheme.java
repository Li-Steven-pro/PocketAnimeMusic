package API;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.Anime;
import Model.Artist;

public class AnimeTheme {

    public interface OnAnimeThemeListener{
        void onAnilistCompleted(List<Anime> animes);
        void onAnimeCompleted(Anime anime);
        void onArtistCompleted(Artist artist);
        void onSearchAnimeCompleted(List<Anime> animes);
    }

    String base_url = "https://animethemes-api.herokuapp.com/api/v1/";
    Context context;
    RequestQueue requestQueue;

    public AnimeTheme(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getAniList(String name, AnimeTheme.OnAnimeThemeListener listener){
        String url = base_url + "anilist/"+name;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // The user exists and send pseudo using intent and begin the main activity
                try {
                    List<Anime> animes = new ArrayList<>();
                    for(int i = 0; i < response.length() ; i++){
                        Anime anime = new Anime((JSONObject) response.get(i));
                        animes.add(anime);
                    }
                    listener.onAnilistCompleted(animes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show the user that he puts invalid anilist pseudo
                Toast.makeText(context, "Anilist user not found!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getAnime(int id, AnimeTheme.OnAnimeThemeListener listener){
        String url = base_url + "anime/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // The user exists and send pseudo using intent and begin the main activity
               Anime anime = new Anime((JSONObject) response);
               listener.onAnimeCompleted(anime);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show the user that he puts invalid anilist pseudo
                Toast.makeText(context, "Can't get anime", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void searchAnime(String name, AnimeTheme.OnAnimeThemeListener listener){
        String url = base_url + "search/anime/" + name;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // The user exists and send pseudo using intent and begin the main activity
                try {
                    List<Anime> animes = new ArrayList<>();
                    for(int i = 0; i < response.length() ; i++){
                        Anime anime = new Anime((JSONObject) response.get(i));
                        animes.add(anime);
                    }
                    listener.onSearchAnimeCompleted(animes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show the user that he puts invalid anilist pseudo
                Toast.makeText(context, "Anilist user not found!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getArtist(int id, AnimeTheme.OnAnimeThemeListener listener){
        String url = base_url + "artist/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // The user exists and send pseudo using intent and begin the main activity
                Artist artist = new Artist();
                artist.setFromAnimeTheme((JSONObject) response);
                listener.onArtistCompleted(artist);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show the user that he puts invalid anilist pseudo
                Toast.makeText(context, "Can't get artist", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

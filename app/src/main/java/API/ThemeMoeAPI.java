package API;

import android.content.Context;
import android.os.AsyncTask;
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
import Model.MalAnime;

public class ThemeMoeAPI
{
    AnimeThemesMoeAPI.OnAnimesListener animesListener;
    public interface OnThemeMoeListener{
        void onListCompleted(List<MalAnime> animes);
    }

    String base_url = "https://themes.moe/api/";
    Context context;

    public ThemeMoeAPI(Context context){
        this.context = context;
    }

    public void getAniList(String name, ThemeMoeAPI.OnThemeMoeListener listener){
        String url = base_url + "anilist/"+name;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // The user exists and send pseudo using intent and begin the main activity
                try {
                    List<MalAnime> animes = new ArrayList<>();
                    for(int i = 0; i < response.length() ; i++){
                        MalAnime anime = new MalAnime();
                        anime.setFromThemeMoe((JSONObject) response.get(i));
                        animes.add(anime);
                    }
                    listener.onListCompleted(animes);
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
        requestQueue.add(jsonObjectRequest);
    }

}

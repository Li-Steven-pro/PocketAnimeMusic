package API;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import steven.li.pocketanimemusic.AppActivity;

public class AnilistAPI {

    String url;

    Context context;

    public AnilistAPI(Context context) {
        this.context = context;
        this.url = "https://graphql.anilist.co";
    }

    public void checkUserExist(String anilistUser){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        JSONObject variables = new JSONObject();
        String query;
        try {
            query = "query ($name: String ) {\n" +
                    "  User(name : $name,){\n" +
                    "    id\n"+
                    "  }\n" +
                    "}";
            variables.put("name", anilistUser);
            postData.put("query", query);
            postData.put("variables", variables);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("process Query");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Intent i = new Intent(context, AppActivity.class);
                i.putExtra("anilistName", anilistUser);
                context.startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Anilist user not found!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getUserList(String anilistUser) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        JSONObject variables = new JSONObject();
        String query;
        try {
            query = "query ($userName: String ) {\n" +
                    "  MediaListCollection(userName : $userName, type : ANIME){\n" +
                    "    lists {\n" +
                    "       name\n" +
                    "       entries {\n" +
                    "           media {\n" +
                    "               title {\n" +
                    "                   userPreferred \n" +
                    "               }\n" +
                    "           }\n" +
                    "       }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            variables.put("userName", anilistUser);
            postData.put("query", query);
            postData.put("variables", variables);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}

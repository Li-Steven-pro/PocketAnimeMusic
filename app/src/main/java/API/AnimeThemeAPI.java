package API;

import android.os.AsyncTask;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Model.Anime;
import steven.li.pocketanimemusic.AnimeListAdapter;

public class AnimeThemeAPI extends AsyncTask<String, Void, JSONObject>
{
    public interface OnAnimesListener{
        void onAnimesCompleted(ArrayList<Anime> animeList);
    }

    URL url;
    ArrayList<Anime> animeList;
    private OnAnimesListener mListener;

    public AnimeThemeAPI(String pseudo, OnAnimesListener listener){
        mListener = listener;
        try {
            this.url = new URL("https://animethemes-api.herokuapp.com/api/v1/anilist/"+ pseudo);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        HttpURLConnection urlConnection;
        JSONObject json = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String s = readStream(in);
            json = new JSONObject(s);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Success get json");
        return json;
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Convert it as data
        sb.append("{\"data\":");
        BufferedReader r = new BufferedReader(new InputStreamReader(is),10000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
            System.out.println(line);
        }
        sb.append('}');
        is.close();
        return sb.toString();
    }
    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        animeList = new ArrayList<Anime>();
        Anime anime;
        try {
            // For each object in the json
            for(int i = 0; i< ((JSONArray) jsonObject.getJSONArray("data")).length(); i++)
            //for(int i = 0; i< 3; i++)
            {
                anime = new Anime((JSONObject) jsonObject.getJSONArray("data").get(i));
                animeList.add(anime);
                // Add url in the adaptor vector
                //animeListAdapter.add((anime));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mListener != null){
            mListener.onAnimesCompleted(animeList);
        }
    }


}

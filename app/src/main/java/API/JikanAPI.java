package API;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.MalAnime;

public class JikanAPI extends AsyncTask<String, Void, JSONObject>{

    public interface OnMalAnimesListener{
        void onMalAnimesCompleted(ArrayList<MalAnime> animeList);
    }

    URL url;
    // List anime
    ArrayList<MalAnime> animeList;
    String base_url = "https://api.jikan.moe/v3";
    private JikanAPI.OnMalAnimesListener mListener;


    public JikanAPI(JikanAPI.OnMalAnimesListener listener){
        this.mListener = listener;
    }

    private JSONObject doRequest (String url){
        HttpURLConnection urlConnection;
        JSONObject json = null;
        try {
            // Classic http request
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String s = readStream(in);
            System.out.println(s);
            json = new JSONObject(s);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Convert it as Json not an array of Json
        BufferedReader r = new BufferedReader(new InputStreamReader(is),10000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public void getAnimeById(int id) throws JSONException {
        String url = base_url + "/anime/"+ Integer.toString(id);
        JSONObject jsonObject = doRequest(url);
        String op = jsonObject.getString("opening_themes");
        List<String> ops = Arrays.asList(op.substring(1,op.length()-1).split(",")) ;
        ops.forEach(System.out::println);
        String ed = jsonObject.getString("ending_themes");
        List<String> eds = Arrays.asList(ed.substring(1,ed.length()-1).split(","));
        eds.forEach(System.out::println);
    }

    public ArrayList<MalAnime> search(String title){
        
        String url = base_url + "/search/anime?q="+this.requestFormaTitle(title)+"&page=1";
        JSONObject jsonObject = doRequest(url);
        ArrayList<MalAnime> list = new ArrayList<MalAnime>();
        try {
            JSONArray results = jsonObject.getJSONArray("results");;
            for(int i = 0; i< results.length(); i++){
                JSONObject object = results.getJSONObject(i);
                MalAnime malAnime = new MalAnime(object.getInt("mal_id"));
                malAnime.setImage_url(object.getString("image_url"));
                malAnime.setTitle(object.getString("title"));
                malAnime.setUrl(object.getString("url"));
                list.add(malAnime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private String requestFormaTitle(String title){
        String[] strgs = title.split(" ");
        return TextUtils.join("%20", strgs);
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        if(mListener != null){
            // Execute the callback when the list is completed
            mListener.onMalAnimesCompleted(this.search("Full metal alchemist"));
        }
        return null;
    }
}

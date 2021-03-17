package steven.li.pocketanimemusic;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.List;

import Model.Anime;

public class AnimeListAdapter extends ArrayAdapter<Anime> {
    List<Anime> animeList;
    public AnimeListAdapter(Context context, ArrayList<Anime> animeList) {
        super(context, 0, animeList);
        this.animeList = animeList;
    }

    @Override
    public int getCount() {
        return animeList.size();
    }

    @Override
    public Anime getItem(int position) {
        return animeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the layout for image using inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_view, parent, false);

        Anime anime = getItem(position);
        // Get the imageView from the layout
        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView label = convertView.findViewById(R.id.text_label);
        label.setText(anime.getTitle());
        // Set the request queue that will handle our multiple images request
        RequestQueue queue = MySingleton.getInstance(parent.getContext()).getRequestQueue();
        // Set an request using the vector to get the image and set in the imageView
        String link = anime.getImageURL();
        ImageRequest request = new ImageRequest(link,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        // Add the image request in the queue
        queue.add(request);
        return convertView;

    }

    public void add(Anime anime)
    {
        animeList.add(anime);
    }
}
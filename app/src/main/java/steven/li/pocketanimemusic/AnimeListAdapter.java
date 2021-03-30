package steven.li.pocketanimemusic;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.List;

import Model.Anime;
import Model.Song;
import steven.li.pocketanimemusic.service.mediaplayer.MusicPlayerService;

public class AnimeListAdapter extends ArrayAdapter<Anime> {
    List<Anime> animeList;
    FragmentActivity activity;
    Context context;
    public AnimeListAdapter(Context context, ArrayList<Anime> animeList, FragmentActivity activity) {
        super(context, 0, animeList);
        this.animeList = animeList;
        this.activity = activity;
        this.context = context;

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

        TextView label = convertView.findViewById(R.id.text_label);
        label.setText(anime.getTitle());

        // Layout for list song
        LinearLayout listView = convertView.findViewById(R.id.list_song_view);

        // Toggle list song
        ImageButton imageButton = convertView.findViewById(R.id.btn_detail);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility() == View.GONE){
                    listView.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.GONE);
                }
            }
        });

        // Get the imageView from the layout
        ImageView imageView = convertView.findViewById(R.id.image_view);
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
        // Set song view in the layout
        renderlistsongView(inflater, parent, listView, anime.getListSong());
        return convertView;

    }

    /**
     * Put song list in the view inflated for each one
     */
    private void renderlistsongView(LayoutInflater inflater, ViewGroup parent, LinearLayout linearLayout, List<Song> songs){
        for(Song song : songs){
            View inflatedView = inflater.inflate(R.layout.item_song_layout, parent, false);
            TextView titleLabel = inflatedView.findViewById(R.id.item_song_name);
            titleLabel.setText(song.getTitle());
            TextView artistLabel = inflatedView.findViewById(R.id.item_artist_name);
            artistLabel.setText(song.getArtist());
            linearLayout.addView(inflatedView);
            ImageButton imageButton = inflatedView.findViewById(R.id.add_playlist);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_ADD_PLAYLIST);
                    broadcastIntent.putExtra(MusicPlayerService.INTENT_BROADCAST_ADD, song);
                    activity.sendBroadcast(broadcastIntent);
                }
            });
        }
    }

    public void add(Anime anime)
    {
        animeList.add(anime);
    }
}
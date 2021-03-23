package steven.li.pocketanimemusic;

import android.content.Context;
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

public class PlayListAdapter extends ArrayAdapter<Song> {
    List<Song> songs;
    FragmentActivity activity;
    Context context;
    public PlayListAdapter(Context context, List<Song> songs, FragmentActivity activity){
        super(context, 0, songs);
        this.songs = songs;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Song getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the layout for image using inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.playlist_songs_item, parent, false);
        Song song = getItem(position);

        TextView titleLabel = convertView.findViewById(R.id.card_song_title);
        titleLabel.setText(song.getTitle());

        TextView artistLabel = convertView.findViewById(R.id.card_song_artist);
        artistLabel.setText(song.getArtist());
        // Toggle list song
        ImageButton imageButton = convertView.findViewById(R.id.btn_detail);
        // Add the image request in the queue
        return convertView;

    }
}

package steven.li.pocketanimemusic;

import android.view.View;
import android.widget.TextView;

import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import Model.Song;

public class PlaylistRecyclerAdapter extends DragDropSwipeAdapter<Song, PlaylistRecyclerAdapter.PlaylistViewHolder> {

    public class PlaylistViewHolder extends DragDropSwipeAdapter.ViewHolder{
        private TextView name, author;

        public PlaylistViewHolder(@NotNull View layout) {
            super(layout);
            this.name = layout.findViewById(R.id.card_song_title);
            this.author = layout.findViewById(R.id.card_song_artist);
        }

        public TextView getName() {
            return name;
        }
        public TextView getAuthor() {
            return author;
        }
    }

    public PlaylistRecyclerAdapter(){
        this.setDataSet(new ArrayList<>());
    }

    @NotNull
    @Override
    protected PlaylistViewHolder getViewHolder(@NotNull View view) {
        return new PlaylistViewHolder(view);
    }

    @Nullable
    @Override
    protected View getViewToTouchToStartDraggingItem(Song song, @NotNull PlaylistViewHolder playlistViewHolder, int i) {
        return null;
    }

    @Override
    protected void onBindViewHolder(Song song, @NotNull PlaylistViewHolder viewHolder, int i) {
        viewHolder.getName().setText(song.getTitle());
        viewHolder.getAuthor().setText(song.getArtist());
    }

}

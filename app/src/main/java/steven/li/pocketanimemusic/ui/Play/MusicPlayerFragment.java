package steven.li.pocketanimemusic.ui.Play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.concurrent.TimeUnit;

import Model.MusicPlayerModel;

import steven.li.pocketanimemusic.AppActivity;
import steven.li.pocketanimemusic.MusicPlayerViewModel;
import steven.li.pocketanimemusic.PlaylistRecyclerAdapter;
import steven.li.pocketanimemusic.R;
import steven.li.pocketanimemusic.service.mediaplayer.MusicPlayerService;
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView;
public class MusicPlayerFragment extends Fragment {

    private TextView titleLabel, artistLabel;
    private TextView playerPosition, playerDuration;
    private SeekBar seekBar;
    private ImageView btnPrev, btnPlay, btnPause, btnNext, btnShuffle, btnRepeat;
    // DragDropSwipeRecycler for playlist
    private DragDropSwipeRecyclerView playlistView;

    // Adapter for the list of songs
    private PlaylistRecyclerAdapter playListRecyclerAdapter;

    private MusicPlayerViewModel mpModel;
    private AppActivity mActivity;

    private BottomSheetBehavior behavior;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        // Load components
        titleLabel = view.findViewById(R.id.mp_song_title);
        artistLabel = view.findViewById(R.id.mp_song_artist);
        playerPosition = view.findViewById(R.id.player_position);
        playerDuration = view.findViewById(R.id.player_duration);
        seekBar = view.findViewById(R.id.seekBar);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrev = view.findViewById(R.id.btn_prev);
        btnPlay = view.findViewById(R.id.btn_play);
        btnPause = view.findViewById(R.id.btn_pause);
        btnShuffle = view.findViewById(R.id.btn_shuffle);
        btnRepeat = view.findViewById(R.id.btn_repeat);
        playlistView = view.findViewById(R.id.playlist_recycler);

        // Initialize Adaptater for playlist
        playListRecyclerAdapter = new PlaylistRecyclerAdapter();
        playlistView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        playlistView.setAdapter(playListRecyclerAdapter);

        // Load view model for music player
        mpModel = new ViewModelProvider(requireActivity()).get(MusicPlayerViewModel.class);
        // Add observer to update ui
        mpModel.getMusicPlayer().observe(getViewLifecycleOwner(), item -> {
            titleLabel.setText(item.getSong().getTitle());
            artistLabel.setText(item.getSong().getArtist());
            playerPosition.setText(converFormat(item.getResumePosition()));

            // Set the playlist
            playListRecyclerAdapter.setDataSet(item.getSongList());
            playListRecyclerAdapter.notifyDataSetChanged();

            if(item.getLooping()){
                btnRepeat.setImageResource(R.drawable.ic_baseline_repeat_on_24);
            }else{
                btnRepeat.setImageResource(R.drawable.ic_baseline_repeat_24);
            }
            if(item.getShuffle()){
                btnShuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
            }else{
                btnShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
            }
            switch (item.getStatus()){
                case MusicPlayerModel.BUFFERING:
                    Toast.makeText(mActivity, "Buffering " + item.getSong().getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                case MusicPlayerModel.PAUSE:
                    btnPause.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    Toast.makeText(mActivity, "Pausing " + item.getSong().getTitle(), Toast.LENGTH_SHORT).show();
                    seekBar.setMax(item.getDuration());
                    playerDuration.setText(converFormat(item.getDuration()));
                    break;
                case MusicPlayerModel.PLAYING:
                    btnPause.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.INVISIBLE);
                    Toast.makeText(mActivity, "Playing " + item.getSong().getTitle(), Toast.LENGTH_SHORT).show();
                    seekBar.setMax(item.getDuration());
                    playerDuration.setText(converFormat(item.getDuration()));
                    break;
            }

        });
        // Observer for the seekar
        mpModel.getCurrectPosition().observe(getViewLifecycleOwner(), item ->{
            seekBar.setProgress(item);
            playerPosition.setText(converFormat(item));
        });

        // Add bottomSheet for the playlist
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        /*
        TODO : Solve the issues with the seekTo feature
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_SEEK_TO);
                broadcastIntent.putExtra(MusicPlayerService.INTENT_BROADCAST_SEEK_TO, progress);
                mActivity.sendBroadcast(broadcastIntent);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });*/
        // Set the event play on the button play
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_PLAY);
                mActivity.sendBroadcast(broadcastIntent);
            }
        });
        // Set the event skip song on the button next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_NEXT);
                mActivity.sendBroadcast(broadcastIntent);
            }
        });

        // Set the event play previous song on the button prev
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_PREV);
                mActivity.sendBroadcast(broadcastIntent);
            }
        });

        // Set the event pause song on the button pause
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_PAUSE);
                mActivity.sendBroadcast(broadcastIntent);
            }
        });

        // Set the event toggle repeat song on the button repeat
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_TOGGLE_REPEAT);
                mActivity.sendBroadcast(broadcastIntent);
            }
        });

        // Set the event toggle shuffle songs on the button shuffle
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(MusicPlayerService.BROADCAST_TOGGLE_SHUFFLE);
                mActivity.sendBroadcast(broadcastIntent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AppActivity){
            mActivity = (AppActivity) context;
        }
    }

    /**
     * Convert the mediaplayer duration output into readable duration "00:00", "1:30"
     * @param duration
     * @return readable format
     */
    private String converFormat(int duration) {
        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        long seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES);

        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //menu.clear();
        //inflater.inflate(R.menu.app_bar_menu2, menu);
    }

}
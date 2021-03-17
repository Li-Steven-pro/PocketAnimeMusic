package steven.li.pocketanimemusic.ui.Play;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import steven.li.pocketanimemusic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView playerPosition, playerDuration;
    private SeekBar seekBar;
    private ImageView btnPrev, btnPlay, btnPause, btnNext;

    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Handler handler = new Handler();
    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicPlayerFragment newInstance(String param1, String param2) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try{
            mediaPlayer.setDataSource("https://animethemes.moe/video/100Man-OP1.webm");
            mediaPlayer.prepareAsync();
        }catch (IOException e){
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }
        });
        /*
        runnable = new Runnable(){
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };
        int duration = mediaPlayer.getDuration();
        String sDuration = converFormat(duration);
        playerDuration.setText(sDuration);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
            }
        });

        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private String converFormat(int duration){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration),
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        playerPosition = view.findViewById(R.id.player_position);
        playerDuration = view.findViewById(R.id.player_duration);
        seekBar = view.findViewById(R.id.seekBar);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrev = view.findViewById(R.id.btn_prev);
        btnPlay = view.findViewById(R.id.btn_play);
        btnPause = view.findViewById(R.id.btn_pause);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
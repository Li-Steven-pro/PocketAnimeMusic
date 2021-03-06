package steven.li.pocketanimemusic.service.mediaplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import Model.MusicPlayerModel;
import Model.Song;

interface IMusicPlayerService{
    void stop();
    void play();
    void pause();
    int getDuration();
    int getPosition();
    void seekTo(int pos);
    void next();
    void prev();
    void playNow(Song song);
    boolean isPlaying();

}
public class MusicPlayerService extends Service implements IMusicPlayerService,MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener {

    // Set all Broadcast Event Receiver Name
    public static final String BROADCAST_ADD_PLAYLIST = "pocketanimemusic.musicplayer.add_playlist";
    public static final String BROADCAST_PLAY = "pocketanimemusic.musicplayer.play";
    public static final String BROADCAST_NEXT = "pocketanimemusic.musicplayer.next";
    public static final String BROADCAST_PREV = "pocketanimemusic.musicplayer.prev";
    public static final String BROADCAST_STOP = "pocketanimemusic.musicplayer.stop";
    public static final String BROADCAST_PAUSE = "pocketanimemusic.musicplayer.pause";
    public static final String BROADCAST_SEEK_TO = "pocketanimemusic.musicplayer.seek_to";
    public static final String INTENT_BROADCAST_SEEK_TO = "pocketanimemusic.musicplayer.seek_to.intent_param";
    public static final String BROADCAST_TOGGLE_REPEAT = "pocketanimemusic.musicplayer.repeat";
    public static final String BROADCAST_TOGGLE_SHUFFLE = "pocketanimemusic.musicplayer.shuffle";

    // Broadcast SENT
    public static final String NOTIFICATION_STATUS = "pocketanimemusic.musicplayer.notify_status";

    // STATE (BUFFERING, PLAYING, PAUSING)
    private int status;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> listSong;
    // Position in the queue
    private int position = 0;
    // Position in the song
    private int resumePosition = 0;
    // TODO : Implement shuffle features
    private boolean shuffle = false;
    private AudioManager audioManager;

    /**
     * Bind the service
     */
    public class MusicBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
    private final IBinder iBinder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCreate(){
        initMusicPlayer();
    }

    /**
     * Prepare the mediaPlayer to receive songs
     * Set broadcast receivers
     */
    private void initMusicPlayer(){
        mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        listSong = new ArrayList<Song>();
        status = MusicPlayerModel.BUFFERING;
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.reset();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        registerAllBroadcastReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unregisterAllBroadcastReceiver();
    }

    public void setListSong(ArrayList<Song> songs){
        listSong = songs;
    }

    public void addToListSong(Song song){
        listSong.add(song);
    }

    /**
     * Reset the media player to play song
     * Play song in the list using "position"
     */
    private void playQueue(){
        if(listSong.size() > 0){
            try{
                mediaPlayer.reset();
                mediaPlayer.setDataSource(listSong.get(position).getWebmURL());
                mediaPlayer.prepareAsync();
                status = MusicPlayerModel.BUFFERING;
                broadcastChangeStatus();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    /**
     * Play or resume the song
     */
    @Override
    public void play() {
        if (!mediaPlayer.isPlaying()) {
            if(resumePosition == 0){
                playQueue();
            }else{
                mediaPlayer.seekTo(resumePosition);
                mediaPlayer.start();
                status = MusicPlayerModel.PLAYING;
                broadcastChangeStatus();
            }
        }
    }

    /**
     * Pause the song
     */
    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            status = MusicPlayerModel.PAUSE;
            broadcastChangeStatus();
        }
    }

    /**
     * Set the next song
     * and reset the media player
     */
    @Override
    public void next() {
        if (position < listSong.size()-1) {
            position += 1;

        } else {
            position = 0;
        }
        resumePosition = 0;
        playQueue();
    }

    @Override
    public void prev() {
        if (position <= 0) {
            position = listSong.size() - 1;
        } else {
            position -= 1;
        }
        resumePosition = 0;
        playQueue();
    }

    /**
     * Get duration of the song if the song is ready
     * @return duration of the song
     */
    @Override
    public int getDuration() {
        if(status != MusicPlayerModel.BUFFERING){
            return mediaPlayer.getDuration();
        }else{
            return 0;
        }
    }

    /**
     * Get current position in the song if the song is playing
     * @return current position in the song
     */
    @Override
    public int getPosition() {
        if(status != MusicPlayerModel.BUFFERING){
            resumePosition = mediaPlayer.getCurrentPosition();
            return resumePosition;
        }else{
            return 0;
        }
    }

    @Override
    public void playNow(Song song) {

    }

    @Override
    public void seekTo(int pos) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * Play the next song in the queue after the song is played
     * Set the position 0 if the song is the last
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(position < listSong.size()-1){
            position += 1;
            resumePosition= 0;
            playQueue();
        }else{
            position = 0;
        }
    }

    /**
     * When the song is ready, play it
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        status = MusicPlayerModel.PLAYING;
        broadcastChangeStatus();
        mp.start();

    }

    /**
     * TODO : implement background controls
     * @param focusState
     */
    @Override
    public void onAudioFocusChange(int focusState) {
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initMusicPlayer();
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }



    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }



    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }
    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    //BroadcastSection

    /**
     * Register Broadcast Receiver
     */
    private void registerAllBroadcastReceiver(){
        registerAddQueueReceiver();
        registerPlayReceiver();
        registerPauseReceiver();
        registerPrevReceiver();
        registerNextReceiver();
        registerSeekToReceiver();
        registerRepeatReceiver();
        registerShuffleReceiver();
    }

    /**
     * Unregister Broadcast Receiver
     */
    private void unregisterAllBroadcastReceiver(){
        unregisterReceiver(addQueue);
        unregisterReceiver(playMusic);
        unregisterReceiver(pauseMusic);
        unregisterReceiver(nextSong);
        unregisterReceiver(prevSong);
        unregisterReceiver(SeekTo);
        unregisterReceiver(toggleRepeat);
        unregisterReceiver(toggleShuffle);
    }


    private BroadcastReceiver addQueue = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("add list");
            Song song = (Song) intent.getSerializableExtra("song");
            if(song != null){
                addToListSong(song);
            }
        }
    };
    private void registerAddQueueReceiver(){
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_ADD_PLAYLIST);
        registerReceiver(addQueue, filter);
    }


    private BroadcastReceiver playMusic = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("play");
            play();
        }
    };

    private void registerPlayReceiver(){
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_PLAY);
        registerReceiver(playMusic, filter);
    }

    private BroadcastReceiver pauseMusic = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pause();
        }
    };

    private void registerPauseReceiver(){
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_PAUSE);
        registerReceiver(pauseMusic, filter);
    }

    private BroadcastReceiver nextSong = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            next();
        }
    };

    private void registerNextReceiver(){
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_NEXT);
        registerReceiver(nextSong, filter);
    }


    private BroadcastReceiver SeekTo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            seekTo(intent.getIntExtra(MusicPlayerService.INTENT_BROADCAST_SEEK_TO,resumePosition));
        }
    };

    private void registerSeekToReceiver() {
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_SEEK_TO);
        registerReceiver(SeekTo, filter);
    }

    private BroadcastReceiver prevSong = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            prev();
        }
    };

    private void registerPrevReceiver() {
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_PREV);
        registerReceiver(prevSong, filter);
    }

    private BroadcastReceiver toggleRepeat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mediaPlayer.setLooping(!mediaPlayer.isLooping());
            broadcastChangeStatus();
        }
    };

    private void registerRepeatReceiver() {
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_TOGGLE_REPEAT);
        registerReceiver(toggleRepeat, filter);
    }

    private BroadcastReceiver toggleShuffle = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            shuffle = !shuffle;
            broadcastChangeStatus();
        }
    };

    private void registerShuffleReceiver() {
        IntentFilter filter = new IntentFilter(MusicPlayerService.BROADCAST_TOGGLE_SHUFFLE);
        registerReceiver(toggleShuffle, filter);
    }

    /**
     * Send information about the music service using ModelPlayerModel
     */
    private void broadcastChangeStatus(){
        Intent intent = new Intent();
        intent.setAction(NOTIFICATION_STATUS);
        MusicPlayerModel musicPlayerModel = new MusicPlayerModel(status, listSong.get(position));
        musicPlayerModel.setSongList(listSong);
        musicPlayerModel.setPosition(position);
        musicPlayerModel.setLooping(mediaPlayer.isLooping());
        musicPlayerModel.setShuffle(shuffle);
        musicPlayerModel.setResumePosition(resumePosition);
        switch (status){
            case MusicPlayerModel.BUFFERING:
                break;
            case MusicPlayerModel.PAUSE:
            case MusicPlayerModel.PLAYING:
                musicPlayerModel.setDuration(mediaPlayer.getDuration());
                break;
        }
        intent.putExtra(musicPlayerModel.INTENT_PARAM,musicPlayerModel);
        sendBroadcast(intent);
    }
}

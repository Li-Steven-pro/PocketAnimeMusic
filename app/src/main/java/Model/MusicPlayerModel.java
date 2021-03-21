package Model;

import java.io.Serializable;
import java.util.List;

public class MusicPlayerModel implements Serializable {

    private List<Song> songList;
    private int position;
    private Song song;
    private int duration;
    private int status;
    private Boolean isLooping;
    private Boolean isShuffle;
    private int resumePosition;

    public final static String INTENT_PARAM = "pocketanimemusicplayermodel";
    public final static int BUFFERING = 1;
    public final static int PLAYING = 2;
    public final static int PAUSE = 3;

    public MusicPlayerModel(int status, Song song){
        this.status = status;
        this.song = song;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Boolean getLooping() {
        return isLooping;
    }

    public void setLooping(Boolean looping) {
        isLooping = looping;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getResumePosition() {
        return resumePosition;
    }

    public void setResumePosition(int resumePosition) {
        this.resumePosition = resumePosition;
    }

    public Boolean getShuffle() {
        return isShuffle;
    }

    public void setShuffle(Boolean shuffle) {
        isShuffle = shuffle;
    }
}

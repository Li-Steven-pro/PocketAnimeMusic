package steven.li.pocketanimemusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Model.MusicPlayerModel;

public class MusicPlayerViewModel extends ViewModel {
    private final MutableLiveData<MusicPlayerModel> musicPlayer = new MutableLiveData<MusicPlayerModel>();
    private final MutableLiveData<Integer> currentPosition = new MutableLiveData<Integer>();

    public void setMusicPlayer(MusicPlayerModel musicPlayer){
        this.setCurrentPosition(musicPlayer.getResumePosition());
        this.musicPlayer.setValue(musicPlayer);
    }

    public void setCurrentPosition(Integer currentPosition){
        this.currentPosition.postValue(currentPosition);
    }

    public LiveData<Integer> getCurrectPosition(){return  this.currentPosition;}

    public LiveData<MusicPlayerModel> getMusicPlayer(){
        return this.musicPlayer;
    }
}

package steven.li.pocketanimemusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Model.MalAnime;

public class MalAnimeViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<MalAnime>> animeList = new MutableLiveData<ArrayList<MalAnime>>();

    public void setAnimeList(ArrayList animeList){
        this.animeList.postValue(animeList);
    }

    public LiveData<ArrayList<MalAnime>> getAnimeList(){
        return this.animeList;
    }
}


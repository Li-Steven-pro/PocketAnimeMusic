package steven.li.pocketanimemusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Model.Anime;

public class AnimeViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Anime>> animeList = new MutableLiveData<ArrayList<Anime>>();

    public void setAnimeList(ArrayList animeList){
        this.animeList.setValue(animeList);
    }

    public LiveData<ArrayList<Anime>> getAnimeList(){
        return this.animeList;
    }
}


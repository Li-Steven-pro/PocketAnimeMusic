package steven.li.pocketanimemusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import Model.Anime;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<Anime>> animeList = new MutableLiveData<List<Anime>>();

    public void setAnimeList(List animeList){
        this.animeList.setValue(animeList);
    }

    public LiveData<List<Anime>> getAnimeList(){
        return this.animeList;
    }
}


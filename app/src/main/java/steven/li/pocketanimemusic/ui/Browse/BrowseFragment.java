package steven.li.pocketanimemusic.ui.Browse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import API.AnimeTheme;
import Model.Anime;
import Model.Artist;
import steven.li.pocketanimemusic.AnimeListRecyclerViewAdapter;
import steven.li.pocketanimemusic.R;
import steven.li.pocketanimemusic.SearchViewModel;

public class BrowseFragment extends Fragment implements AnimeTheme.OnAnimeThemeListener {
;

    private SearchViewModel model;
    AnimeListRecyclerViewAdapter animeListAdapter;
    private RecyclerView rv;
    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.searchTextField);
        MaterialButton btn_search = view.findViewById(R.id.btn_search);
        CircularProgressIndicator progressSearch = view.findViewById(R.id.progress_search);

        model = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressSearch.setVisibility(View.VISIBLE);
                String name = textInputLayout.getEditText().getText().toString();
                new AnimeTheme(getContext()).searchAnime(name, BrowseFragment.this);
            }
        });

        rv = view.findViewById(R.id.recyclerView);
        // Instanciate the adapter
        animeListAdapter= new AnimeListRecyclerViewAdapter(view.getContext());
        // Set the adapter in the list view
        rv.setAdapter(animeListAdapter);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

        model.getAnimeList().observe(getViewLifecycleOwner(), item ->{
            // Set the research results
            animeListAdapter.setData(item);
            progressSearch.setVisibility(View.GONE);
            // Notify the adapter that he needs to update the UI.
            animeListAdapter.notifyDataSetChanged();
        });
        return view;
    }

    @Override
    public void onAnilistCompleted(List<Anime> animes) {

    }

    @Override
    public void onAnimeCompleted(Anime anime) {

    }

    @Override
    public void onArtistCompleted(Artist artist) {

    }

    @Override
    public void onSearchAnimeCompleted(List<Anime> animes) {
        model.setAnimeList(animes);
    }
}
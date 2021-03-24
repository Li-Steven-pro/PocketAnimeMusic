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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import API.AnimeThemesMoeAPI;
import Model.Anime;
import steven.li.pocketanimemusic.AnimeListAdapter;
import steven.li.pocketanimemusic.AnimeListRecyclerViewAdapter;
import steven.li.pocketanimemusic.AnimeViewModel;
import steven.li.pocketanimemusic.MalAnimeViewModel;
import steven.li.pocketanimemusic.R;
import steven.li.pocketanimemusic.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchViewModel model;
    private RecyclerView rv;
    public BrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.searchTextField);
        MaterialButton btn_search = view.findViewById(R.id.btn_search);
        model = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AnimeThemesMoeAPI(getContext()).search(textInputLayout.getEditText().getText().toString(), new AnimeThemesMoeAPI.OnAnimesListener() {
                    @Override
                    public void onAnimesCompleted(List<Anime> animeList) {
                        model.setAnimeList(animeList);
                    }
                });

            }
        });

        rv = view.findViewById(R.id.recyclerView);
        model.getAnimeList().observe(getViewLifecycleOwner(), item ->{
            // Instanciate the adapter
            AnimeListRecyclerViewAdapter animeListAdapter= new AnimeListRecyclerViewAdapter(view.getContext(),item);
            // Set the adapter in the list view
            rv.setAdapter(animeListAdapter);
            rv.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
            // Notify the adapter that he needs to update the UI.
            animeListAdapter.notifyDataSetChanged();
        });
        return view;
    }
}
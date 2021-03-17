package steven.li.pocketanimemusic.ui.List;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import API.AnimeThemeAPI;
import Model.Anime;
import steven.li.pocketanimemusic.AnimeListAdapter;
import steven.li.pocketanimemusic.AnimeViewModel;
import steven.li.pocketanimemusic.R;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MyListFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Anime> animeList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Anime> listAnime;
    private ListView listView;
    private View view;
    private AnimeListAdapter animeListAdapter;

    private AnimeViewModel model;

    public MyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyListFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static MyListFragment newInstance(String param1, String param2) {
        MyListFragment fragment = new MyListFragment();
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

        // Inflate the layout "fragment_list" for this fragment
        view =  inflater.inflate(R.layout.fragment_list, container, false);
        // Set the list view
        listView = view.findViewById(R.id.list_view);
        // Load the AnimeViewModel
        model = new ViewModelProvider(requireActivity()).get(AnimeViewModel.class);
        // Add an observer to update the list of item showed
        model.getAnimeList().observe(getViewLifecycleOwner(), item ->{
            // Instanciate the adapter
            animeListAdapter= new AnimeListAdapter(view.getContext(),item);
            // Set the adapter in the list view
            listView.setAdapter(animeListAdapter);
            // Notify the adapter that he needs to update the UI.
            animeListAdapter.notifyDataSetChanged();
        });
        return view;
    }

    /*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        System.out.println("Sauvegarde de la list");
        super.onSaveInstanceState(outState);
        outState.putSerializable("animeList", listAnime);
        Toast.makeText(this.getContext(), "Sauvegarde de la list", Toast.LENGTH_SHORT).show();
    }*/
}
package steven.li.pocketanimemusic.ui.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.IBinder;
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
import steven.li.pocketanimemusic.AppActivity;
import steven.li.pocketanimemusic.R;
import steven.li.pocketanimemusic.service.mediaplayer.MusicPlayerService;

public class MyListFragment extends Fragment {

    private ListView listView;
    private View view;
    private AnimeListAdapter animeListAdapter;
    private AppActivity mActivity;
    private AnimeViewModel model;

    public MyListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AppActivity){
            mActivity = (AppActivity) context;
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
            animeListAdapter= new AnimeListAdapter(view.getContext(),item,requireActivity());
            // Set the adapter in the list view
            listView.setAdapter(animeListAdapter);
            // Notify the adapter that he needs to update the UI.
            animeListAdapter.notifyDataSetChanged();
        });

        return view;
    }

}
package steven.li.pocketanimemusic.ui.Setting;

import android.os.Bundle;

import steven.li.pocketanimemusic.R;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        setHasOptionsMenu(true);
        getActivity().getActionBar();
    }


}
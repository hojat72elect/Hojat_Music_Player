package ca.on.sudbury.hojat.hojatmusicplayer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static ca.on.sudbury.hojat.hojatmusicplayer.MainActivity.musicFiles;

/**
 * The fragment that creates "SONGS" tab of the app.
 */
public class SongsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    static MusicAdapter musicAdapter;
    RecyclerView recyclerView;

    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the UI of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if (!(musicFiles.size() < 1)) {
            //we have some music files on this device.
            musicAdapter = new MusicAdapter(getContext(), musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                    false));
        }
        return view;
    }
}
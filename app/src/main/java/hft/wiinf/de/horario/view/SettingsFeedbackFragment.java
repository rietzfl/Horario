package hft.wiinf.de.horario.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hft.wiinf.de.horario.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFeedbackFragment extends Fragment {


    public SettingsFeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_feedback, container, false);
    }

}

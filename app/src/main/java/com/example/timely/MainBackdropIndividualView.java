package com.example.timely;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainBackdropIndividualView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainBackdropIndividualView extends Fragment implements View.OnClickListener {

    private Presentation datum;

    public MainBackdropIndividualView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            datum = (Presentation) bundle.getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_backdrop_solo_view, container, false);
        TextView duration = root.findViewById(R.id.main_backdrop_presentation_solo_duration);
        duration.setText(datum.getDuration());
        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_backdrop_delete_button) {
            MainActivity mainActivity = (MainActivity)getActivity();
            if (mainActivity != null) {
                mainActivity.deleteData(this.datum);
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

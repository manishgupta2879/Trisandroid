package layout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcs.tim.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificationResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificationResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public VerificationResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VerificationResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificationResultFragment newInstance(String param1) {
        VerificationResultFragment fragment = new VerificationResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        Log.d("CHECK4",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d("CHECK5",mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getArguments()!=null){
            Log.d("CHECK6",getArguments().getString(ARG_PARAM1));
        }else{
            Log.d("CHECK6","null");
        }
        return inflater.inflate(R.layout.new_fragment_verification_result, container, false);
    }

}

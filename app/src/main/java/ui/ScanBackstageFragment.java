package ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secbox.zhaoqc.secbox.R;


public class ScanBackstageFragment extends BaseFragment {


    public ScanBackstageFragment() {
        // Required empty public constructor
    }

    public static ScanBackstageFragment newInstance() {
        ScanBackstageFragment fragment = new ScanBackstageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_scan_tools;
        position = 3;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scan_backstage, container, false);
        return rootView;
    }

}

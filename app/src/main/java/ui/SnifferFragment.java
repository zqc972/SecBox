package ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secbox.zhaoqc.secbox.R;


public class SnifferFragment extends BaseFragment {

    public SnifferFragment() {
        // Required empty public constructor
    }

    public static SnifferFragment newInstance() {
        SnifferFragment fragment = new SnifferFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sniffer, container, false);
        return rootView;
    }

    @Override
    public String getTitle() {
        return "抓包工具";
    }

}

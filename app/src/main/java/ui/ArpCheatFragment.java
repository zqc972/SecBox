package ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secbox.zhaoqc.secbox.R;


public class ArpCheatFragment extends BaseFragment {

    public ArpCheatFragment() {
        // Required empty public constructor
    }

    public static ArpCheatFragment newInstance() {
        ArpCheatFragment fragment = new ArpCheatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_cheat_attack;
        position = 0;

        return inflater.inflate(R.layout.fragment_arp_cheat, container, false);
    }

    @Override
    public String getTitle() {
        return "Arp攻击";
    }
}

package ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secbox.zhaoqc.secbox.R;


public class PasswordGenenratorFragment extends BaseFragment {


    public PasswordGenenratorFragment() {
        // Required empty public constructor
    }


    public static PasswordGenenratorFragment newInstance() {
        PasswordGenenratorFragment fragment = new PasswordGenenratorFragment();
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
        return inflater.inflate(R.layout.fragment_password_genenrator, container, false);
    }

    @Override
    public String getTitle() {
        return "密码生成器";
    }

}

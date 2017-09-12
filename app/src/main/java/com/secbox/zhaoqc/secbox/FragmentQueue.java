package com.secbox.zhaoqc.secbox;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ui.BaseFragment;

/**
 * Created by zhaoqc on 17-8-27.
 */

public class FragmentQueue {
    public static List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

    public static void addFragment(BaseFragment fragment) {
        fragmentList.add(fragment);
    }

    public static BaseFragment getFragment(int index) {
        return fragmentList.get(index);
    }

}

package com.app.whosnextapp.messaging.videocall.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentExecuotr {

    public static void addFragment(FragmentManager fragmentManager, int containerId, Fragment fragment, String tag) {
        fragmentManager.beginTransaction().replace(containerId, fragment, tag).commitAllowingStateLoss();
    }
}

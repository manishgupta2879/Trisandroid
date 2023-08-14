package com.pcs.tim.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.numOfTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position){
            case 0:
                return new VerificationResultRefugeeInfo();
            case 1:
                return new VerificationResultPLKS();
            default:
                return new VerificationResultRefugeeInfo();

        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

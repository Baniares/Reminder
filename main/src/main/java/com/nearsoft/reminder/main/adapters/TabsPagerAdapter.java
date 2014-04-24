package com.nearsoft.reminder.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.nearsoft.reminder.main.CurrentRemindersFragment;
import com.nearsoft.reminder.main.PassedRemindersFragment;
import com.nearsoft.reminder.main.ProfileFragment;

/**
 * Created by Baniares on 4/8/14.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> fragments = new SparseArray<Fragment>();
    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int index){
        fragments.put(0,new CurrentRemindersFragment());
        fragments.put(1,new PassedRemindersFragment());
        fragments.put(2,new ProfileFragment());
        switch (index){
            case 0:
                return fragments.get(0);
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
        }
        return null;
    }

    @Override
    public int getCount(){
        return 3;
    }
}

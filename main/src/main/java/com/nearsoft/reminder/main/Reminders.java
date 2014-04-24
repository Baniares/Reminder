package com.nearsoft.reminder.main;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nearsoft.reminder.main.adapters.TabsPagerAdapter;

public class Reminders extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private MenuItem addReminder;
    private MenuItem editProfile;
    private String[] tabs = {"Current","Passed","Profile"};

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminders);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab_name : tabs){
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                switch(position){
                    case 0:
                        CurrentRemindersFragment current = (CurrentRemindersFragment)
                                viewPager.getAdapter().instantiateItem(
                                        viewPager,viewPager.getCurrentItem());
                        current.updateDB();
                        addReminder.setVisible(true);
                        editProfile.setVisible(false);

                        break;
                    case 1:
                        PassedRemindersFragment passed = (PassedRemindersFragment)
                                viewPager.getAdapter().instantiateItem(
                                        viewPager,viewPager.getCurrentItem());
                        passed.updateDB();
                        addReminder.setVisible(true);
                        editProfile.setVisible(false);
                        break;
                    case 2:
                        addReminder.setVisible(false);
                        editProfile.setVisible(true);
                        break;
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reminders, menu);
        editProfile = menu.findItem(R.id.editProfileActionBar);
        addReminder = menu.findItem(R.id.addReminderActionBar);
        editProfile.setVisible(false);
        viewPager.setEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addReminderActionBar:
                Intent i = new Intent(this,ReminderFragmentActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("mode",1);
                i.putExtra("ReminderId",-6);
                startActivity(i);
                return true;
            case R.id.editProfileActionBar:
                ProfileFragment profile = (ProfileFragment)
                        viewPager.getAdapter().instantiateItem(
                                viewPager,viewPager.getCurrentItem());
                profile.setEditMode();
                return true;
            default:
                Toast.makeText(Reminders.this,"Error :B",Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}
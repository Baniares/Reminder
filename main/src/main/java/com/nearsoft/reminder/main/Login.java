package com.nearsoft.reminder.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Login extends FragmentActivity {
    private FragmentLogin fragmentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            fragmentLogin = new FragmentLogin();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragmentLogin)
                    .commit();
        }else{
            fragmentLogin = (FragmentLogin) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
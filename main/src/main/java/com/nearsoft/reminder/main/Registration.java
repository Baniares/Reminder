package com.nearsoft.reminder.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;

/**
 * Created by Baniares on 3/28/14.
 */

public class Registration extends Activity {
    private ProfilePictureView profilePic;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        profilePic=(ProfilePictureView)findViewById(R.id.profilepic);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Session.setActiveSession((Session) extras.getSerializable("fb_session"));
        }
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
        Session session = Session.getActiveSession();
        profilePic.setProfileId("Baniaresu");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

}
